package com.sd.stackoverflow.repository;

import com.sd.stackoverflow.model.AnswerVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IAnswerVoteRepository extends JpaRepository<AnswerVote, Long> {
    Optional<AnswerVote> findByAnswerVoteKey_AnswerIdAndAndAnswerVoteKey_UserId(Long answerId, Long userId);
}
