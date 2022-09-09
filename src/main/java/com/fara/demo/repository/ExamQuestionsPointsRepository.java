package com.fara.demo.repository;

import com.fara.demo.domain.Exam;
import com.fara.demo.domain.Question.BaseQuestion;
import com.fara.demo.domain.Question.ExamQuestionsPoints;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamQuestionsPointsRepository extends JpaRepository<ExamQuestionsPoints, Long> {
    List<ExamQuestionsPoints> findByExamId(Long examId);

    Boolean existsByBaseQuestionAndExam(BaseQuestion baseQuestion, Exam exam);

    List<ExamQuestionsPoints> findByExam(Exam exam);
}
