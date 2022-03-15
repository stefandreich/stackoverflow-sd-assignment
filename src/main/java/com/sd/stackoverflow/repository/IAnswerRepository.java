package com.sd.stackoverflow.repository;

import com.sd.stackoverflow.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;

@Repository
public interface IAnswerRepository extends JpaRepository<Answer, Long> {

    @Query(value = "select (select count(*) from answer_votes likes where likes.vote = 1 and likes.answer_id = a.answer_id) as posVotes, " +
            "       (select count(*) from answer_votes dislikes where dislikes.vote = 0 and dislikes.answer_id = a.answer_id) as negVotes, " +
            "       current_user_vote.vote as currentUserVote " +
            "       from stackoverflow.answer a " +
            "       left join answer_votes current_user_vote on current_user_vote.answer_id = a.answer_id and current_user_vote.user_id = :userId " +
            "       where a.answer_id = :id ", nativeQuery = true)
    Tuple getAnswerVoteForAnswerID(@Param("id") Long id, @Param("userId") Long userId);
}
