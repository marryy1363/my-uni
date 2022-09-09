package com.fara.demo.service.impl;


import com.fara.demo.domain.Exam;
import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.BaseStudentAnswer;
import com.fara.demo.domain.answer.EssayAnswer;
import com.fara.demo.domain.answer.StudentTakeExam;
import com.fara.demo.repository.BaseStudentAnswerRepository;
import com.fara.demo.repository.EssayAnswerRepository;
import com.fara.demo.repository.StudentTakeExamRepository;
import com.fara.demo.service.TutorServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TutorService implements TutorServiceApi {

    private final BaseStudentAnswerRepository baseStudentAnswerRepository;
    private final StudentTakeExamRepository studentTakeExamRepository;

    @Autowired
    public TutorService(BaseStudentAnswerRepository baseStudentAnswerRepository, StudentTakeExamRepository studentTakeExamRepository, EssayAnswerRepository essayAnswerRepository) {
        this.baseStudentAnswerRepository = baseStudentAnswerRepository;
        this.studentTakeExamRepository = studentTakeExamRepository;
        this.essayAnswerRepository = essayAnswerRepository;
    }

    @Override
    public List<User> findStudentsTookExamByExamId(Exam exam) {
        List<User> students = new ArrayList<>();
        List<StudentTakeExam> byExam = studentTakeExamRepository.findByExam(exam);
        for (StudentTakeExam record : byExam) {
            if (record.isDone() || record.getFinished()) {
                User student = record.getUser();
                students.add(student);
            }
        }
        return students;
    }

    private final EssayAnswerRepository essayAnswerRepository;

    @Override
    public List<BaseStudentAnswer> getEssayQuestionsWithAnswer(Long studentId, Long examId) throws Exception {


        List<BaseStudentAnswer> byStudentAndExam = baseStudentAnswerRepository.findByStudentIdAndExamId(studentId, examId);
        if (byStudentAndExam == null || byStudentAndExam.isEmpty()) {
            throw new Exception("no essay questions");
        }
        return byStudentAndExam;
    }

    @Override
    public int getTotalScore(List<BaseStudentAnswer> answers) {

        int sum = 0;

        for (BaseStudentAnswer ans : answers) {
            sum += ans.getScore();
        }

        return sum;
    }

    @Override
    public void setEssayScore(Long questionAnswerId, Integer score) throws Exception {
        Optional<EssayAnswer> byId = essayAnswerRepository.findById(questionAnswerId);
        if (byId.isEmpty()) {
            throw new Exception(" essay not found");
        }
        EssayAnswer essayAnswer = byId.get();
        Integer maxPoint = essayAnswer.getExamQuestionsPoints().getPoint();
        if (score > maxPoint) throw new Exception("score is higher than max ");
        if (score < 0) throw new Exception("score must be positive");
        essayAnswer.setScore(score);
        essayAnswerRepository.save(essayAnswer);
    }


}
