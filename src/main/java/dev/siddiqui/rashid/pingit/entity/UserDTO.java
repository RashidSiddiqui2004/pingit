package dev.siddiqui.rashid.pingit.entity;

public record UserDTO(
                Long id, String username,
                String password,
                String collegeMail,
                String subscribingMail,
                String rollNumber,
                String branch) {
}
