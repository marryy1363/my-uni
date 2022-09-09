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

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class AdminController {

    final UserServiceApi userServiceApi;
    final UserRepository userRepository;
    final CourseService courseService;
    final CourseRepository courseRepository;

    @GetMapping({"", "/"})
    List<User> getPanel() {
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


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        try {

            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                model.addAttribute("user", byId.get());
                return "edit";
            } else model.addAttribute("error", " user not found ");
        } catch (Exception e) {
            model.addAttribute("error", "something went wrong " + e.getMessage());
        }
        return "edit";
    }

    @GetMapping("/accept/{id}")
    public String accept(@PathVariable("id") Long id, Model model) {
        try {

            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                User user = byId.get();
                user.setEnable(true);
                userRepository.save(user);
                return "redirect:/admin";
            } else model.addAttribute(
                    "error", " user not found ");
        } catch (Exception e) {
            model.addAttribute("error", "something went wrong " + e.getMessage());
        }
        return "redirect:/admin";
    }


    @GetMapping("/edit-course/{id}")
    public String editCourse(
            @PathVariable("id") Long id,
            Model model
    ) {
        try {
            Optional<Course> byId = courseRepository.findById(id);
            if (byId.isPresent()) {
                model.addAttribute("course", byId.get());
                model.addAttribute("listUser", byId.get().getStudents());
            } else
                model.addAttribute("error", " course not found ");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "something went wrong " + e.getMessage());
        }
        return "course-edit";
    }

    @GetMapping("courses")
    public String getCources(Model model) {
        List<Course> all = ((List<Course>) courseRepository.findAll());
        model.addAttribute("courseList", all);
        return "courses";
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


    @PostMapping("/search-course")//assign
    public String getSearchCourse(
            @RequestParam("courseId") Long courseId,
            @RequestParam("userId") Long userId,
            Model model) {
        try {
            Optional<Course> byId1 = courseRepository.findById(courseId);
            Optional<User> byId = userRepository.findById(userId);
            if (byId.isPresent() && byId1.isPresent()) {
                User student = byId.get();
                model.addAttribute("user", student);
                Course course = byId1.get();
                course.getStudents().add(student);
                courseRepository.save(course);
                model.addAttribute("course", course);

                return "edit";
            } else {
                model.addAttribute("error search failed");
            }
        } catch (Exception e) {
            model.addAttribute("error " + e.getMessage());
        }
        return "edit";
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

    @PostMapping("search-user")//assign
    public String searchUsers(
            @RequestParam("field") UserFields fields,
            @RequestParam("query") String query,
            @RequestParam("role") Roles role,
            @RequestParam("enable") boolean enable,
            Model model,
            RedirectAttributes redirectAttributes

    ) {

        System.out.println("hi");

        System.out.println(
                "fields:" + fields + " " +
                        "query:" + query + " " +
                        "role:" + role + " " +
                        "enable:" + enable + " "
        );

//        try {
//            List<User> users = userServiceApi.searchWithFieldsAndRoleAndStatusEnablity(fields,
//                    query,
//                    role,
//                    enable);
//            if (users.size() == 0) {
//                throw new Exception(" no users found ");
//            }
//            redirectAttributes.addFlashAttribute("listUser", users);
//            return "redirect:/admin";
//        } catch (Exception e) {
//            e.printStackTrace();
//             redirectAttributes.addFlashAttribute("error", " some thing went wrong " + e.getMessage());
//            return "redirect:/admin";
//        }
        return "redirect:/admin";

    }

    @GetMapping("search-user")//assign
    public String searchGetUsers(
    ) {

        return "redirect:/admin";
    }

    @PostMapping("search-for-users")//assign
    public String searchForUsers(
            @RequestParam(value = "field", required = false) UserFields fieldss,
            @RequestParam(value = "query", required = false) String queryy,
            @RequestParam(value = "role", required = false) Roles rolee,
            @RequestParam(value = "enable", required = false) Boolean enablee,
            Model model,
            RedirectAttributes redirectAttributes

    ) {
        Optional<UserFields> fields = Optional.ofNullable(fieldss);
        Optional<String> query = Optional.ofNullable(queryy);
        Optional<Roles> role = Optional.ofNullable(rolee);
        Optional<Boolean> enable = Optional.ofNullable(enablee);
        System.out.println("hi");

        System.out.println(
                "fields:" + fields + " " +
                        "query:" + query + " " +
                        "role:" + role + " " +
                        "enable:" + enable + " "
        );


        try {
            List<User> users = userServiceApi.searchWithFieldsAndRoleAndStatusEnablity(fields,
                    query,
                    role,
                    enable);

            if (users == null) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");

            if (users.size() == 0) {
                throw new Exception(" no users found ");
            }
            model.addAttribute("listUser", users);
            return "admin";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", " some thing went wrong " + e.getMessage());
            return "admin";
        }


    }


    //1 course - edit html
//    delete student from course
//    @{'/admin/remove-stuedent/' +${user.id}}+'/course/'+${course.id}
    private final AdminServiceApi adminServiceApi;

    @GetMapping("remove-stuedent/{studentId}/course/{courseId}")//assign
    public String searchGetUsers(
            @PathVariable("studentId") Long studentId,
            @PathVariable("courseId") Long courseId,

            RedirectAttributes redirectAttributes

    ) {

        try {

            adminServiceApi.removeStudentFromCourse(studentId, courseId);
            redirectAttributes.addFlashAttribute("success", "successful deleting student");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "something went wrong " + e.getMessage());
        }
        return "redirect:/admin/edit-course/" + courseId;
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


    //3
// assign tutor to the course
//    @{'~/admin/assign-tutor/'+${course.id}}///////////////////////
    @GetMapping("/assign-tutor/{courseId}")
    public String getForm(
            @PathVariable("courseId") Long courseId,
            @RequestParam(value = "field", required = false) UserFields fieldss,
            @RequestParam(value = "query", required = false) String queryy,

            Model model

    ) {


        model.addAttribute("courseId", courseId);
        Optional<UserFields> fields = Optional.ofNullable(fieldss);
        Optional<String> query = Optional.ofNullable(queryy);


        try {
            List<User> users = userServiceApi.searchWithFieldsAndRoleAndStatusEnablity(fields,
                    query,
                    Optional.of(Roles.TUTOR),
                    Optional.of(true));
            if (users == null) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");
            if (users.size() == 0) {
                throw new Exception(" no users found ");
            }
            model.addAttribute("listUser", users);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", " some thing went wrong " + e.getMessage());
        }
        return "assign-tutor";
    }


    @PostMapping("/assign-tutor/{courseId}")
    public String getFormb(
            @PathVariable("courseId") Long courseId,
            @RequestParam(value = "field", required = false) UserFields fieldss,
            @RequestParam(value = "query", required = false) String queryy,

            Model model

    ) {


        model.addAttribute("courseId", courseId);
        Optional<UserFields> fields = Optional.ofNullable(fieldss);
        Optional<String> query = Optional.ofNullable(queryy);


        try {
            List<User> users = userServiceApi.searchWithFieldsAndRoleAndStatusEnablity(fields,
                    query,
                    Optional.of(Roles.TUTOR),
                    Optional.of(true));

            if (users == null) throw new Exception(" no users found ");

            if (users.isEmpty()) throw new Exception(" no users found ");
            if (users.size() == 0) {
                throw new Exception(" no users found ");
            }
            model.addAttribute("listUser", users);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", " some thing went wrong " + e.getMessage());
        }
        return "assign-tutor";
    }

    @GetMapping("/assign-tutor-fin/{courseId}/{tutorId}")
    public String assignTutor(
            @PathVariable("courseId") Long courseId,
            @PathVariable("tutorId") Long tutorId,
            Model model,
            RedirectAttributes redirectAttributes

    ) {


        try {
            adminServiceApi.assignTutorToCourse(
                    courseId,
                    tutorId
            );


            redirectAttributes.addFlashAttribute("success", "adding tutor was successful");
            return "redirect:/admin/edit-course/{courseId}";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("err", " some thing went wrong " + e.getMessage());
            return "redirect:/admin/assign-tutor/{courseId}";
        }
    }
}

