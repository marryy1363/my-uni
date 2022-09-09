package com.fara.demo.domain.answer;



import com.fara.demo.domain.Exam;
import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
public class StudentTakeExam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(name = "finished", nullable = false)
    private Boolean finished = false;

    @Column(name = "starting_time", nullable = false)
    private LocalDateTime startingTime;

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        if (!this.finished) {
            // only can finish once
            this.finished = finished;
        }
    }



    public StudentTakeExam(User user, Exam exam, LocalDateTime startingTime) {
        this.user = user;
        this.exam = exam;
        this.startingTime = startingTime;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) throws Exception {
        if (user.getRoles() != Roles.STUDENT) throw new Exception(" student not found");
        this.user = user;
    }

    public Long getId() {
        return id;
    }


    public boolean isDone() {
        Integer timeInMinutes = exam.getTimeInMinutes();

        LocalDateTime endingTime = startingTime.plusMinutes((long) timeInMinutes);
        if (startingTime.isAfter(endingTime)) {
            finished = true;
            return true;
        } else
            return false;
    }


    @PrePersist
    public void prePersist() {
        isDone();
    }

    @PreUpdate
    public void preUpdate() {
        isDone();
    }
}
