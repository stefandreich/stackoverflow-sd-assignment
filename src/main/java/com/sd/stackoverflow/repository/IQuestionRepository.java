package com.sd.stackoverflow.repository;

import com.sd.stackoverflow.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Set;

@Repository
public interface IQuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "select q from Question q " +
            "join q.tags tag " +
            "where q.title like CONCAT('%',:title,'%') " +
            "or tag.text = :title")
    Set<Question> findAllByTitleContainsAndTags(@Param("title") String title);

    @Query(value = "select (select count(*) from question_votes likes where likes.vote = 1 and likes.question_id = a.question_id) as posVotes, " +
            "       (select count(*) from question_votes dislikes where dislikes.vote = 0 and dislikes.question_id = a.question_id) as negVotes, " +
            "       current_user_vote.vote as currentUserVote " +
            "       from stackoverflow.question a " +
            "       left join question_votes current_user_vote on current_user_vote.question_id = a.question_id and current_user_vote.user_id = :userId " +
            "       where a.question_id = :id ", nativeQuery = true)
    Tuple getQuestionVoteForAnswerID(@Param("id") Long id, @Param("userId") Long userId);
}
