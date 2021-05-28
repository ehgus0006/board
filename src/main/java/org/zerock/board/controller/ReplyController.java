package org.zerock.board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Reply;
import org.zerock.board.service.ReplyService;

import java.util.List;

@RestController
@RequestMapping(value = "/replies/")
@Slf4j
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService; // 자동 주입을 위해 final

    @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {
        log.info("bno:" + bno);
        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }



    // 일반적인 Form 데이터는 그냥 DTO만 사용해서 값이 세팅이 되지만 JSON형태는 그냥 받을수 없다.
    // JSON 형태의 데이터를 받을때는 @RequestBody를 사용해야한다.
    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {
        log.info("replyDTO=" + replyDTO);
        System.out.println("게시판번호는" + replyDTO.getBno());
        Long rno = replyService.register(replyDTO);

        // 리턴할때 ResponseEntity사용이유는 HttpStatus를 같이 주기 위해 사용한다.
        return new ResponseEntity<>(rno, HttpStatus.OK);
    }


    // url의 PathVariable rno 값을 추출하여 삭제를 한다
    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
        log.info("rno" + rno);
        replyService.remove(rno);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {
        log.info("replyDTO"+replyDTO);

        replyService.modify(replyDTO);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
