package com.sd.stackoverflow.service;

import com.sd.stackoverflow.dto.AnswerDTO;
import com.sd.stackoverflow.model.Answer;
import com.sd.stackoverflow.repository.IAnswerRepository;
import com.sd.stackoverflow.service.customexceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final IAnswerRepository iAnswerRepository;

    public List<AnswerDTO> getAllAnswers() throws ResourceNotFoundException {
        List<AnswerDTO> answerList = iAnswerRepository.findAll().stream()
                .map(AnswerDTO::fromAnswerEntity)
                .collect(Collectors.toList());

        if (answerList.isEmpty()) {
            throw new ResourceNotFoundException("No answers found in database!");
        }

        return answerList;
    }

    public Answer getAnswer(Long id) {
        Optional<Answer> foundAnswer = iAnswerRepository.findById(id);

        if (foundAnswer.isEmpty()) {
            throw new ResourceNotFoundException("Answer with id " + id + " not found.");
        }

        return foundAnswer.get();
    }

    public Answer addAnswer(Answer givenAnswer) throws ResourceNotFoundException {
        Optional<Answer> foundAnswer = iAnswerRepository.findById(givenAnswer.getAnswerId());

        if (foundAnswer.isPresent()) {
            throw new ResourceNotFoundException("Answer" + givenAnswer.getAnswerId() + " already found. Cannot perform create operation.");
        }

        return iAnswerRepository.save(givenAnswer);
    }

    public Answer updateAnswer(Answer answer) throws ResourceNotFoundException {
        Answer initialAnswer = iAnswerRepository.findById(answer.getAnswerId()).orElse(null);

        if (initialAnswer == null) {
            throw new ResourceNotFoundException("Answer with id " + answer.getAnswerId() + " cannot be found.");
        } else {
            return iAnswerRepository.save(initialAnswer);
        }
    }

    public void deleteAnswer(Long id) throws ResourceNotFoundException {
        Optional<Answer> foundAnswer = iAnswerRepository.findById(id);

        if (foundAnswer.isEmpty()) {
            throw new ResourceNotFoundException("Given answer was not found. Delete operation could not be performed.");
        } else {
            iAnswerRepository.delete(foundAnswer.get());
        }
    }
}
