package dev.siddiqui.rashid.pingit.repository;

import dev.siddiqui.rashid.pingit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCollegeMail(String collegeMail);

    Optional<User> findByRollNo(String rollNumber);
}
