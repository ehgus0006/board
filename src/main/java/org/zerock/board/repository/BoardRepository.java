package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> , SearchBoardRepository {

//    한개의 로우(Object) 내에 Object[ ]로 나옴


    @Query("select b,w from Board b left join b.writer w where b.bno=:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query("select b,r from Board b left JOIN Reply r on r.board = b where b.bno=:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "select b, w, count(r) from Board b left join b.writer w left join Reply r on r.board=b group by b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록화면에 필요한 데이터

    @Query("select b,w, count(r) from Board b left join b.writer w left join Reply r on r.board= b where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

}
