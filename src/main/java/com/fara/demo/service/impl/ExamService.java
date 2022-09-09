package com.fara.demo.service.impl;


import com.fara.demo.domain.Course;
import com.fara.demo.domain.Exam;
import com.fara.demo.repository.CourseRepository;
import com.fara.demo.repository.ExamRepository;
import com.fara.demo.service.ExamServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static com.fara.demo.Utils.DateUtils.dateOf;


@Service
public class ExamService implements ExamServiceApi {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ExamService(ExamRepository examRepository, CourseRepository courseRepository) {
        this.examRepository = examRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public Exam editExam(Long id, String title, String brief, Integer timeInMinutes, String examDate) throws Exception {
        Optional<Exam> byId = examRepository.findById(id);
        if (byId.isEmpty()) throw new IllegalStateException("exam not found");
        Exam exam = byId.get();
        if (title.length() < 2) throw new Exception("short title");
        if (brief.length() < 2) throw new Exception("short brief");
        if (timeInMinutes < 0) throw new Exception("only positive values for time ");
        Date date = dateOf(examDate);
        try {
            if (date.before(Date.valueOf(LocalDate.now())))
                throw new Exception("date of exam cant be in past");
        } catch (Exception e) {
            throw new Exception("date of exam cant be in past");
        }
        exam.setTitle(title);
        exam.setBrief(brief);
        exam.setTimeInMinutes(timeInMinutes);
        exam.setExamDate(date);
        return examRepository.save(exam);
    }

    @Override
    public Exam createNewExamForCourse(Long courseId, String title, String brief, Integer timeInMinutes, String examDate) throws Exception {

        Optional<Course> byId = courseRepository.findById(courseId);
        if (byId.isEmpty()) throw new Exception("course not found");
        Course course = byId.get();
        Exam exam = new Exam();
        if (title.length() < 2) throw new Exception("short title");
        if (brief.length() < 2) throw new Exception("short brief");
        if (timeInMinutes < 0) throw new Exception("only positive values for time ");

        Date date = dateOf(examDate);
        try {
            if (date.before(Date.valueOf(LocalDate.now())))
                throw new Exception("date of exam cant be in past");
        } catch (Exception e) {
            throw new Exception("date of exam cant be in past");
        }
        exam.setTitle(title);
        exam.setBrief(brief);
        exam.setTimeInMinutes(timeInMinutes);
        exam.setExamDate(date);
        exam.setCourse(course);
        course.getExams().add(exam);
        courseRepository.save(course);
        return examRepository.save(exam);

    }
}
