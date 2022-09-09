package com.fara.demo.service.impl;
;
import com.fara.demo.domain.Course;
import com.fara.demo.domain.Exam;
import com.fara.demo.domain.Question.ExamQuestionsPoints;
import com.fara.demo.domain.Question.Options;
import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.BaseStudentAnswer;
import com.fara.demo.domain.answer.EssayAnswer;
import com.fara.demo.domain.answer.SelectOptionAnswer;
import com.fara.demo.domain.answer.StudentTakeExam;
import com.fara.demo.repository.*;
import com.fara.demo.service.StudentServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements StudentServiceApi {


    private final CourseRepository courseRepository;
    private final ExamQuestionsPointsRepository examQuestionsPointsRepository;

    @Autowired
    public StudentService(CourseRepository courseRepository, ExamQuestionsPointsRepository examQuestionsPointsRepository, SelectOptionAnswerRepository selectOptionAnswerRepository, EssayAnswerRepository essayAnswerRepository, BaseStudentAnswerRepository baseStudentAnswerRepository, StudentTakeExamRepository studentTakeExamRepository, ExamRepository examRepository, OptionsRepository optionsRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.examQuestionsPointsRepository = examQuestionsPointsRepository;
        this.selectOptionAnswerRepository = selectOptionAnswerRepository;
        this.essayAnswerRepository = essayAnswerRepository;
        this.baseStudentAnswerRepository = baseStudentAnswerRepository;
        this.studentTakeExamRepository = studentTakeExamRepository;
        this.examRepository = examRepository;
        this.optionsRepository = optionsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Course> getStudentCourses(User student) {
        return courseRepository.findByStudentsIsContaining(student);
    }

    @Override
    public List<Exam> getCourseExams(Long courseId) {
        Optional<Course> byId = courseRepository.findById(courseId);
        if (byId.isEmpty()) throw new IllegalStateException(" could not find course");
        Course course = byId.get();
        List<Exam> exams = course.getExams();
        if (exams == null) throw new IllegalStateException("no exams found");
        if (exams.isEmpty()) throw new IllegalStateException("no exams found");
        return exams;
    }

    @Override
    public List<ExamQuestionsPoints> getExamALlQuestions(Long examId) throws Exception {

        List<ExamQuestionsPoints> questionsPoints = examQuestionsPointsRepository.findByExamId(examId);
        if (questionsPoints == null) throw new Exception(" questions not found");
        if (questionsPoints.isEmpty()) throw new Exception(" questions not found");
        if (questionsPoints.get(0).getExam().past()) throw new Exception(" exam date is past");
        if (!questionsPoints.get(0).getExam().available()) throw new Exception(" exam is not available ");
        return questionsPoints;

    }

    private final SelectOptionAnswerRepository selectOptionAnswerRepository;
    private final EssayAnswerRepository essayAnswerRepository;
    private final BaseStudentAnswerRepository baseStudentAnswerRepository;

    @Override
    public List<BaseStudentAnswer> getStudentExamQuestion(User user, Long examId) throws Exception {

            List<BaseStudentAnswer> byStudent = baseStudentAnswerRepository.findByStudentAndExam(user, examId);
        if (byStudent == null || byStudent.isEmpty()) {
            makeQuestions(user, examId);
            byStudent = baseStudentAnswerRepository.findByStudentAndExam(user, examId);
        }
        if (byStudent == null || byStudent.isEmpty()) throw new Exception(" questions not found");
        return byStudent;
    }

    private void makeQuestions(User user, Long examId) throws Exception {
        List<ExamQuestionsPoints> aLlQuestions = getExamALlQuestions(examId);
        if (user == null) throw new Exception("no user found");
        if (user.getRoles() != Roles.STUDENT) throw new Exception(" must be student ");

        for (ExamQuestionsPoints examQuestion : aLlQuestions) {
            if (examQuestion.getBaseQuestion().getQuestionType().equals("MultiOptionQuestion")) {
                SelectOptionAnswer selectOptionAnswer = new SelectOptionAnswer(user, examQuestion);
                selectOptionAnswerRepository.save(selectOptionAnswer);
            } else {
                EssayAnswer essayAnswer = new EssayAnswer(user, examQuestion);
                essayAnswerRepository.save(essayAnswer);
            }

        }
    }

    private final StudentTakeExamRepository studentTakeExamRepository;
    private final ExamRepository examRepository;

    private void startExamForStudent(User user, Long examId) throws Exception {
        Optional<Exam> byId = examRepository.findById(examId);
        if (byId.isEmpty()) throw new Exception("exam not found");
        Exam exam = byId.get();
        StudentTakeExam studentTakeExam = new StudentTakeExam(user, exam, LocalDateTime.now());
        studentTakeExamRepository.save(studentTakeExam);
    }

    @Override
    public int getTimer(User user, Long examId) throws Exception {
        Optional<Exam> byId = examRepository.findById(examId);
        if (byId.isEmpty()) throw new Exception("exam not found");
        Exam exam = byId.get();
        StudentTakeExam byExamAndUser = studentTakeExamRepository.findByExamAndUser(exam, user);
        if (byExamAndUser == null) {
            startExamForStudent(user, examId);
            byExamAndUser = studentTakeExamRepository.findByExamAndUser(exam, user);
        }
        if (byExamAndUser.getFinished()) throw new Exception("time is up");
        LocalDateTime startingTime = byExamAndUser.getStartingTime();
        LocalDateTime now = LocalDateTime.now();
        Duration between = Duration.between(startingTime, now);
        long st = between.toMinutes();
        int spentTime = (int) st;
        Integer totalTime = exam.getTimeInMinutes();
        int counter = totalTime - (spentTime);
        if (counter < 0) {
            byExamAndUser.isDone();
            throw new Exception("time is up");
        }

        return counter;
    }

    @Override
    public LocalDateTime getFinishTime(User user, Long examId) throws Exception {
        Optional<Exam> byId = examRepository.findById(examId);
        if (byId.isEmpty()) throw new Exception("exam not found");
        Exam exam = byId.get();
        StudentTakeExam byExamAndUser = studentTakeExamRepository.findByExamAndUser(exam, user);
        if (byExamAndUser == null) {
            startExamForStudent(user, examId);
            byExamAndUser = studentTakeExamRepository.findByExamAndUser(exam, user);
        }
        if (byExamAndUser.getFinished()) throw new Exception("exam finished");
        LocalDateTime startingTime = byExamAndUser.getStartingTime();

        Integer totalTime = exam.getTimeInMinutes();

        LocalDateTime endingTime = startingTime.plusMinutes(totalTime);
        if (endingTime.isBefore(LocalDateTime.now())) {
            byExamAndUser.setFinished(true);
            studentTakeExamRepository.save(byExamAndUser);
            throw new Exception("exam finished");
        }
        return endingTime;
    }

    private  final OptionsRepository optionsRepository;

    @Override
    public void answerQuestion(Long studentAnswerId, Long optionId, String essay) throws Exception {
        if (optionId != null) {
            Optional<SelectOptionAnswer> byId = selectOptionAnswerRepository.findById(studentAnswerId);
            if (byId.isEmpty()) {
                throw new Exception(" student question answer not found");
            }
            SelectOptionAnswer selectOptionAnswer = byId.get();
            Optional<Options> byId1 = optionsRepository.findById(optionId);
            if (byId1.isEmpty()) throw new Exception(" selected option not found");
            Options options = byId1.get();
            selectOptionAnswer.setSelectedOption(options);
            selectOptionAnswerRepository.save(selectOptionAnswer);

        }

        if (essay != null) {
            Optional<EssayAnswer> byId = essayAnswerRepository.findById(studentAnswerId);
            if (byId.isEmpty()) {
                throw new Exception(" student question answer not found");
            }
            EssayAnswer essayAnswer = byId.get();
            essayAnswer.setEssay(essay);
            essayAnswerRepository.save(essayAnswer);
        }


    }

    private final UserRepository userRepository;

    @Override
    public void finishExam(Long examId, Long studentId) throws Exception {

        Optional<Exam> byId = examRepository.findById(examId);
        if (byId.isEmpty()) throw new Exception("exam not found");
        Exam exam = byId.get();
        Optional<User> byId1 = userRepository.findById(studentId);
        if (byId1.isEmpty()) throw new Exception("student not found");
        User user = byId1.get();
        StudentTakeExam byExamAndUser = studentTakeExamRepository.findByExamAndUser(exam, user);
        byExamAndUser.setFinished(true);
        studentTakeExamRepository.save(byExamAndUser);
    }
}
