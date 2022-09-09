package com.fara.demo.service;

import com.fara.demo.domain.Exam;
import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.BaseStudentAnswer;

import java.util.List;

public interface TutorServiceApi {
    List<User> findStudentsTookExamByExamId(Exam exam);

    List<BaseStudentAnswer> getEssayQuestionsWithAnswer(Long studentId, Long examId) throws Exception;


    int getTotalScore(List<BaseStudentAnswer> answers);

    void setEssayScore(Long questionAnswerId, Integer score) throws Exception;
}
