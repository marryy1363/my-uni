package com.fara.demo.domain;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.fara.demo.domain.Permissions.*;



@Getter
@AllArgsConstructor
public enum Roles {
    STUDENT(
            Sets.newHashSet()
    ),
    TUTOR(
            Sets.newHashSet()
    ),
    ADMIN(
            Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, TUTOR_READ, TUTOR_WRITE, COURSE_READ, COURSE_WRITE,ADMIN_READ,ADMIN_WRITE)
    ),
    ;

    private final Set<Permissions> permissions;


    Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream().map(
                applicationUserPermissions -> new SimpleGrantedAuthority(applicationUserPermissions.getPermission())
        ).collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
