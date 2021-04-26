package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Reply;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying
    @Query("delete from Reply r where r.board.bno=:bno")
    void deleteByBno(Long bno);

    //게시물로 댓글 가져오기
    List<Reply> getRepliesByBoardOrderByRno(Board board);

}


