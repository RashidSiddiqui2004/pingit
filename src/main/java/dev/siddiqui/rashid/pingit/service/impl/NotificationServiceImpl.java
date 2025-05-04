package dev.siddiqui.rashid.pingit.service.impl;

import dev.siddiqui.rashid.pingit.entity.UserDTO;
import dev.siddiqui.rashid.pingit.service.NotificationService;
import dev.siddiqui.rashid.pingit.service.UserService;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean SendNotificationToUserByRollNumber(String noticeUrl, String noticeTitle, String rollNumber) {
        UserDTO user = userService.findByRollNumber(rollNumber);

        if (user == null) {
            logger.warn("User not found for Roll Number: {}", rollNumber);
            return true;
        }

        String content = """
                    <html>
                    <body>
                        <p>Hello <b>%s</b>,</p>
                        <p>A new notice has been published:</p>
                        <p><a href="%s">Click here to view the notice</a></p>
                        <br>
                        <p>Regards,<br>PingIt Team</p>
                    </body>
                    </html>
                """.formatted(user.username(), noticeUrl);

        String subject = "New Notice: " + noticeTitle;

        if (!sendMail(user.subscribingMail(), subject, content)) {
            return false;
        }

        logger.info("Notification sent to user: {}", user.subscribingMail());

        return true;
    }

    @Override
    public boolean SendNotificationToUserByUserCollegeMail(String noticeUrl, String noticeTitle,
            String userCollegeMail) {

        UserDTO user = userService.findByCollegeMail(userCollegeMail);

        if (user == null) {
            logger.warn("User not found for College Mail: {}", userCollegeMail);
            return true;
        }

        String content = """
                    <html>
                    <body>
                        <p>Hello <b>%s</b>,</p>
                        <p>A new notice relevant to you has been published:</p>
                        <p><a href="%s">Click here to view the notice</a></p>
                        <br>
                        <p>Regards,<br>PingIt Team</p>
                    </body>
                    </html>
                """.formatted(user.username(), noticeUrl);

        String subject = "New Notice: " + noticeTitle;

        if (!sendMail(user.subscribingMail(), subject, content)) {
            return false;
        }

        logger.info("Notification sent to user: {}", user.subscribingMail());

        return true;
    }

    private boolean sendMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Email successfully sent to: {}", to);

            return true;
        } catch (Exception e) {

            logger.error("Error sending email to: {}", to, e);
            return false;
        }
    }
}
