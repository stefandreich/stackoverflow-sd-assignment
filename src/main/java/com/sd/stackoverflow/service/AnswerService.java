package com.sd.stackoverflow.service;

import com.sd.stackoverflow.dto.AnswerDTO;
import com.sd.stackoverflow.dto.AnswerVoteCounter;
import com.sd.stackoverflow.mapper.AnswerMapper;
import com.sd.stackoverflow.model.Answer;
import com.sd.stackoverflow.model.AnswerVote;
import com.sd.stackoverflow.model.AnswerVoteKey;
import com.sd.stackoverflow.model.User;
import com.sd.stackoverflow.repository.IAnswerRepository;
import com.sd.stackoverflow.repository.IAnswerVoteRepository;
import com.sd.stackoverflow.repository.IUserRepository;
import com.sd.stackoverflow.service.customexceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final IAnswerRepository iAnswerRepository;
    private final IAnswerVoteRepository iAnswerVoteRepository;
    private final IUserRepository iUserRepository;

    private final AnswerMapper answerMapper;

    public List<AnswerDTO> getAllAnswers(Long userId) throws ResourceNotFoundException {
        return iAnswerRepository.findAll().stream()
                .map(answer -> answerMapper.toDTO(answer, userId))
                .collect(Collectors.toList());
    }

    public AnswerDTO getAnswer(Long id, Long userId) {
        Optional<Answer> foundAnswer = iAnswerRepository.findById(id);

        if (foundAnswer.isEmpty()) {
            throw new ResourceNotFoundException("Answer with id " + id + " not found.");
        }

        return answerMapper.toDTO(foundAnswer.get(), userId);
    }

    public AnswerDTO addAnswer(AnswerDTO givenAnswer, Long userId) throws ResourceNotFoundException {
        if (givenAnswer.getAnswerId() != null) {
            throw new ResourceNotFoundException("Answer" + givenAnswer.getAnswerId() + " already found. Cannot perform create operation.");
        }

        User currentUser = iUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found."));

        Answer answer = answerMapper.toEntity(givenAnswer);

        answer.setUser(currentUser);

        answer.setAnswerTextCreated(LocalDateTime.now());

        return answerMapper.toDTO(iAnswerRepository.save(answer), null);
    }

    // nu uita sa trimiti tot tot tot in body in Postman (question + user)
    public AnswerDTO updateAnswer(AnswerDTO answer, Long userId) throws ResourceNotFoundException {
        if (answer.getUser() == null || answer.getUser().getUserId() == null) {
            throw new ResourceNotFoundException("Answer has no user.");
        }

        if (!Objects.equals(answer.getUser().getUserId(), userId)) {
            throw new ResourceNotFoundException("You can't update another user answer.");
        }

        return answerMapper.toDTO(iAnswerRepository.save(answerMapper.toEntity(answer)), userId);
    }

    public void deleteAnswer(Long id, Long userId) throws ResourceNotFoundException {
        Optional<Answer> foundAnswer = iAnswerRepository.findById(id);

        if (foundAnswer.isEmpty()) {
            throw new ResourceNotFoundException("Given answer was not found. Delete operation could not be performed.");
        } else {
            if (foundAnswer.get().getUser() == null || foundAnswer.get().getUser().getUserId() == null) {
                throw new ResourceNotFoundException("Answer has no user.");
            }

            if (!Objects.equals(foundAnswer.get().getUser().getUserId(), userId)) {
                throw new ResourceNotFoundException("You can't delete another user answer.");
            }
            iAnswerRepository.delete(foundAnswer.get());
        }
    }

    public AnswerVoteCounter voteOnAnswer(Long id, Long userId, Boolean vote) throws ResourceNotFoundException {
        Optional<Answer> foundAnswer = iAnswerRepository.findById(id);

        if (foundAnswer.isEmpty()) {
            throw new ResourceNotFoundException("Given answer was not found. You cannot vote.");
        } else {
            if (Objects.equals(foundAnswer.get().getUser().getUserId(), userId)) {
                throw new ResourceNotFoundException("You can't vote on your own answer.");
            }

            Optional<AnswerVote> foundAnswerVote = iAnswerVoteRepository.findByAnswerVoteKey_AnswerIdAndAndAnswerVoteKey_UserId(id, userId);

            if (foundAnswerVote.isPresent()) {
                if (foundAnswerVote.get().getVote() == vote) {
                    iAnswerVoteRepository.delete(foundAnswerVote.get());
                } else {
                    foundAnswerVote.get().setVote(vote);
                    iAnswerVoteRepository.save(foundAnswerVote.get());
                }
            } else {
                AnswerVote newAnswerVote = new AnswerVote(new AnswerVoteKey(id, userId), vote);
                iAnswerVoteRepository.save(newAnswerVote);
            }

            Tuple tuple = iAnswerRepository.getAnswerVoteForAnswerID(foundAnswer.get().getAnswerId(), userId);
            return (new AnswerVoteCounter(tuple.get(0, BigInteger.class).intValue(), tuple.get(1, BigInteger.class).intValue(), tuple.get(2, Boolean.class)));
        }
    }



}
