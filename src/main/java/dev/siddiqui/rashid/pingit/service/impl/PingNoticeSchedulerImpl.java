package dev.siddiqui.rashid.pingit.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import dev.siddiqui.rashid.pingit.entity.NoticeDTO;
import dev.siddiqui.rashid.pingit.enums.NoticeStatus;
import dev.siddiqui.rashid.pingit.service.NoticeFetcherService;
import dev.siddiqui.rashid.pingit.service.NoticeParserService;
import dev.siddiqui.rashid.pingit.service.NotificationService;
import dev.siddiqui.rashid.pingit.service.PingNoticeScheduler;

public class PingNoticeSchedulerImpl implements PingNoticeScheduler {

    @Autowired
    private NoticeFetcherService noticeFetcherService;

    @Autowired
    private NoticeParserService noticeParserService;

    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    // Runs everyday at 12:00 (24 hour clock format)
    @Scheduled(cron = "0 12 * * *")
    public Boolean runNoticeJob() {

        try {
            System.out.println("Scheduled task running at: " + java.time.LocalDateTime.now());

            // Delete the previously stored notices from PostgreSQL database which have
            // status as `Success`

            List<NoticeDTO> allNotices = noticeFetcherService.findAll();

            List<NoticeDTO> failedNotices = new ArrayList<>();

            for (NoticeDTO noticeDTO : allNotices) {
                if (noticeDTO.noticeStatus() == NoticeStatus.SUCCESS) {
                    noticeFetcherService.delete(noticeDTO.id());
                } else if (noticeDTO.noticeStatus() == NoticeStatus.FAILURE) {
                    failedNotices.add(noticeDTO);
                }
            }

            Document document = noticeFetcherService.fetchAllNotices();

            List<NoticeDTO> notices = noticeParserService.getParsedNotices(document);

            notices.addAll(failedNotices);

            // store the notices temporarily in PostgreSQL database
            for (NoticeDTO noticeDTO : notices) {

                if (noticeDTO.id() == null) {
                    noticeFetcherService.create(noticeDTO);
                }

                String noticeTitle = noticeDTO.noticeTitle();
                String noticeURL = noticeDTO.noticeUrl();

                for (String embeddedRollNumber : noticeDTO.embeddedRollNumbers()) {

                    if (!notificationService.SendNotificationToUserByRollNumber(noticeURL, noticeTitle,
                            embeddedRollNumber)) {

                        NoticeDTO updatedNoticeDTO = new NoticeDTO(
                                noticeDTO.id(),
                                noticeDTO.noticeTitle(),
                                noticeDTO.noticeUrl(),
                                noticeDTO.noticeDate(),
                                NoticeStatus.FAILURE,
                                noticeDTO.embeddedMails(),
                                noticeDTO.embeddedRollNumbers());

                        noticeFetcherService.update(noticeDTO.id(), updatedNoticeDTO);

                        logger.warn("Failed to send notification to user with roll number: {}", embeddedRollNumber);

                    }

                }

                for (String embeddedClgMail : noticeDTO.embeddedMails()) {
                    if (!notificationService.SendNotificationToUserByUserCollegeMail(noticeURL, noticeTitle,
                            embeddedClgMail)) {

                        NoticeDTO updatedNoticeDTO = new NoticeDTO(
                                noticeDTO.id(),
                                noticeDTO.noticeTitle(),
                                noticeDTO.noticeUrl(),
                                noticeDTO.noticeDate(),
                                NoticeStatus.FAILURE,
                                noticeDTO.embeddedMails(),
                                noticeDTO.embeddedRollNumbers());

                        noticeFetcherService.update(noticeDTO.id(), updatedNoticeDTO);

                        logger.warn("Failed to send notification to user with college mail: {}", embeddedClgMail);
                    }
                }

                notificationService.SendNotificationToUserByRollNumber(noticeURL, noticeTitle, noticeURL);
            }

            logger.info("Notice Scheduler job completed successfully at {}", LocalDateTime.now());

        } catch (Exception e) {
            logger.error("Notice Scheduler job failed at {}. Reason: {}", LocalDateTime.now(), e.getMessage(), e);

        }

        return true;
    }
}
