package com.fara.demo.controller.tutorControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tutor")
@PreAuthorize("hasRole('ROLE_TUTOR')")
public class Questions {



}
