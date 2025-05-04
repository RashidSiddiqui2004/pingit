package dev.siddiqui.rashid.pingit.repository;

import dev.siddiqui.rashid.pingit.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
