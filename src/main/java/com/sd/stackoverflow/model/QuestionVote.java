package com.sd.stackoverflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question_votes")
public class QuestionVote {

    @EmbeddedId
    private QuestionVoteKey questionVoteKey;

    @Column(name = "vote")
    private Boolean vote;
}
