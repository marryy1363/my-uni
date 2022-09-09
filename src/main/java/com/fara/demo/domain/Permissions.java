package com.fara.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Permissions {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    TUTOR_READ("tutor:read"),
    TUTOR_WRITE("tutor:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    private final String permission;

}
