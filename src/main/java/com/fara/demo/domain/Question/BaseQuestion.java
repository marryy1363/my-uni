package com.fara.demo.domain.Question;


import com.fara.demo.domain.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"title"}
        )
)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn(name="Question_type",
        discriminatorType = DiscriminatorType.STRING)

public class BaseQuestion {
    // id -------------------------------
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    protected Long id;

    //@Column(name="subtype_id", insertable = false, updatable = false)
    //https://stackoverflow.com/questions/43570875/how-to-access-discriminator-column-in-jpa


    // title ----------------------------
    @Column(name = "title", nullable = false, unique = true, length = 100)
    protected String title;
    // relation -------------------------
    @ManyToOne
    @JoinColumn(name = "course_id")
    protected Course course;

    @Column(name = "prompt" , columnDefinition = "TEXT")
    protected String prompt;

    @Column(name="Question_type", insertable = false, updatable = false)
        private String questionType;

    public BaseQuestion(String title, Course course, String prompt) {
        this.title = title;
        this.course = course;
        this.prompt = prompt;
    }
}