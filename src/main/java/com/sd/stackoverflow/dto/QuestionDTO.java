package com.sd.stackoverflow.dto;

import com.sd.stackoverflow.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Long questionId;
    private String title;
    private String questionText;
    private LocalDateTime questionDateCreated;
    private String tag;
    private Integer posVotes;
    private Integer negVotes;

    public static QuestionDTO fromQuestionEntity(Question question) {
        return QuestionDTO.builder()
                .questionId(question.getQuestionId())
                .title(question.getTitle())
                .questionText(question.getQuestionText())
                .questionDateCreated(question.getQuestionDateCreated())
                .posVotes(question.getPosVotes())
                .negVotes(question.getNegVotes())
                .build();
    }
}
