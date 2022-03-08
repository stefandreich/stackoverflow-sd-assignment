package com.sd.stackoverflow.service;

import com.sd.stackoverflow.dto.QuestionDTO;
import com.sd.stackoverflow.model.Question;
import com.sd.stackoverflow.repository.IQuestionRepository;
import com.sd.stackoverflow.service.customexceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final IQuestionRepository iQuestionRepository;

    public List<QuestionDTO> getAllQuestions() throws ResourceNotFoundException {
        List<QuestionDTO> questionList = iQuestionRepository.findAll().stream()
                .map(QuestionDTO::fromQuestionEntity)
                .collect(Collectors.toList());

        if (questionList.isEmpty()) {
            throw new ResourceNotFoundException("No questions found in database!");
        }

        return questionList;
    }

    public Question getQuestion(Long id) {
        Optional<Question> foundQuestion = iQuestionRepository.findById(id);

        if (foundQuestion.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        return foundQuestion.get();
    }

    public Question addQuestion(Question givenQuestion) throws ResourceNotFoundException {
        if (givenQuestion.getQuestionId() != null) {
            throw new ResourceNotFoundException("Question" + givenQuestion.getQuestionId() + " already found. Cannot perform create operation.");
        }

        givenQuestion.setQuestionDateCreated(LocalDateTime.now());

        return iQuestionRepository.save(givenQuestion);
    }

    public Question updateQuestion(Question question) throws ResourceNotFoundException {
        Question initialQuestion = iQuestionRepository.findById(question.getQuestionId()).orElse(null);

        if (initialQuestion == null) {
            throw new ResourceNotFoundException("Question with id " + question.getQuestionId() + " cannot be found.");
        } else {
            return iQuestionRepository.save(initialQuestion);
        }
    }

    public void deleteQuestion(Long id) throws ResourceNotFoundException {
        Optional<Question> foundQuestion = iQuestionRepository.findById(id);

        if (foundQuestion.isEmpty()) {
            throw new ResourceNotFoundException("Given question was not found. Delete operation could not be performed");
        } else {
            iQuestionRepository.delete(foundQuestion.get());
        }
    }
}
