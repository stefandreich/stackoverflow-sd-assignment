package com.sd.stackoverflow.service;

import com.sd.stackoverflow.dto.QuestionDTO;
import com.sd.stackoverflow.dto.QuestionVoteCounter;
import com.sd.stackoverflow.mapper.QuestionMapper;
import com.sd.stackoverflow.model.*;
import com.sd.stackoverflow.repository.IQuestionRepository;
import com.sd.stackoverflow.repository.IQuestionVoteRepository;
import com.sd.stackoverflow.repository.ITagRepository;
import com.sd.stackoverflow.repository.IUserRepository;
import com.sd.stackoverflow.service.customexceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final IQuestionRepository iQuestionRepository;
    private final ITagRepository iTagRepository;
    private final IQuestionVoteRepository iQuestionVoteRepository;
    private final IUserRepository iUserRepository;

    private final QuestionMapper questionMapper;

    public List<QuestionDTO> getAllQuestions(Long userId) throws ResourceNotFoundException {
        return iQuestionRepository.findAll().stream()
                .map(question -> questionMapper.toDTO(question, userId))
                .collect(Collectors.toList());
    }

    public QuestionDTO getQuestion(Long id, Long userId) {
        Optional<Question> foundQuestion = iQuestionRepository.findById(id);

        if (foundQuestion.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        return questionMapper.toDTO(foundQuestion.get(), userId);
    }

    @Transactional
    public QuestionDTO addQuestion(QuestionDTO questionDTO, Long userId) throws ResourceNotFoundException {
        if (questionDTO.getQuestionId() != null) {
            throw new ResourceNotFoundException("Question" + questionDTO.getQuestionId() + " already found. Cannot perform create operation.");
        }

        User currentUser = iUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found."));

        Question question = questionMapper.toEntity(questionDTO);

        question.setQuestionDateCreated(LocalDateTime.now());

        question.setUser(currentUser);

        setSavedTagsOnQuestion(question);

        Question getQuestionSaved = iQuestionRepository.save(question);

        return questionMapper.toDTO(getQuestionSaved, null);
    }

    private void setSavedTagsOnQuestion(Question question) {
        Set<Tag> savedTags = question.getTags().stream()
                .map(tag -> {
                    if (tag.getTagId() != null) {
                        return tag;
                    }

                    return iTagRepository.save(tag);
                })
                .collect(Collectors.toSet());

        question.setTags(savedTags);
    }

    public List<QuestionDTO> getQuestionsByTitleAndTags(String title, Long userId) throws ResourceNotFoundException {
        Set<Question> foundQuestions = iQuestionRepository.findAllByTitleContainsAndTags(title);

        return foundQuestions.stream().map(question -> questionMapper.toDTO(question, userId)).collect(Collectors.toList());
    }

    public List<Tag> getAllTags() {
        return iTagRepository.findAll();
    }

    public QuestionDTO updateQuestion(QuestionDTO questionDTO, Long userId) throws ResourceNotFoundException {
        if (questionDTO.getUser() == null || questionDTO.getUser().getUserId() == null) {
            throw new ResourceNotFoundException("Question has no user.");
        }

        if (!Objects.equals(questionDTO.getUser().getUserId(), userId)) {
            throw new ResourceNotFoundException("Question with id " + questionDTO.getQuestionId() + " cannot be found.");
        }

        Question question = questionMapper.toEntity(questionDTO);

        Question getQuestionSaved = iQuestionRepository.save(question);

        return questionMapper.toDTO(getQuestionSaved, userId);
    }

    public void deleteQuestion(Long id, Long userId) throws ResourceNotFoundException {
        Optional<Question> foundQuestion = iQuestionRepository.findById(id);

        if (foundQuestion.isEmpty()) {
            throw new ResourceNotFoundException("Given question was not found. Delete operation could not be performed");
        } else {
            if (foundQuestion.get().getUser() == null || foundQuestion.get().getUser().getUserId() == null) {
                throw new ResourceNotFoundException("Question has no user.");
            }

            if (!Objects.equals(foundQuestion.get().getUser().getUserId(), userId)) {
                throw new ResourceNotFoundException("You can't delete another user question.");
            }
            iQuestionRepository.delete(foundQuestion.get());
        }
    }

    @Transactional
    public QuestionVoteCounter voteOnQuestion(Long id, Long userId, Boolean vote) throws ResourceNotFoundException {
        Optional<Question> foundQuestion = iQuestionRepository.findById(id);

        if (foundQuestion.isEmpty()) {
            throw new ResourceNotFoundException("Given question was not found. You cannot vote.");
        } else {
            if (Objects.equals(foundQuestion.get().getUser().getUserId(), userId)) {
                throw new ResourceNotFoundException("You can't vote on your own question.");
            }

            Optional<QuestionVote> foundQuestionVote = iQuestionVoteRepository.findByQuestionVoteKey_QuestionIdAndAndQuestionVoteKey_UserId(id, userId);

            if (foundQuestionVote.isPresent()) {
                if (foundQuestionVote.get().getVote() == vote) {
                    iQuestionVoteRepository.delete(foundQuestionVote.get());
                } else {
                    foundQuestionVote.get().setVote(vote);
                    iQuestionVoteRepository.save(foundQuestionVote.get());
                }
            } else {
                QuestionVote newQuestionVote = new QuestionVote(new QuestionVoteKey(id, userId), vote);
                iQuestionVoteRepository.save(newQuestionVote);
            }

            Tuple tuple = iQuestionRepository.getQuestionVoteForAnswerID(foundQuestion.get().getQuestionId(), userId);
            return (new QuestionVoteCounter(tuple.get(0, BigInteger.class).intValue(), tuple.get(1, BigInteger.class).intValue(), tuple.get(2, Boolean.class)));
        }
    }


}
