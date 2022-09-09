package com.fara.demo.service;

import com.fara.demo.domain.Course;
import com.fara.demo.domain.Exam;
import com.fara.demo.domain.Question.ExamQuestionsPoints;
import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.BaseStudentAnswer;

import java.time.LocalDateTime;
import java.util.List;

public interface StudentServiceApi {
    List<Course> getStudentCourses(User student);

    List<Exam> getCourseExams(Long courseId);

    List<ExamQuestionsPoints> getExamALlQuestions(Long examId) throws Exception;

    List<BaseStudentAnswer> getStudentExamQuestion(User user, Long examId) throws Exception;



    int getTimer(User user, Long examId) throws Exception;

    LocalDateTime getFinishTime(User user, Long examId) throws Exception;

    void answerQuestion(Long studentAnswerId, Long optionId, String essay) throws Exception;

    void finishExam(Long examId, Long studentId) throws Exception;

}
