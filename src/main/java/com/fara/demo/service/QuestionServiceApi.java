package com.fara.demo.service;


import com.fara.demo.domain.Question.BaseQuestion;
import com.fara.demo.domain.Question.ExamQuestionsPoints;

import java.util.List;
import java.util.Optional;

public interface QuestionServiceApi {


    Optional<BaseQuestion> findById(Long questionId);

    List<ExamQuestionsPoints> findAllQuestionsByExamId(Long id);

    ExamQuestionsPoints createNewEssayQuestion(String title, Integer points, String prompt, Long examId);

    ExamQuestionsPoints createMultiOptionQuestion(Long examId, String title, Integer points, String prompt, List<String> strings, String option);

    List<BaseQuestion> findQuery(String query, String type, Long examId);

    ExamQuestionsPoints selectQuestionFromBank(Long examId, Long questionId, Integer points);

    BaseQuestion editQuestion(Long questionId, String title, Optional<Long> answer, String prompt);
}
