package com.fara.demo.controller.studentControllers;


import com.fara.demo.domain.ApplicationUserDetails;
import com.fara.demo.domain.Course;
import com.fara.demo.domain.Exam;
import com.fara.demo.domain.User;
import com.fara.demo.domain.answer.BaseStudentAnswer;
import com.fara.demo.repository.UserRepository;
import com.fara.demo.service.StudentServiceApi;
import com.fara.demo.service.UserServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
//@PreAuthorize("hasRole('ROLE_STUDENT')")
public class StudentController {
    private final UserRepository userRepository;
    private final StudentServiceApi studentService;

    @Autowired
    public StudentController( UserRepository userRepository, StudentServiceApi studentService) {
        this.userRepository = userRepository;
        this.studentService = studentService;
    }

    @GetMapping({"/courses/{studentId}", "courses/{studentId}"})
    List<Course> getCourses(@PathVariable Long studentId) {

        try {
            Optional<User> byId = userRepository.findById(studentId);
            if (byId.isPresent()) {
                List<Course> studentCourses = studentService.getStudentCourses(byId.get());
                return studentCourses;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
        @GetMapping("view-exams/{courseId}")
        List<Exam> getExams( @PathVariable Long courseId) {

        try {
            List<Exam> courseExams = studentService.getCourseExams(courseId);
            return courseExams;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
            return null;
        }

    @GetMapping("take-exam/{examId}")
    List<BaseStudentAnswer> takeExam( @PathVariable Long examId) {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ApplicationUserDetails applicationUserDetails = (ApplicationUserDetails) principal;
            User user = applicationUserDetails.getUser();
            List<BaseStudentAnswer> studentExamQuestions = studentService.getStudentExamQuestion(user, examId);

            LocalDateTime finishTime = studentService.getFinishTime(user, examId);
            return studentExamQuestions;

        } catch (Exception e) {
            e.printStackTrace();

        }

       return null;
    }

    @PostMapping("answer/{studentAnswerId}/{examId}")
    void getAnswers(
            @PathVariable("examId") Long examId,
            @PathVariable("studentAnswerId") Long studentAnswerId,
            @RequestParam(value = "optionId", required = false) Long optionId,
            @RequestParam(value = "essay", required = false) String essay) {

        try {

            studentService.answerQuestion(studentAnswerId, optionId, essay);

        } catch (IllegalStateException e) {
            e.printStackTrace();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @PostMapping("finish-exam")
    void finishExam(
            @RequestParam("examId") Long examId,
            @RequestParam("studentId") Long studentId,
            @RequestParam("courseId") Long courseId) {


        try {

            studentService.finishExam(examId, studentId);

        }  catch (Exception e) {
            e.printStackTrace();

        }

    }

}
