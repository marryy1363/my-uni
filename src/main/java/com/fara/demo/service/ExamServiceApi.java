package com.fara.demo.service;


import com.fara.demo.domain.Exam;

public interface ExamServiceApi {
    Exam editExam(Long id, String title, String brief, Integer timeInMinutes, String examDate) throws Exception;

    Exam createNewExamForCourse(Long courseId, String title, String brief, Integer timeInMinutes, String examDate) throws Exception;
}
