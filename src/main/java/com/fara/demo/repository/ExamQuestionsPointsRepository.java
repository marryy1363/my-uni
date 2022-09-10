package com.fara.demo.repository;

import com.fara.demo.domain.Exam;
import com.fara.demo.domain.Question.BaseQuestion;
import com.fara.demo.domain.Question.ExamQuestionsPoints;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ExamQuestionsPointsRepository extends JpaRepository<ExamQuestionsPoints, Long> {


    @Override
    Optional<ExamQuestionsPoints> findById(Long aLong);

    List<ExamQuestionsPoints> findByExamId(Long examId);

    Boolean existsByBaseQuestionAndExam(BaseQuestion baseQuestion, Exam exam);

    List<ExamQuestionsPoints> findByExam(Exam exam);
}
