package com.fara.demo.controller.adminControllers;

import com.fara.demo.domain.Course;
import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import com.fara.demo.domain.enums.UserFields;
import com.fara.demo.repository.CourseRepository;
import com.fara.demo.repository.UserRepository;
import com.fara.demo.service.AdminServiceApi;
import com.fara.demo.service.UserServiceApi;
import com.fara.demo.service.impl.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/admin")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminController {

    final UserServiceApi userServiceApi;
    final UserRepository userRepository;
    final CourseService courseService;
    final CourseRepository courseRepository;

    @GetMapping({"", "/"})
    List<User> findAllUsers() {
        List<User> users = userServiceApi.findAll();
        try {
            if (users.size() == 0) {
                throw new Exception(" no users found ");
            }
            return users;
        } catch (Exception e) {
            e.getMessage();
        }
        return users;
    }
    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") Long id  ) throws Exception {


        try {
            Optional<User> byId= byId = userRepository.findById(id);

            if (byId.isPresent()) {
                User user = byId.get();

                return user;
            }
        } catch (Exception e) {
           throw new Exception( "something went wrong " + e.getMessage());
        }

        return null;
    }


    @GetMapping("/accept/{id}")
    public User accept(@PathVariable("id") Long id ) {
        try {

            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                User user = byId.get();
                user.setEnable(true);
                userRepository.save(user);
                return user;
            } else throw new Exception(" user not found ");
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }


    @PutMapping("/edit-course")
    public Set<User> editCourse(@RequestBody Course course) {
        try {
            Optional<Course> byId = courseRepository.findById(course.getId());
            if (byId.isPresent()) {
                Course course1 = byId.get();
             Set<User> studens= course1.getStudents();
             return studens;
            } else
                throw new Exception( " course not found ");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @GetMapping("courses")
    public List<Course> getCourses() {
        List<Course> all = ((List<Course>) courseRepository.findAll());

        return all;
    }


    @PostMapping("create-course")
    public Course courseCreate(@RequestBody Course course) {

        Course aCourse = null;
        try {
            aCourse = courseService.createACourse(
                    course.getCourseName(),
                    course.getStartingDate().toString(),
                    course.getEndingDate().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return aCourse;
    }
    @PutMapping("/assign-course")//assign
    public Course assignCourse(
            @RequestParam("courseId") Long courseId,
            @RequestParam("userId") Long userId
         ) {
        try {
            Optional<Course> byId1 = courseRepository.findById(courseId);
            Optional<User> byId = userRepository.findById(userId);
            if (byId.isPresent() && byId1.isPresent()) {
                User student = byId.get();
                Course course = byId1.get();
                course.getStudents().add(student);
                courseRepository.save(course);


                return course;
            } else {
                throw new Exception("error search failed");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @PutMapping("/edit-user/{id}")//assign
    public User editUser(@RequestBody User user) {

        try {
            userServiceApi.editUser(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRoles(),
                    user.getEnable());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return user;
    }



    //1 course - edit html
//    delete student from course
//    @{'/admin/remove-stuedent/' +${user.id}}+'/course/'+${course.id}
    private final AdminServiceApi adminServiceApi;

    @DeleteMapping("remove-stuedent/{studentId}/course/{courseId}")//assign
    public String searchGetUsers(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId

    ) {

        try {

            adminServiceApi.removeStudentFromCourse(studentId, courseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //2   + assign new student
// @{'~/admin/assign-student-to/'+${course.id}}
    @GetMapping("assign-student-to/{courseId}")
    public String addStudentToCourse(
            @PathVariable("courseId") Long courseId,
            Model model
    ) {

        try {
            List<User> users = userServiceApi.searchWithFieldsAndRoleAndStatusEnablity(
                    Optional.empty(), Optional.empty(), Optional.of(Roles.STUDENT), Optional.of(true)
            );

            if (users == null) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");
            if (users == null) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");

            if (users.size() == 0) {
                throw new Exception(" no users found ");
            }
            model.addAttribute("courseId", courseId);
            model.addAttribute("listUser", users);
            return "admin/select-student";
        } catch (Exception e) {
            model.addAttribute("error", " some thing went wrong " + e.getMessage());
        }
        return "admin/select-student";

    }

    //
    //
    //
    // shows the search results for adding students
    //
    //
    //
    @PostMapping("assign-student-to/search/{courseId}")
    public String searchStudents(
            @RequestParam(value = "field", required = false) UserFields fieldss,

            @RequestParam(value = "query", required = false) String queryy,

            @RequestParam(value = "enable", required = false) Boolean enablee,

            @PathVariable("courseId") Long courseId,
            Model model
    ) {

        try {

            Optional<UserFields> fields = Optional.ofNullable(fieldss);
            Optional<String> query = Optional.ofNullable(queryy);
            Optional<Boolean> enable = Optional.ofNullable(enablee);

            List<User> users = userServiceApi.searchWithFieldsAndRoleAndStatusEnablity(
                    fields, query, Optional.of(Roles.STUDENT), enable/*Optional.of(true)*/
            );
            if (users == null) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");

            if (users == null) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");
            if (users.size() == 0) {
                throw new Exception(" no users found ");
            }
            model.addAttribute("courseId", courseId);
            model.addAttribute("listUser", users);
            return "admin/select-student";
        } catch (Exception e) {
            model.addAttribute("error", " some thing went wrong " + e.getMessage());
        }
        return "admin/select-student";

    }

    //
    //
    //
    //th:href="@{'/admin/select-student/' +${courseId}+'/'+${user.id}}"
    //assign students to course
    //
    //

    @GetMapping("select-student/{courseId}/{studentId}")
    public String addStudentToCourseResult(
            @PathVariable("courseId") Long courseId,
            @PathVariable("studentId") Long studentId,
            Model model
    ) {
        System.out.println(courseId);
        System.out.println(studentId);


        try {

            List<User> users = userServiceApi.searchWithFieldsAndRoleAndStatusEnablity(
                    Optional.empty(), Optional.empty(), Optional.of(Roles.STUDENT), Optional.of(true)
            );
            if (users == null) throw new Exception(" no users found ");
            if (users == null) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");
            if (users.size() == 0) {
                throw new Exception(" no users found ");
            }
            model.addAttribute("courseId", courseId);
            model.addAttribute("listUser", users);

            adminServiceApi.assignAnStudentToCourse(courseId, studentId);


            model.addAttribute("success", "adding student successful");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            model.addAttribute("err", "some thing went wrong " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", " some thing went wrong " + e.getMessage());
        }
        return "admin/select-student";

    }




    @PostMapping("/assign-tutor-fin/{courseId}/{tutorId}")
    public void assignTutor(
            @PathVariable("courseId") Long courseId,
            @PathVariable("tutorId") Long tutorId) {
        try {
            Optional<Course> byId = courseRepository.findById(courseId);
            if (byId.isPresent()) {
                Course course = byId.get();
            }else throw new Exception("course not find");
            Optional<User> byId1 = userRepository.findById(tutorId);
            if (byId1.isPresent()){
                User user = byId1.get();
            }else throw new Exception("tutor not find");
            adminServiceApi.assignTutorToCourse(
                    courseId,
                    tutorId
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

