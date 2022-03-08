package com.sd.stackoverflow.dto;

import com.sd.stackoverflow.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

    private Long answerId;
    private String answerText;
    private LocalDateTime answerTextCreated;
    private Integer posVotes;
    private Integer negVotes;

    public static AnswerDTO fromAnswerEntity(Answer answer) {
        return AnswerDTO.builder()
                .answerId(answer.getAnswerId())
                .answerText(answer.getAnswerText())
                .answerTextCreated(answer.getAnswerTextCreated())
                .posVotes(answer.getPosVotes())
                .negVotes(answer.getNegVotes())
                .build();
    }
}
