package com.fara.demo.service.dto;


import com.fara.demo.domain.Roles;
import com.fara.demo.domain.enums.UserFields;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserSearchDto {
    private Roles roles;
    private Boolean enable;

    private UserFields key;
    private String value;
}
