package com.fara.demo.service.impl;

import com.fara.demo.domain.Course;
import com.fara.demo.domain.Exam;
import com.fara.demo.domain.Question.*;
import com.fara.demo.repository.*;
import com.fara.demo.service.QuestionServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class QuestionService implements QuestionServiceApi {

    private final ExamQuestionsPointsRepository examQuestionsPointsRepository;

    @Autowired
    public QuestionService(ExamQuestionsPointsRepository examQuestionsPointsRepository, ExamRepository examRepository, EssayQuestionRepository essayQuestionRepository, QuestionRepository questionRepository, MultiOptionQuestionRepository multiOptionQuestionRepository, OptionsRepository optionsRepository) {
        this.examQuestionsPointsRepository = examQuestionsPointsRepository;
        this.examRepository = examRepository;
        this.essayQuestionRepository = essayQuestionRepository;
        this.questionRepository = questionRepository;
        this.multiOptionQuestionRepository = multiOptionQuestionRepository;
        this.optionsRepository = optionsRepository;
    }

    @Override
    public Optional<BaseQuestion> findById(Long questionId) {
        return questionRepository.findById(questionId);
    }

    @Override
    public List<ExamQuestionsPoints> findAllQuestionsByExamId(Long id) {

        return examQuestionsPointsRepository.findByExamId(id);
    }

    private final ExamRepository examRepository;
    private final EssayQuestionRepository essayQuestionRepository;
    private final QuestionRepository questionRepository;
    private final MultiOptionQuestionRepository multiOptionQuestionRepository;
    private final OptionsRepository optionsRepository;

    @Override
    public ExamQuestionsPoints createNewEssayQuestion(String title, Integer points, String prompt, Long examId) {
        if (questionRepository.existsByTitle(title)) {
            throw new IllegalStateException(" duplicate title");
        }
        Optional<Exam> byId = examRepository.findById(examId);
        if (byId.isEmpty()) throw new IllegalStateException("cant find the exam");
        if (points <= 0) throw new IllegalStateException("points are not positve integer");
        if (prompt.isEmpty()) throw new IllegalStateException("prompt cant be empty");
        title = title.trim();
        prompt = prompt.trim();
        Exam exam = byId.get();
        Course course = exam.getCourse();
        EssayQuestion essayQuestion = new EssayQuestion(title, course, prompt);
        EssayQuestion essayQuestionSaved = essayQuestionRepository.save(essayQuestion);
        ExamQuestionsPoints pointedQuestion = new ExamQuestionsPoints(exam, essayQuestionSaved, points);
        return examQuestionsPointsRepository.save(pointedQuestion);
    }

    @Override
    @Transactional
    public ExamQuestionsPoints createMultiOptionQuestion(Long examId, String title, Integer points, String prompt, List<String> options, String answer) {

        if (questionRepository.existsByTitle(title)) {
            throw new IllegalStateException(" duplicate title");
        }
        Set<String> allOptions = new HashSet<>();
        for (String option : options) {
            if (!allOptions.add(option.trim())) throw new IllegalStateException("duplicate options");
        }
        if (allOptions.add(answer.trim())) throw new IllegalStateException("answer not found");
        Optional<Exam> byId = examRepository.findById(examId);
        if (byId.isEmpty()) throw new IllegalStateException("cant find the exam");
        if (points <= 0) throw new IllegalStateException("points are not positve integer");
        if (prompt.isEmpty()) throw new IllegalStateException("prompt cant be empty");
//        if (option.isEmpty()) throw new IllegalStateException("option cant be empty");
//        option = option.trim();
        title = title.trim();
        prompt = prompt.trim();

        Exam exam = byId.get();
        Course course = exam.getCourse();
        MultiOptionQuestion multiOptionQuestion = new MultiOptionQuestion(title, course, prompt);
        MultiOptionQuestion save = multiOptionQuestionRepository.save(multiOptionQuestion);
        Options ans = null;
        for (String option : allOptions) {
            Options opt = new Options(option);
            opt.setQuestion(save);
            Options savedOption = optionsRepository.save(opt);
            if (option.equals(answer))
                ans = savedOption;
        }
        if (ans == null) throw new IllegalStateException("answer not selected");
        multiOptionQuestion.setAnswer(ans);
        multiOptionQuestionRepository.save(multiOptionQuestion);
        ExamQuestionsPoints pointedQuestion = new ExamQuestionsPoints(exam, multiOptionQuestion, points);
        return examQuestionsPointsRepository.save(pointedQuestion);
    }

    @Override
    public List<BaseQuestion> findQuery(String query, String type, Long examId) {
        Optional<Exam> byId = examRepository.findById(examId);
        if (byId.isEmpty()) throw new IllegalStateException("exam not found");
        Exam exam = byId.get();
        Course course = exam.getCourse();
        Specification<BaseQuestion> title = QuestionSpecification.title(query);
        Specification<BaseQuestion> typee = QuestionSpecification.type(type);
        Specification<BaseQuestion> courseIs = QuestionSpecification.courseIs(course);

        List<BaseQuestion> all = questionRepository.findAll(
                Specification.where(
                        typee
                                .and(
                                        title
                                ).and(
                                        courseIs
                                )

                ));
        return all;
    }

    @Override
    public ExamQuestionsPoints selectQuestionFromBank(Long examId, Long questionId, Integer points) {
        if (points==null) throw new IllegalStateException("points cant be null ");
        Optional<Exam> byId = examRepository.findById(examId);
        if (byId.isEmpty()) {
            throw new IllegalStateException(" exam not found ");
        }
        Exam exam = byId.get();
        Optional<BaseQuestion> byId1 = questionRepository.findById(questionId);
        if (byId1.isEmpty()) throw new IllegalStateException(" question not found");
        BaseQuestion baseQuestion = byId1.get();
        if (points < 0) throw new IllegalStateException(" only positive numbers");
        if (examQuestionsPointsRepository.existsByBaseQuestionAndExam(baseQuestion, exam))
            throw new IllegalStateException(" question selected before repetitious selection");
        ExamQuestionsPoints pointedQuestion = new ExamQuestionsPoints(exam, baseQuestion, points);
        return examQuestionsPointsRepository.save(pointedQuestion);
    }

    @Override
    public BaseQuestion editQuestion(Long questionId, String title, Optional<Long> answer, String prompt) {
        Optional<BaseQuestion> byId = questionRepository.findById(questionId);
        if (byId.isEmpty()) {
            throw new IllegalStateException(" question not found");
        }
        if (title.length()<2) throw new IllegalStateException("short title");
        if (prompt.length()<5) throw new IllegalStateException("short prompt");
        BaseQuestion baseQuestion = byId.get();

        if (Objects.equals(baseQuestion.getQuestionType(), "MultiOptionQuestion") && answer.isPresent()) {
            Optional<MultiOptionQuestion> byId1 = multiOptionQuestionRepository.findById(questionId);
            if (byId1.isEmpty()) throw new IllegalStateException(" question not found");
            MultiOptionQuestion multiOptionQuestion = byId1.get();
            Optional<Options> byId2 = optionsRepository.findById(answer.get());
            if (byId2.isEmpty()) throw new IllegalStateException("answer not found");
            Options options = byId2.get();
            multiOptionQuestion.setAnswer(options);
            multiOptionQuestion.setTitle(title);
            multiOptionQuestion.setPrompt(prompt);
           return multiOptionQuestionRepository.save(multiOptionQuestion);
        }
        baseQuestion.setPrompt(prompt);
        baseQuestion.setTitle(title);
        return questionRepository.save(baseQuestion);
    }
}
