package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLOps;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("search1...........................");


        // board entity
        QBoard board = QBoard.board;
        // 연관관계 조인을 위한 댓글
        QReply reply = QReply.reply;
        // 연관관계 조인을 위한 멤버
        QMember member = QMember.member;

        // board 테이블의 레코드는 Board Entity 객체로 생성된다.
        JPQLQuery<Board> jpqlQuery = from(board);
        // ... from board b left join member m on b.writer_email = m.email
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));

        // ... from board b left join member m on b.writer_email = m.email
        //                          left join reply r on r.board.bno = b.bno
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
        // select * from board where bno = 1;
        // select b from board b left join reply r on r.board_bno = b.bno
        // jpqlQuery.select(board).where(board.bno.eq(2L));

        // select b.*, m.email , count(r.rno) from ....... group bt b.bno
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("----------------------------------------------");
        log.info("tuple="+tuple);
        log.info("----------------------------------------------");

        List<Tuple> result = tuple.fetch();
        log.info("result=" + result);
        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage.............................");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // select b, w , count(r) from Board b
        // left join b.writer w left join Reply r on r.board = b
        
        // 다중 결과를 반환 받을 때는 튜플을 사용한다.
        // 셀렉트의 결과값을 Tuple을 이용해서 처리한다
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        // where 절을 동적으로 만들기 위한 BooleanBuilder 객체를 생성하겠다.
        // where title like '%keyword%'
        // where title like '%keyword%' or content like '%keyword%';

        // type : "t" , "tc" , "tw" , "cw" , "c" , "w" ,  "tcw"
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);
        booleanBuilder.and(expression);

        if (type != null) { // 검색 조건이 있다는 의미.
            // split 분리 하라는 의미

            String[] typeArr = type.split(""); // 문자열의 글자 하나하나를 다 분리해서, 배열로 만들어라
            // type Arr - {"t"} , {"t", "c"}

            // 검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {
                // 원래 switch 문은 정수형으로만 가능했는데 버전 변경 후 문자 타입도 가능하다.
                switch (t){
                    case "t": // title like "%keyword%";
                        conditionBuilder.or(board.title.contains(keyword)); // contains = 포함하는지
                        break;
                    case "w": // member.email like "%keyword%"
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c": // board.content like "%keyword%"
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);

        //order by
        Sort sort = pageable.getSort();

        //tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            tuple.orderBy(new OrderSpecifier<>(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(board);
        // page처리
        // 예를 들어, 2번 page에 표시할 결과 레코드 10개 주세요
        // offset 10
        // 시작 인덱스를 지정하는 offset 조회할개수를 지정하는 limit
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();
        log.info("result=" + result);

        long count = tuple.fetchCount();
        log.info("count=" + count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,count);
    }
}
