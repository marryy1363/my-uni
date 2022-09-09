package com.fara.demo.domain.Question;



import com.fara.demo.domain.Exam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExamQuestionsPoints {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(optional = false)
    @JoinColumn(name = "base_question_id", nullable = false)
    private BaseQuestion baseQuestion;

    @Column(name = "point", nullable = false)
    private Integer point;

    public ExamQuestionsPoints(Exam exam, BaseQuestion baseQuestion, Integer point) {
        this.exam = exam;
        this.baseQuestion = baseQuestion;
        this.point = point;
    }


    private void validate() {
        if (!Objects.equals(baseQuestion.course.getId(), exam.getCourse().getId())) {
            throw new IllegalStateException(" exam and question are from different courses");
        }
    }

    @PrePersist
    public void prePersist() {
        validate();
    }

    @PreUpdate
    public void preUpdate() {
        validate();
    }
}
