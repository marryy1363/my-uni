package com.fara.demo.controller.tutorControllers;

import com.fara.demo.domain.ApplicationUserDetails;
import com.fara.demo.domain.Course;
import com.fara.demo.domain.Exam;
import com.fara.demo.domain.Question.BaseQuestion;
import com.fara.demo.domain.Question.ExamQuestionsPoints;
import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.BaseStudentAnswer;
import com.fara.demo.repository.CourseRepository;
import com.fara.demo.repository.ExamRepository;
import com.fara.demo.service.ExamQuestionPointsServiceApi;
import com.fara.demo.service.ExamServiceApi;
import com.fara.demo.service.QuestionServiceApi;
import com.fara.demo.service.TutorServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tutor")
//@PreAuthorize("hasRole('ROLE_TUTOR')")
public class TutorController {

    private final CourseRepository courseRepository;
    private final ExamRepository examRepository;
    private final ExamServiceApi examServiceApi;

    @Autowired
    public TutorController(CourseRepository courseRepository, ExamRepository examRepository, ExamServiceApi examServiceApi, TutorServiceApi tutorServiceApi, QuestionServiceApi questionServiceApi, ExamQuestionPointsServiceApi examQuestionPointsServiceApi) {
        this.courseRepository = courseRepository;
        this.examRepository = examRepository;
        this.examServiceApi = examServiceApi;
        this.tutorServiceApi = tutorServiceApi;
        this.questionServiceApi = questionServiceApi;
        this.examQuestionPointsServiceApi = examQuestionPointsServiceApi;
    }

