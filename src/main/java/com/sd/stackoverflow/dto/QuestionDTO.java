package com.sd.stackoverflow.dto;

import com.sd.stackoverflow.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    private Long questionId;
    private String title;
    private String questionText;
    private LocalDateTime questionDateCreated;
    private UserDTO user;
    private Integer posVotes;
    private Integer negVotes;
    private Boolean currentUserVote;
    private List<AnswerDTO> answers = new ArrayList<>();
    private Set<Tag> tags;
}
