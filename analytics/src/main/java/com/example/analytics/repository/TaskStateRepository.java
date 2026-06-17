package com.example.analytics.repository;

import com.example.analytics.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
    long countByStatus(String status);

    List<TaskStateEntity> findByBoardId(Long boardId);
}
