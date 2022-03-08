package com.sd.stackoverflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Column(name = "title")
    private String title;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "question_date_created")
    private LocalDateTime questionDateCreated;

    @Column(name = "pos_votes")
    private Integer posVotes;

    @Column(name = "neg_votes")
    private Integer negVotes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "questions_tags",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
}
