package com.sd.stackoverflow.controller;

import com.sd.stackoverflow.dto.QuestionDTO;
import com.sd.stackoverflow.model.Question;
import com.sd.stackoverflow.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @Transactional
    @PostMapping("/questions/addQuestion")
    public ResponseEntity<?> addQuestion(@RequestBody Question question) {
        Question addedQuestion = questionService.addQuestion(question);

        return new ResponseEntity<>(addedQuestion, HttpStatus.OK);
    }

    @RequestMapping(value = "/questions/getAllQuestions", method = RequestMethod.GET)
    public ResponseEntity<?> getAllQuestions() {
        List<QuestionDTO> questionDTOList = questionService.getAllQuestions();

        return new ResponseEntity<>(questionDTOList, HttpStatus.OK);
    }

    @RequestMapping(value = "/questions/getQuestion", method = RequestMethod.GET)
    public ResponseEntity<?> getQuestion(@RequestBody Long id) {
        Question question = questionService.getQuestion(id);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @PostMapping("/questions/updateQuestion")
    public ResponseEntity<?> updateQuestion(@RequestBody Question question) {
        Question updatedQuestion = questionService.updateQuestion(question);

        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @DeleteMapping("/questions/deleteQuestion/{id}")
    public void deleteQuestionById(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }
}
