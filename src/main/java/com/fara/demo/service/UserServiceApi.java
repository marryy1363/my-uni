package com.fara.demo.service;


import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import com.fara.demo.domain.enums.UserFields;
import com.fara.demo.service.dto.UserSearchDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserServiceApi {
     boolean preRegister(String firstName, String lastName, String username, String password, String email, Roles role) throws Exception;

    List<User> findAll();

    User editUser(Long id, String firstName, String lastName, String username, String password, String email, Roles role, boolean enable) throws Exception;

    List<User> searchWithFieldsAndRoleAndStatusEnablity(UserSearchDto userSearchDto);

    List<User> searchWithFieldsAndRoleAndStatusEnablity(Optional<UserFields> fields,
                                                        Optional<String> query,
                                                        Optional<Roles> role,

                                                        Optional<Boolean> enable);
}
