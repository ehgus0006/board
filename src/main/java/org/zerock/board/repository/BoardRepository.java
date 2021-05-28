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

    // 한개의 로우(Object) 내에 Object[ ]로 나옴
    // 게시판에 대해서 조회를 하는데 작성자를 뽑는 데이터
    // 내부에 있는 엔티티를 이용할 때는 LEFT JOIN뒤에 ON을 이용하는 부분이 없습니다 // 다대 1인경우
    // 멤버와 게시글은 1대다 형식이고 board에서는 ManyToOne상황 이기에 on 필요없음  / fk를 필요한 상황에서
    @Query("select b,w from Board b left join b.writer w where b.bno=:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    // 특정 게시물과 해당 게시물에 속한 댓글들을 조회해야하는 상황 // 연관관계 없는 엔티티 조인에서는 on을 사용해야함
    @Query("select b,r from Board b left JOIN Reply r on r.board = b where b.bno=:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    // select b,r from Board b left join Reply r on r.board = b where b.bno=:bno

    @Query(value = "select b, w, count(r) from Board b left join b.writer w left join Reply r on r.board=b group by b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록화면에 필요한 데이터


    @Query("select b,w, count(r) from Board b left join b.writer w left join Reply r on r.board= b where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);


}
