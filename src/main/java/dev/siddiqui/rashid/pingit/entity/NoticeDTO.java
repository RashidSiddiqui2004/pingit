package dev.siddiqui.rashid.pingit.entity;

import java.util.Date;
import java.util.List;

import dev.siddiqui.rashid.pingit.enums.NoticeStatus;

public record NoticeDTO(
        Long id,
        String noticeTitle,
        String noticeUrl,
        Date noticeDate,
        NoticeStatus noticeStatus,
        List<String> embeddedMails,
        List<String> embeddedRollNumbers) {
}
