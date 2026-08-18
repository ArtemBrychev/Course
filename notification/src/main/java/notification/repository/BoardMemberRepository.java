package notification.repository;

import notification.model.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMemberRepository
        extends JpaRepository<BoardMember, Long> {

    List<BoardMember> findAllByBoardId(Long boardId);

    void deleteByBoardIdAndUserId(
            Long boardId,
            Long userId
    );
}