package dev.siddiqui.rashid.pingit.service;

public interface NotificationService {

    public boolean SendNotificationToUserByRollNumber(String NoticeUrl, String noticeTitle, String rollNumber);

    public boolean SendNotificationToUserByUserCollegeMail(String NoticeUrl, String noticeTitle,
            String userCollegeMail);

}
