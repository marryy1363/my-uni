package com.fara.demo.domain.answer;



import com.fara.demo.domain.Question.ExamQuestionsPoints;
import com.fara.demo.domain.Question.MultiOptionQuestion;
import com.fara.demo.domain.Question.Options;
import com.fara.demo.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("SelectOptionAnswer")
public class SelectOptionAnswer extends BaseStudentAnswer {
    @OneToOne(orphanRemoval = false)
    @JoinColumn(name = "selected_option_id")
    private Options selectedOption;


    @PrePersist
    public void prePersist() throws Exception {
        validateStudentRole();
        validateOption();
        calculateScore();
    }

    public void setSelectedOption(Options selectedOption) throws Exception {
        validateStudentRole();
        validateOption();
        calculateScore();
        this.selectedOption = selectedOption;
    }

    @PreUpdate
    public void preUpdate() throws Exception {
        validateStudentRole();
        validateOption();
        calculateScore();
    }



    public SelectOptionAnswer(User student, ExamQuestionsPoints examQuestionsPoints, Options selectedOption) throws Exception {
        super( student, examQuestionsPoints, 0, "SelectOptionAnswer");
        this.selectedOption = selectedOption;
        validateStudentRole();
        validateOption();
        calculateScore();
    }

    public SelectOptionAnswer( User student, ExamQuestionsPoints examQuestionsPoints) throws Exception {
        super( student, examQuestionsPoints, 0, "SelectOptionAnswer");

        validateStudentRole();
        validateOption();
        calculateScore();
    }

    private void validateOption() throws Exception {

        if (examQuestionsPoints.getBaseQuestion() == null || !examQuestionsPoints.getBaseQuestion().getQuestionType().equals("MultiOptionQuestion"))
            throw new Exception(" question not found ");
        try {
            MultiOptionQuestion baseQuestion = (MultiOptionQuestion) examQuestionsPoints.getBaseQuestion();
        } catch (Exception e) {
            System.err.println(" question type not multi option ");
            e.printStackTrace();
            throw new Exception(" question type not multi option ");
        }
        if (selectedOption!=null &&  !((MultiOptionQuestion) examQuestionsPoints.getBaseQuestion()).getOpts().contains(selectedOption)) {
            System.err.println("option not found");
            throw new Exception("option not found");
        }
    }

    private void calculateScore() {
        if (selectedOption!=null && examQuestionsPoints.getBaseQuestion() != null && ((MultiOptionQuestion) examQuestionsPoints.getBaseQuestion()).getAnswer().equals(selectedOption)) {
            score = examQuestionsPoints.getPoint();
        } else score = 0;
    }
}
