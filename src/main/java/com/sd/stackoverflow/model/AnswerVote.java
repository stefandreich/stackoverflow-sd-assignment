package com.sd.stackoverflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer_votes")
public class AnswerVote {

    @EmbeddedId
    private AnswerVoteKey answerVoteKey;

    @Column(name = "vote")
    private Boolean vote;
}
