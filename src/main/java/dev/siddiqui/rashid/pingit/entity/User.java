package dev.siddiqui.rashid.pingit.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
// @Entity: Marks this class as a JPA entity, which will be mapped to a table in
// the PostgreSQL database.
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // consider storing a hashed password

    @Column(nullable = false, unique = true)
    private String collegeMail;

    private String subscribingMail;

    private String rollNumber;
    private String branch;
}
