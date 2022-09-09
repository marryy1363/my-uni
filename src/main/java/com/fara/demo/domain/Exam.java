package com.fara.demo.domain;



import com.fara.demo.domain.Question.ExamQuestionsPoints;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "exam")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exam {

    public Exam(String title, String brief, Integer timeInMinutes, Date examDate) {
        this.title = title;
        this.brief = brief;
        this.timeInMinutes = timeInMinutes;
        this.examDate = examDate;
    }

    public Exam(String title, String brief, Integer timeInMinutes) {
        this.title = title;
        this.brief = brief;
        this.timeInMinutes = timeInMinutes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "brief")
    private String brief;

    @Column(name = "time_in_minutes", nullable = false)
    private Integer timeInMinutes;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "exam_date", nullable = false)
    private Date examDate = Date.valueOf(LocalDate.now());

    @OneToMany(mappedBy = "exam", orphanRemoval = true)
    private List<ExamQuestionsPoints> questionsWithPoints = new ArrayList<>();

    @Transient
    public boolean available() {
        return examDate.equals(Date.valueOf(LocalDate.now()));
    }

    @Transient
    public boolean past() {
        return examDate.before(Date.valueOf(LocalDate.now()));
    }

    @Transient
    public Period period() {
        return Period.between( LocalDate.now(),examDate.toLocalDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return Objects.equals(id, exam.id) && Objects.equals(title, exam.title) && Objects.equals(brief, exam.brief) && Objects.equals(timeInMinutes, exam.timeInMinutes) && Objects.equals(course, exam.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, brief, timeInMinutes, course);
    }
}