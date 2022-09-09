package com.fara.demo.domain.answer;


import com.fara.demo.domain.Question.ExamQuestionsPoints;
import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("EssayAnswer")
public class EssayAnswer extends BaseStudentAnswer {
    @Column(name = "essay")
    private String essay;

    @Column(name = "scored", columnDefinition = "tinyint(1) default 0")
    private Boolean scored =false;

    public Boolean getScored() {
        return scored;
    }


    public String getEssay() {
        return essay;
    }

    public void setEssay(String essay) {
        this.essay = essay;
    }

    public EssayAnswer(User student, ExamQuestionsPoints examQuestionsPoints, String essay) {
        super(student, examQuestionsPoints, 0, "EssayAnswer");
        this.essay = essay;
    }

    public EssayAnswer(User student, ExamQuestionsPoints examQuestionsPoints) {
        super(student, examQuestionsPoints, 0, "EssayAnswer");

    }


    public void setScore(Integer score) {
        this.scored = true;
        this.score = score;
    }


    @PrePersist
    public void prePersist() throws Exception {
        validateStudentRole();
    }

    @PreUpdate
    public void preUpdate() throws Exception {
        validateStudentRole();
    }

    protected void validateStudentRole() throws Exception {

        if (student != null && student.getRoles() != Roles.STUDENT) {
            throw new Exception(" students can only answer");
        }
    }

}
