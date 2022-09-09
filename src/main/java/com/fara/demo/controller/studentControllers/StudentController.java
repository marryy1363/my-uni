package com.fara.demo.controller.studentControllers;


import com.fara.demo.domain.ApplicationUserDetails;
import com.fara.demo.domain.Course;
import com.fara.demo.domain.Exam;
import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.BaseStudentAnswer;
import com.fara.demo.service.StudentServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentController {

    private final StudentServiceApi studentService;

    @Autowired
    public StudentController(StudentServiceApi studentService) {
        this.studentService = studentService;
    }

    @GetMapping({"/", ""})
    String getPanel(Model model) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            ApplicationUserDetails requestedUser =
                    ((ApplicationUserDetails) authentication.getPrincipal());
            String fullName = requestedUser.getUser().getFullName();
            User student = requestedUser.getUser();
            model.addAttribute("fullName", fullName);
            List<Course> courseList = studentService.getStudentCourses(student);
            model.addAttribute("courseList", courseList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
        }

        return "student/student";
    }

    @GetMapping("view-exams/{courseId}")
    String getExams(Model model, @PathVariable Long courseId) {

        try {
            List<Exam> courseExams = studentService.getCourseExams(courseId);
            model.addAttribute("examList", courseExams);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            model.addAttribute("err", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
        }

        return "student/view-exams";
    }

    @GetMapping("take-exam/{examId}")
    String takeExam(Model model, @PathVariable Long examId) {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ApplicationUserDetails applicationUserDetails = (ApplicationUserDetails) principal;
            User user = applicationUserDetails.getUser();
            List<BaseStudentAnswer> studentExamQuestions = studentService.getStudentExamQuestion(user, examId);

            LocalDateTime finishTime = studentService.getFinishTime(user, examId);
            model.addAttribute("questionList", studentExamQuestions);

            model.addAttribute("finishTime", finishTime);

        } catch (IllegalStateException e) {
            e.printStackTrace();
            model.addAttribute("err", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
        }

        return "student/take-exam";
    }

    @PostMapping("answer/{studentAnswerId}/{examId}")
    String getAnswers(
            Model model,
            @PathVariable("examId") Long examId,
            @PathVariable("studentAnswerId") Long studentAnswerId,
            @RequestParam(value = "optionId", required = false) Long optionId,
            @RequestParam(value = "essay", required = false) String essay) {

        try {

            studentService.answerQuestion(studentAnswerId, optionId, essay);

        } catch (IllegalStateException e) {
            e.printStackTrace();
            model.addAttribute("err", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/student/take-exam/{examId}";
    }

    @PostMapping("finish-exam")
    String finishExam(
            RedirectAttributes redirectAttributes,
            @RequestParam("examId") Long examId,
            @RequestParam("studentId") Long studentId,
            @RequestParam("courseId") Long courseId) {


        try {

            System.out.println(studentId);
            System.out.println(examId);

            studentService.finishExam(examId, studentId);
            redirectAttributes.addFlashAttribute("success","exam finished successfully");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("err", e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/student/view-exams/" + courseId;
    }

}
