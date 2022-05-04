package com.sd.stackoverflow.controller;

import com.sd.stackoverflow.dto.QuestionDTO;
import com.sd.stackoverflow.dto.QuestionVoteCounter;
import com.sd.stackoverflow.service.QuestionService;
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
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/questions/addQuestion/{userId}")
    public ResponseEntity<?> addQuestion(@RequestBody QuestionDTO question, @PathVariable Long userId) {
        QuestionDTO addedQuestion = questionService.addQuestion(question, userId);

        return new ResponseEntity<>(addedQuestion, HttpStatus.OK);
    }

    @RequestMapping(value = "/questions/getAllQuestions/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllQuestions(@PathVariable Long userId) {
        List<QuestionDTO> questionDTOList = questionService.getAllQuestions(userId);

        return new ResponseEntity<>(questionDTOList, HttpStatus.OK);
    }

    @RequestMapping(value = "/questions/getQuestion/{id}/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getQuestion(@PathVariable Long id, @PathVariable Long userId) {
        QuestionDTO question = questionService.getQuestion(id, userId);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @RequestMapping(value = "/questions/getQuestionsByTitleAndTags/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getQuestionByTitle(@RequestParam String title, @PathVariable Long userId) {
        List<QuestionDTO> question = questionService.getQuestionsByTitleAndTags(title, userId);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @RequestMapping(value = "/questions/getTags", method = RequestMethod.GET)
    public ResponseEntity<?> getTags() {
        return new ResponseEntity<>(questionService.getAllTags(), HttpStatus.OK);
    }

    @PostMapping("/questions/updateQuestion/{userId}")
    public ResponseEntity<?> updateQuestion(@RequestBody QuestionDTO question, @PathVariable Long userId) {
        QuestionDTO updatedQuestion = questionService.updateQuestion(question, userId);

        return new ResponseEntity<>(updatedQuestion, HttpStatus.OK);
    }

    @DeleteMapping("/questions/deleteQuestion/{id}/{userId}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable Long id, @PathVariable Long userId) {
        questionService.deleteQuestion(id, userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/questions/setQuestionVotes/{id}/{userId}")
    public ResponseEntity<?> setQuestionVotes(@PathVariable Long id, @PathVariable Long userId, @RequestParam Boolean vote) {
        QuestionVoteCounter question = questionService.voteOnQuestion(id, userId, vote);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }
}
