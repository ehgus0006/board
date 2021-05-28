package org.zerock.board.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.board.entity.Board;

public interface SearchBoardRepository {

    Board search1();


    // pageRequestDTO를 받지 않는 이유는 DTO를 Repository영역에 다루지 않기 위해서다.
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
