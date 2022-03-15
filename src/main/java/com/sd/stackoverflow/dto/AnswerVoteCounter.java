package com.sd.stackoverflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerVoteCounter {

    private Integer posVotes;
    private Integer negVotes;
    private Boolean currentUserVote;
}
