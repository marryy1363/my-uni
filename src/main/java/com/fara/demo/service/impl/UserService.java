package com.fara.demo.service.impl;

import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import com.fara.demo.domain.enums.UserFields;
import com.fara.demo.repository.UserRepository;
import com.fara.demo.repository.UserSpecifications;
import com.fara.demo.service.UserServiceApi;
import com.fara.demo.service.dto.UserSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
//@AllArgsConstructor
public class UserService implements UserServiceApi {


    final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preRegister(String firstName, String lastName, String username, String password, String email, Roles role) throws Exception {
        if (userRepository.existsByUsername(username)) throw new Exception(" duplicate user name");
        if (userRepository.existsByEmail(username)) throw new Exception(" duplicate email");
        Roles roles = null;
        if (password.length() < 3) throw new Exception(" short password ");
        if (username.length() < 3) throw new Exception(" short user name ");
        if (firstName.length() < 3) throw new Exception(" short first name ");
        if (lastName.length() < 3) throw new Exception(" short last name ");
        password = new BCryptPasswordEncoder().encode(password);
        roles = role;
        System.out.println(role.getPermissions());
        User user = new User(
                firstName, lastName, username, password, email, roles, false
        );
        userRepository.save(user);
        return true;
    }


    @Override
    public List<User> findAll() {
        return ((List<User>) userRepository.findAll());
    }

    @Override
    public User editUser(Long id, String firstName, String lastName, String username, String password, String email, Roles role, boolean enable) throws Exception {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();

            if (!Objects.equals(user.getUsername(), username) && userRepository.existsByUsername(username))
                throw new Exception(" duplicate user name");
            if (!Objects.equals(user.getEmail(), email) && userRepository.existsByEmail(username))
                throw new Exception(" duplicate email");

            if (password != null && !password.isEmpty() && !password.isBlank()) {
                if (password.length() < 3) throw new Exception(" short password ");
                user.setPassword(new BCryptPasswordEncoder().encode(password));
            }

            if (username.length() < 3) throw new Exception(" short user name ");
            user.setUsername(username);
            if (firstName.length() < 3) throw new Exception(" short first name ");
            user.setFirstName(firstName);
            if (lastName.length() < 3) throw new Exception(" short last name ");
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRoles(role);
            user.setEnable(enable);
            return userRepository.save(user);
        } else throw new Exception("user not found");
    }

    @Override
    public List<User> searchWithFieldsAndRoleAndStatusEnablity(UserSearchDto userSearchDto) {

        return userRepository.findAll((root, query, criteriaBuilder) -> {
            // package javax.persistence.criteria;
            List<Predicate> predicates = new ArrayList<>();
            if (userSearchDto.getRoles() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("roles"), userSearchDto.getRoles())
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }


    @Override
    public List<User> searchWithFieldsAndRoleAndStatusEnablity(Optional<UserFields> fields,
                                                               Optional<String> query,
                                                               Optional<Roles> role,
                                                               Optional<Boolean> enable) {
        List<User> aa = null;
        try {
            Specification<User> search = UserSpecifications.attrLike(fields, query);
            Specification<User> withRole = UserSpecifications.hasRole(role);
            Specification<User> active = UserSpecifications.isActive(enable);
            if (search != null && withRole != null && active != null)
                aa = userRepository.findAll(
                        Specification.where(
                                search
                                        .and(
                                                withRole
                                        ).and(
                                                active
                                        )

                        ));
            if (search == null && withRole != null && active != null)
                aa = userRepository.findAll(
                        Specification.where(

                                withRole
                                        .and(
                                                active
                                        )

                        ));
            if (search == null && withRole == null && active != null)
                aa = userRepository.findAll(
                        Specification.where(
                                active
                        ));
            if (search == null && withRole != null && active == null)
                aa = userRepository.findAll(
                        Specification.where(
                                withRole
                        ));
            if (search != null && withRole == null && active == null)
                aa = userRepository.findAll(
                        Specification.where(
                                search
                        ));
            if (search != null && withRole != null && active == null)
                aa = userRepository.findAll(
                        Specification.where(
                                search
                                        .and(
                                                withRole
                                        )
                        ));
            if (search == null && withRole == null && active == null)
                aa = userRepository.findAll();

        } catch (Exception e) {
            e.printStackTrace();

        }

        return aa;
    }


}
