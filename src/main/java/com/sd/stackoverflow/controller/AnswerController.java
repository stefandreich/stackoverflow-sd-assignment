package com.sd.stackoverflow.controller;

import com.sd.stackoverflow.dto.AnswerDTO;
import com.sd.stackoverflow.model.Answer;
import com.sd.stackoverflow.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/answers/addAnswer")
    public ResponseEntity<?> addAnswer(@RequestBody Answer answer) {
        Answer addedAnswer = answerService.addAnswer(answer);

        return new ResponseEntity<>(addedAnswer, HttpStatus.OK);
    }

    @RequestMapping(value = "/answers/getAllAnswers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAnswers() {
        List<AnswerDTO> answerDTOList = answerService.getAllAnswers();

        return new ResponseEntity<>(answerDTOList, HttpStatus.OK);
    }

    @RequestMapping(value = "/answers/getAnswer", method = RequestMethod.GET)
    public ResponseEntity<?> getAnswer(@RequestBody Long id) {
        Answer answer = answerService.getAnswer(id);

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @PostMapping("/answers/updateAnswer")
    public ResponseEntity<?> updateAnswer(@RequestBody Answer answer) {
        Answer updateAnswer = answerService.updateAnswer(answer);

        return new ResponseEntity<>(updateAnswer, HttpStatus.OK);
    }

    @DeleteMapping("/answers/deleteAnswer/{id}")
    public void deleteAnswerById(@PathVariable Long id) {
        answerService.deleteAnswer(id);
    }
}
