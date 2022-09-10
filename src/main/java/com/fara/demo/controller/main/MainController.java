package com.fara.demo.controller.main;


import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import com.fara.demo.service.UserServiceApi;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilte


@RestController

@AllArgsConstructor
public class MainController {

//    @GetMapping("register")
//    public String getRegister(User user) {
//
//        return "register";
//    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }



    UserServiceApi userServiceApi;

    @PostMapping("/register")
    public User addPerson(@RequestBody User user) throws Exception {

        try {
            userServiceApi.preRegister(

                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRoles()
                   );
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("something went wrong"+e.getMessage());
        }

        return user;
    }

//    @GetMapping("/403")
//    public String get403() {
//        return "403";
//    }


}
