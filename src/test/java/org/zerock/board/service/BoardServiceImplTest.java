package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.repository.BoardRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BoardServiceImplTest {

    @Autowired
    private BoardService boardService;

    @Transactional
    @Test
    public void testModify() {
        Long bno = 2L;

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(bno)
                .title("제목 변경합니다.")
                .content("내용 변경합니다.")
                .build();
        boardService.modify(boardDTO);
    }

    @Test
    public void testRemove() {
        Long bno = 1L;
        boardService.removeWithReplies(bno);
    }

    @Test
    public void testGet() {
        Long bno = 100L;
        BoardDTO boardDTO = boardService.get(bno);
        System.out.println(boardDTO);

    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for (BoardDTO boardDTO : result.getDtoList()) {
            System.out.println(boardDTO);
        }
    }

    @Test
    public void testRegister() {
        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("Test...")
                .writerEmail("user55@aaa.com") // 현재 데이터베이스에 존재하는 회원이메일일
               .build();
        Long bno = boardService.register(dto);

    }

}