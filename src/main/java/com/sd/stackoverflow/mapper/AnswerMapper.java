package com.sd.stackoverflow.mapper;

import com.sd.stackoverflow.dto.AnswerDTO;
import com.sd.stackoverflow.dto.QuestionDTO;
import com.sd.stackoverflow.model.Answer;
import com.sd.stackoverflow.model.Question;
import com.sd.stackoverflow.repository.IAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerMapper {

    private final UserMapper userMapper;

    private final IAnswerRepository iAnswerRepository;

    public AnswerDTO toDTO(Answer answer, Long userId) {
        if (answer == null) {
            return null;
        }
        Question question = answer.getQuestion();

        QuestionDTO questionDTO = QuestionDTO.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .questionText(question.getQuestionText())
                .questionDateCreated(question.getQuestionDateCreated())
                .user(userMapper.toDTO(question.getUser()))
                .tags(question.getTags())
                .build();

        AnswerDTO answerDTO = AnswerDTO.builder()
                .answerId(answer.getAnswerId())
                .answerText(answer.getAnswerText())
                .answerTextCreated(answer.getAnswerTextCreated())
                .user(userMapper.toDTO(answer.getUser()))
                .question(questionDTO)
                .build();


        Tuple tuple = iAnswerRepository.getAnswerVoteForAnswerID(answer.getAnswerId(), userId);

        answerDTO.setPosVotes(tuple.get(0, BigInteger.class).intValue());
        answerDTO.setNegVotes(tuple.get(1, BigInteger.class).intValue());
        answerDTO.setCurrentUserVote(tuple.get(2, Boolean.class));

        return answerDTO;
    }

    public List<AnswerDTO> toDTO(List<Answer> answers, Long userId) {
        return answers.stream().map(a -> this.toDTO(a, userId)).collect(Collectors.toList());
    }

    public Answer toEntity(AnswerDTO answerDTO) {
        if (answerDTO == null) {
            return null;
        }
        QuestionDTO questionDTO = answerDTO.getQuestion();

        Question question = Question.builder()
                .questionId(questionDTO.getQuestionId())
                .title(questionDTO.getTitle())
                .questionText(questionDTO.getQuestionText())
                .questionDateCreated(questionDTO.getQuestionDateCreated())
                .user(userMapper.toEntity(questionDTO.getUser()))
                .tags(questionDTO.getTags())
                .build();

        return Answer.builder()
                .answerId(answerDTO.getAnswerId())
                .answerText(answerDTO.getAnswerText())
                .answerTextCreated(answerDTO.getAnswerTextCreated())
                .question(question)
                .user(userMapper.toEntity(answerDTO.getUser()))
                .build();
    }

    public List<Answer> toEntity(List<AnswerDTO> answerDTOs) {
        return answerDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