    @GetMapping
    String getPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUserDetails requestedUser =
                ((ApplicationUserDetails) authentication.getPrincipal());
        User tutor = requestedUser.getUser();
        model.addAttribute("tutor", tutor);
        List<Course> byTutor = courseRepository.findByTutor(tutor);
        model.addAttribute("courseList", byTutor);
        return "tutor";
    }

    @GetMapping("edit-course/{id}")
    String editCourse(
            @PathVariable("id") Long id,
            Model model) {
        try {
            Optional<Course> byId = courseRepository.findById(id);
            if (byId.isEmpty()) throw new IllegalStateException("course not found");
            Course course = byId.get();
            model.addAttribute("course", course);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "something went wrong " + e.getMessage());
        }

        return "teacher/edit-course";
    }

    private final TutorServiceApi tutorServiceApi;
    private final QuestionServiceApi questionServiceApi;

    @GetMapping("edit-exam/{examId}")
    String getEditExam(
            @PathVariable("examId") Long examId,
            Model model) {
        try {

            Optional<Exam> byId = examRepository.findById(examId);
            if (byId.isEmpty()) throw new IllegalStateException("exam not found");
            Exam exam = byId.get();
            List<ExamQuestionsPoints> allQuestionsByExam = questionServiceApi.findAllQuestionsByExamId(examId);
            model.addAttribute("questionsList", allQuestionsByExam);
            Integer sum = 0;
            for (ExamQuestionsPoints q : allQuestionsByExam) {
                sum += q.getPoint();
            }
            List<User> students = tutorServiceApi.findStudentsTookExamByExamId(exam);
            model.addAttribute("students", students);
            model.addAttribute("exam", exam);
            model.addAttribute("sum", sum);
            return "teacher/edit-exam";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errors", "something went wrong " + e.getMessage());
        }
        return "teacher/edit-exam";

    }

    @GetMapping("delete-exam/{id}")
    String getDeleteExam(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
//                    Model model
    ) {
        try {
            Optional<Exam> byId = examRepository.findById(id);
            if (byId.isEmpty()) throw new IllegalStateException("exam not found");
            Exam exam = byId.get();
            examRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "deleting exam successful");
            return "redirect:/tutor";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errors", "something went wrong " + e.getMessage());
        }
        return "redirect:/tutor";
    }

    //    ???????????????????????????????????????????????????????????????????????
    @PostMapping("edit-exams/{id}")
    void editExam(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("brief") String brief,
            @RequestParam("timeInMinutes") Integer timeInMinutes,
            @RequestParam("examDate") String examDate

    ) {

        Optional<Exam> byId = examRepository.findById(id);
        if (byId.isEmpty()) throw new IllegalStateException("exam not found");
        Exam exam = byId.get();


        try {

            examServiceApi.editExam(
                    id,
                    title,
                    brief,
                    timeInMinutes,
                    examDate
            );


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @PostMapping("create-exams")
    void makeNewExam(
            @RequestParam("courseId") Long courseId,
            @RequestParam("title") String title,
            @RequestParam("brief") String brief,
            @RequestParam("timeInMinutes") Integer timeInMinutes,
            @RequestParam("examDate") String examDate

    ) {
        try {

            examServiceApi.createNewExamForCourse(
                    courseId,
                    title,
                    brief,
                    timeInMinutes,
                    examDate
            );

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /////////////////////                        <a class=" text-success "
    //                           th:href="@{'/tutor/edit-question/' +${question.baseQuestion.id}}">edit</a><br>
    //          edit question
    //                        <a class=" text-danger " th:href="@{'/tutor/delete-question/' +${question.baseQuestion.id}}">delete</a>
    //          delete question
    //
    //          new question
    //
    //          search bank of questions with title
    //
    //=================================================================================

    @GetMapping("new-question/{examId}")
    String createQuestionForExam(
            @PathVariable("examId") Long examId,
            Model model) {
        model.addAttribute("examId", examId);
        return "teacher/create-question";
    }


    //            '/new-questions-bank/{examId}'
    @GetMapping("new-questions-bank/{examId}")
    String createQuestionForExamFromBank(
            @PathVariable("examId") Long examId,
            Model model) {
        try {
            List<BaseQuestion> search = questionServiceApi.findQuery("", "all", examId);
            if (search.size() == 0) throw new Exception("no results found");
            model.addAttribute("search", search);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "something went wrong " + e.getMessage());
        }

        model.addAttribute("examId", examId);
        return "teacher/new-questions-bank";
    }

    @PostMapping("/search-questions/{examId}")
    String createQuestionForExamFromBankSearch(
            @PathVariable("examId") Long examId,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam("type") String type,
            Model model) {

        try {
            List<BaseQuestion> search = questionServiceApi.findQuery(query, type, examId);
            if (search.size() == 0) throw new Exception("no results found");
            model.addAttribute("search", search);
        } catch (IllegalStateException e) {
            model.addAttribute("err", "something went wrong " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            model.addAttribute("error", "something went wrong " + e.getMessage());
            e.printStackTrace();
        }

        model.addAttribute("examId", examId);
        return "teacher/new-questions-bank";
    }

    //@{'/tutor/select-question-bank/' +${question.id}+'/'+${examId}
    @GetMapping("/select-question-bank/{questionId}/{examId}")
    String selectQuestionForExamFromBankSearch(
            @PathVariable("examId") Long examId,
            @PathVariable("questionId") Long questionId,
//            @RequestParam(value = "query", required = false) String query,
//            @RequestParam("type") String type,
            Model model) {

//        try {
//            List<BaseQuestion> search = questionServiceApi.findQuery(query, type, examId);
//            if (search.size() == 0) throw new Exception("no results found");
//            model.addAttribute("search", search);
//        } catch (IllegalStateException e) {
//            model.addAttribute("err", "something went wrong " + e.getMessage());
//            e.printStackTrace();
//        } catch (Exception e) {
//            model.addAttribute("error", "something went wrong " + e.getMessage());
//            e.printStackTrace();
//        }

        Optional<BaseQuestion> byId = questionServiceApi.findById(questionId);
        BaseQuestion baseQuestion = byId.get();
        model.addAttribute("examId", examId);
        model.addAttribute("question", baseQuestion);

        return "teacher/new-questions-bank-point";
    }
    //new-bank-question/121/77
    //@{'~/tutor/new-bank-question/' +${question.id}+'/' +${examId}  }

    @PostMapping("/new-bank-question/{questionId}/{examId}")
    String assignPointQuestionForExamFromBankSearch(
            @PathVariable("examId") Long examId,
            @PathVariable("questionId") Long questionId,
            @RequestParam(value = "points", required = false) Integer points,
            Model model) {
        try {
            questionServiceApi.selectQuestionFromBank(examId, questionId, points);
            model.addAttribute("success", "question successfully added");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            model.addAttribute("err", "something went wrong " + e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "something went wrong " + e.getMessage());
        }
        Optional<BaseQuestion> byId = questionServiceApi.findById(questionId);
        BaseQuestion baseQuestion = byId.get();
        model.addAttribute("examId", examId);
        model.addAttribute("question", baseQuestion);

        return "teacher/new-questions-bank-point";
    }

    //--------------------------------------------------------------------------------

    //            '/new-multi-option/{examId}'
    @GetMapping("new-multi-option/{examId}")
    String createMultiOptionQuestionForExam(
            @PathVariable("examId") Long examId,
            Model model) {
        model.addAttribute("created", false);
        model.addAttribute("examId", examId);
        return "teacher/new-multi-option";
    }

    @PostMapping("new-multi-option/{examId}")
    ExamQuestionsPoints postCreateMultiOptionQuestionForExam(
            @PathVariable("examId") Long examId,
            @RequestParam("title") String title,
            @RequestParam("points") Integer points,
            @RequestParam("prompt") String prompt,
            @RequestParam("option") List<String> option,
            @RequestParam("answer") String answer

            ) {

        System.out.println(
                examId + "-" +
                        title + "-" +
                        points + "-" +
                        prompt + "--" +
                        option + "-" +
                        answer
        );
        try {

            ExamQuestionsPoints multiOptionQuestion = questionServiceApi.createMultiOptionQuestion(
                    examId,
                    title,
                    points,
                    prompt,
                    option,
                    answer
            );

            return multiOptionQuestion;

        } catch (IllegalStateException e) {
            e.printStackTrace();

        }


        return null;
    }



    @PostMapping("new-essay-question/{examId}")
    ExamQuestionsPoints postCreateEssayQuestionForExam(
            @PathVariable("examId") Long examId,
            @RequestParam("title") String title,
            @RequestParam("points") Integer points,
            @RequestParam("prompt") String prompt
           ) {
        try {
            ExamQuestionsPoints newEssayQuestion = questionServiceApi.createNewEssayQuestion(title,
                    points,
                    prompt, examId);
            return newEssayQuestion;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return null;
    }

    ///edit-question/152

    @GetMapping("edit-question/{questionId}")
    String editQuestions(
            @PathVariable("questionId") Long questionId,
            Model model) {

        Optional<BaseQuestion> byId = questionServiceApi.findById(questionId);
        if (byId.isEmpty()) {
            throw new IllegalStateException(" question not found ");
        }

        BaseQuestion baseQuestion = byId.get();
        model.addAttribute("question", baseQuestion);
        if (baseQuestion.getQuestionType().equals("MultiOptionQuestion")) {
            return "teacher/edit-multi-question";
        } else {
            return "teacher/edit-essay-question";
        }
    }
//title
//prompt
//answer

    @PostMapping("edit-question/{questionId}")
    String editQ(
            @PathVariable("questionId") Long questionId,
            @RequestParam("title") String title,
            @RequestParam(value = "answer", required = false) Long answer,
            @RequestParam("prompt") String prompt,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            questionServiceApi.editQuestion(
                    questionId,
                    title,
                    Optional.ofNullable(answer),
                    prompt
            );
            redirectAttributes.addFlashAttribute("success", "editing successful");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("err", "something went wrong " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "something went wrong " + e.getMessage());
        }
//        System.out.println(
//                "questionId="+questionId+" "+
//                "title="+title+" "+
//                "answer="+answer+" "+
//                "prompt="+prompt);


        return "redirect:/tutor/edit-question/" + questionId;
    }

    //                           th:href="@{'/tutor/edit-question/' +${question.baseQuestion.id}}">edit</a><br>

    private final ExamQuestionPointsServiceApi examQuestionPointsServiceApi;

    //                        <a class=" text-danger " th:href="@{'/tutor/delete-question/' +${question.baseQuestion.id}}">delete</a>
    @DeleteMapping("delete-question/{pqid}")
    void deleteQuestionPointed(
            @PathVariable("pqid") Long pqid


    ) {
        Long examId = 0L;
        try {
            examId = examQuestionPointsServiceApi.findAndDeletePointedQuestionAndGiveExamId(pqid);


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    //    view-student-exam-result ' +${user.id} +'/'+${exam.id} }"
    @GetMapping("view-student-exam-result/{studentId}/{examId}")
    String deleteQuestionPointed(
            @PathVariable("studentId") Long studentId,
            @PathVariable("examId") Long examId,
            RedirectAttributes redirectAttributes,
            Model model

    ) {

        try {
//      list of essay questions
            List<BaseStudentAnswer> answers = tutorServiceApi.getEssayQuestionsWithAnswer(studentId, examId);
            int totalScore = tutorServiceApi.getTotalScore(answers);
            model.addAttribute("totalScore", totalScore);
            model.addAttribute("answers", answers);
            model.addAttribute("studentId", studentId);
            model.addAttribute("examId", examId);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "something went wrong " + e.getMessage());
        }
        return "teacher/view-student-exam-result";

    }

    @PostMapping("set-score/{questionAnswerId}")
    String setScore(
            @PathVariable("questionAnswerId") Long questionAnswerId,
            @RequestParam("studentId") Long studentId,
            @RequestParam("score") Integer score,
            @RequestParam("examId") Long examId,

            RedirectAttributes redirectAttributes,
            Model model

    ) {

        try {
            System.out.println(questionAnswerId);
            System.out.println(score);
            tutorServiceApi.setEssayScore(questionAnswerId,score);

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "something went wrong " + e.getMessage());
        }
        return "redirect:/tutor/view-student-exam-result/"+studentId+"/"+examId;

    }


}
