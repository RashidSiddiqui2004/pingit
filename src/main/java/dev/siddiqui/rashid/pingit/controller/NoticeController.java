package dev.siddiqui.rashid.pingit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.siddiqui.rashid.pingit.entity.NoticeDTO;
import dev.siddiqui.rashid.pingit.service.NoticeFetcherService;

@RestController
@RequestMapping(path = "/notice")
public class NoticeController {

    @Autowired
    private NoticeFetcherService noticeFetcherService;

    @GetMapping
    private ResponseEntity<?> getAllNotices() {
        try {
            List<NoticeDTO> notices = noticeFetcherService.fetchAll();

            for (NoticeDTO noticeDTO : notices) {
                noticeFetcherService.create(noticeDTO);
            }

            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not fetch notices at this time.");
        }
    }

    @GetMapping(path = "/{id}")
    private ResponseEntity<?> getNoticeMetaData(@RequestParam Long noticeId) {

        try {
            NoticeDTO noticeDTO = noticeFetcherService.findById(noticeId);
            return ResponseEntity.ok(noticeDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Notice with given id doesn't exist!");
        }

    }
}
