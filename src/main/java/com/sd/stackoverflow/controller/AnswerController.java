package com.sd.stackoverflow.controller;

import com.sd.stackoverflow.dto.AnswerDTO;
import com.sd.stackoverflow.dto.AnswerVoteCounter;
import com.sd.stackoverflow.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
@CrossOrigin(origins = "http://localhost:4200")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/answers/addAnswer/{userId}")
    public ResponseEntity<?> addAnswer(@RequestBody AnswerDTO answer, @PathVariable Long userId) {
        AnswerDTO addedAnswer = answerService.addAnswer(answer, userId);

        return new ResponseEntity<>(addedAnswer, HttpStatus.OK);
    }

    @RequestMapping(value = "/answers/getAllAnswers/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAnswers(@PathVariable Long userId) {
        List<AnswerDTO> answerDTOList = answerService.getAllAnswers(userId);

        return new ResponseEntity<>(answerDTOList, HttpStatus.OK);
    }

    @RequestMapping(value = "/answers/getAnswer/{id}/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAnswer(@PathVariable Long id, @PathVariable Long userId) {
        AnswerDTO answer = answerService.getAnswer(id, userId);

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @PostMapping("/answers/updateAnswer/{userId}")
    public ResponseEntity<?> updateAnswer(@RequestBody AnswerDTO answer, @PathVariable Long userId) {
        AnswerDTO updateAnswer = answerService.updateAnswer(answer, userId);

        return new ResponseEntity<>(updateAnswer, HttpStatus.OK);
    }

    @DeleteMapping("/answers/deleteAnswer/{id}/{userId}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable Long id, @PathVariable Long userId) {
        answerService.deleteAnswer(id, userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/answers/setAnswerVotes/{id}/{userId}")
    public ResponseEntity<?> setAnswerVotes(@PathVariable Long id, @PathVariable Long userId, @RequestParam Boolean vote) {
        AnswerVoteCounter answer = answerService.voteOnAnswer(id, userId, vote);

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
}
