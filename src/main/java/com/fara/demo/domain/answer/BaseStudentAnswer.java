package com.fara.demo.domain.answer;


import com.fara.demo.domain.Question.ExamQuestionsPoints;
import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity

@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "answer_type", discriminatorType = DiscriminatorType.STRING)
public class  BaseStudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    protected User student;

    @ManyToOne
    @JoinColumn(name = "exam_questions_points_id")
    protected ExamQuestionsPoints examQuestionsPoints;

    @Column(name = "score")
    protected Integer score=0;

    @Column(name = "answer_type", insertable = false, updatable = false)
    private
    String answerType;

    public BaseStudentAnswer(User student, ExamQuestionsPoints examQuestionsPoints, Integer score, String answerType) {
        this.student = student;
        this.examQuestionsPoints = examQuestionsPoints;
        this.score = score;
        this.answerType = answerType;
    }



    protected void validateStudentRole() throws Exception {

        if (student != null && student.getRoles() != Roles.STUDENT) {
            throw new Exception(" students can only answer");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BaseStudentAnswer that = (BaseStudentAnswer) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
