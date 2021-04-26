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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Board board;
}
