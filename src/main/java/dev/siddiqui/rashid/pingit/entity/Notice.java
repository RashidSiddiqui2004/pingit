package dev.siddiqui.rashid.pingit.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

import dev.siddiqui.rashid.pingit.enums.NoticeStatus;

@Data
@Entity
@Table(name = "notices")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String noticeTitle;

    @Column(length = 2048)
    private String noticeUrl;

    private Date noticeDate;

    private NoticeStatus noticeStatus;

    @Transient
    private List<String> embeddedMails;

    @Transient
    private List<String> embeddedRollNumbers;

}
