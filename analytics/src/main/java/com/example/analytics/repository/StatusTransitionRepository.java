package com.example.analytics.repository;

import com.example.analytics.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatusTransitionRepository extends JpaRepository<StatusTransitionEntity, Long> {

    List<StatusTransitionEntity> findByTaskIdOrderByOccurredAt(Long taskId);
    List<StatusTransitionEntity> findByBoardId(Long boardId);
    List<StatusTransitionEntity> findByUserId(Long userId);

}
