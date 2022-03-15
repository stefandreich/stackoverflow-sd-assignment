package com.sd.stackoverflow.mapper;

import com.sd.stackoverflow.dto.AnswerDTO;
import com.sd.stackoverflow.dto.QuestionDTO;
import com.sd.stackoverflow.model.Answer;
import com.sd.stackoverflow.model.Question;
import com.sd.stackoverflow.repository.IAnswerRepository;
import com.sd.stackoverflow.repository.IQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionMapper {

    private final UserMapper userMapper;

    private final IQuestionRepository iQuestionRepository;
    private final IAnswerRepository iAnswerRepository;

    public QuestionDTO toDTO(Question question, Long userId) {
        List<AnswerDTO> answerDTOList = new ArrayList<>();

        question.getAnswers().forEach(answer -> {
            AnswerDTO answerDTO = AnswerDTO.builder()
                    .answerId(answer.getAnswerId())
                    .answerText(answer.getAnswerText())
                    .answerTextCreated(answer.getAnswerTextCreated())
                    .user(userMapper.toDTO(answer.getUser()))
                    .build();

            Tuple tuple = iAnswerRepository.getAnswerVoteForAnswerID(answer.getAnswerId(), userId);

            answerDTO.setPosVotes(tuple.get(0, BigInteger.class).intValue());
            answerDTO.setNegVotes(tuple.get(1, BigInteger.class).intValue());
            answerDTO.setCurrentUserVote(tuple.get(2, Boolean.class));

            answerDTOList.add(answerDTO);
        });

        QuestionDTO questionDTO = QuestionDTO.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .questionText(question.getQuestionText())
                .questionDateCreated(question.getQuestionDateCreated())
                .user(userMapper.toDTO(question.getUser()))
                .answers(answerDTOList.stream().sorted(Comparator.comparingInt(AnswerDTO::getPosVotes).reversed()).collect(Collectors.toList()))
                .tags(question.getTags())
                .build();

        Tuple tuple = iQuestionRepository.getQuestionVoteForAnswerID(question.getQuestionId(), userId);

        questionDTO.setPosVotes(tuple.get(0, BigInteger.class).intValue());
        questionDTO.setNegVotes(tuple.get(1, BigInteger.class).intValue());
        questionDTO.setCurrentUserVote(tuple.get(2, Boolean.class));

        return questionDTO;
    }

    public Question toEntity(QuestionDTO questionDTO) {
        List<Answer> answers = new ArrayList<>();

        questionDTO.getAnswers().forEach(answerDTO -> {
            Answer answer = Answer.builder()
                    .answerId(answerDTO.getAnswerId())
                    .answerText(answerDTO.getAnswerText())
                    .answerTextCreated(answerDTO.getAnswerTextCreated())
                    .user(userMapper.toEntity(answerDTO.getUser()))
                    .build();

            answers.add(answer);
        });


        return Question.builder()
                .questionId(questionDTO.getQuestionId())
                .title(questionDTO.getTitle())
                .questionText(questionDTO.getQuestionText())
                .questionDateCreated(questionDTO.getQuestionDateCreated())
                .user(userMapper.toEntity(questionDTO.getUser()))
                .answers(answers)
                .tags(questionDTO.getTags())
                .build();
    }
}
