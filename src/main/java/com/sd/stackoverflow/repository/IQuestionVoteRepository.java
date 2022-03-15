package com.sd.stackoverflow.repository;

import com.sd.stackoverflow.model.QuestionVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IQuestionVoteRepository extends JpaRepository<QuestionVote, Long> {
    Optional<QuestionVote> findByQuestionVoteKey_QuestionIdAndAndQuestionVoteKey_UserId(Long questionId, Long userId);
}
