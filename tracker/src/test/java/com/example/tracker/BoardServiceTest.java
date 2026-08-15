package com.example.tracker;

import com.example.tracker.eventdriven.EventSender;
import com.example.tracker.model.Board;
import com.example.tracker.model.BoardMember;
import com.example.tracker.model.User;
import com.example.tracker.repository.BoardMemberRepository;
import com.example.tracker.repository.BoardRepository;
import com.example.tracker.repository.cache.BoardCacheRepository;
import com.example.tracker.service.BoardService;
import com.example.tracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    Board board1;
    Board board2;
    User user1;
    User user2;
    BoardMember boardMembership1;
    BoardMember boardMembership2;
    BoardMember boardMembership3;

    @Mock
    BoardRepository boardRepository;
    @Mock
    BoardCacheRepository boardCacheRepository;
    @Mock
    UserService userService;
    @Mock
    BoardMemberRepository boardMemberRepository;
    @Mock
    EventSender eventSender;

    @InjectMocks
    BoardService boardService;


    @BeforeEach
    public void init(){
        user1 = User.builder()
                .id(1L)
                .email("User1@emmail.com")
                .passwordHash("4f3f2f24f24f3")
                .build();

        user2 = User.builder()
                .id(2L)
                .email("User2@emmail.com")
                .passwordHash("4f3f2f24f24f3")
                .build();

        board1 = Board.builder()
                .id(1L)
                .title("Board1")
                .owner(user1)
                .build();

        board2 = Board.builder()
                .id(2L)
                .title("Board1")
                .owner(user1)
                .build();

        boardMembership1 = BoardMember.builder()
                .id(1L)
                .board(board2)
                .user(user1)
                .build();

        boardMembership2 = BoardMember.builder()
                .id(2L)
                .board(board1)
                .user(user1)
                .build();

        boardMembership3 = BoardMember.builder()
                .id(2L)
                .board(board2)
                .user(user2)
                .build();
    }

    @DisplayName("isOwner test on actual owner")
    @Test
    public void isOwnerOnOwnerShouldReturnTrue(){
        assertThat(boardService.isOwner(board1, user1)).isTrue();
    }

    @DisplayName("isOwner test on member")
    @Test
    public void isOwnerOnMemberShouldReturnFalse(){
        assertThat(boardService.isOwner(board2, user2)).isFalse();
    }

    @DisplayName("isOwner test on nonmember")
    @Test
    public void isOwnerOnNonMemberShouldReturnFalse(){
        assertThat(boardService.isOwner(board1, user2)).isFalse();
    }

    @DisplayName("isMemberOnOwner")
    @Test
    public void isMemberOnOwnerShouldReturnTrue(){
        when(boardMemberRepository.existsByBoardIdAndUserId(board1.getId(), user1.getId()))
                .thenReturn(true);

        assertThat(boardService.isMember(board1, user1)).isTrue();
        verify(boardMemberRepository, times(1))
                .existsByBoardIdAndUserId(board1.getId(), user1.getId());
    }

    @DisplayName("isMemberOnMember")
    @Test
    public void isMemberOnMemberShouldReturnTrue(){
        when(boardMemberRepository.existsByBoardIdAndUserId(board2.getId(), user2.getId()))
                .thenReturn(true);

        assertThat(boardService.isMember(board2, user2)).isTrue();
        verify(boardMemberRepository, times(1))
                .existsByBoardIdAndUserId(board2.getId(), user2.getId());
    }

    @DisplayName("isMemberOnNonMember")
    @Test
    public void isMemberOnNonMemberShouldReturnFalse(){
        when(boardMemberRepository.existsByBoardIdAndUserId(board1.getId(), user2.getId()))
                .thenReturn(false);

        assertThat(boardService.isMember(board1, user2)).isFalse();
        verify(boardMemberRepository, times(1))
                .existsByBoardIdAndUserId(board1.getId(), user2.getId());
    }

    @DisplayName("hasAcces on Owner")
    @Test
    public void hasAccessOnOwnerShouldReturnTrue(){
        /*when(boardMemberRepository.existsByBoardIdAndUserId(board2.getId(), user1.getId()))
                .thenReturn(true);*/

        assertThat(boardService.hasAccess(board2, user1)).isTrue();
    }

    @DisplayName("hasAcces on Member")
    @Test
    public void hasAccessOnMemberShouldReturnTrue(){
        when(boardMemberRepository.existsByBoardIdAndUserId(board2.getId(), user2.getId()))
                .thenReturn(true);

        assertThat(boardService.hasAccess(board2, user2)).isTrue();
    }

    @DisplayName("hasAcces on NonMember")
    @Test
    public void hasAccessOnNonMemberShouldReturnFalse(){
        when(boardMemberRepository.existsByBoardIdAndUserId(board1.getId(), user2.getId()))
                .thenReturn(false);

        assertThat(boardService.hasAccess(board1, user2)).isFalse();
    }


}
