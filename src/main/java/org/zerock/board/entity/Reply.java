package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer;


    /*
    * 하나의 댓글은 오직 하나의 게시글에 소속되고
    * 하나의 게시글은 여러 개의 댓글을 가질 수 있다.
    * ManyToOne 의 관계일 때 Many 측의 레코드 가져올 때
    * 그럼 LAZY(지연), EAGER(즉) 로딩이냐?
    * EAGER 로딩한다.
    * */
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
}
