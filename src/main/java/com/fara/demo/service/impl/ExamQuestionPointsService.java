package com.fara.demo.service.impl;


import com.fara.demo.domain.Question.ExamQuestionsPoints;
import com.fara.demo.repository.ExamQuestionsPointsRepository;
import com.fara.demo.service.ExamQuestionPointsServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamQuestionPointsService implements ExamQuestionPointsServiceApi {

    private final ExamQuestionsPointsRepository examQuestionsPointsRepository;

    @Autowired
    public ExamQuestionPointsService(ExamQuestionsPointsRepository examQuestionsPointsRepository) {
        this.examQuestionsPointsRepository = examQuestionsPointsRepository;
    }

    @Override
    public Long findAndDeletePointedQuestionAndGiveExamId(Long pqid) {
        Optional<ExamQuestionsPoints> byId = examQuestionsPointsRepository.findById(pqid);
        if (byId.isEmpty()) {
            throw new IllegalStateException(" question not found");

        }
        ExamQuestionsPoints examQuestionsPoints = byId.get();
        Long id = examQuestionsPoints.getExam().getId();
        examQuestionsPointsRepository.delete(examQuestionsPoints);
        return id;
    }
}
