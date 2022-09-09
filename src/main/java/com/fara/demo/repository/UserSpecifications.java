package com.fara.demo.repository;//package com.fara.myunisystem.repository;


import com.fara.demo.domain.Roles;
import com.fara.demo.domain.User;
import com.fara.demo.domain.enums.UserFields;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;


public class UserSpecifications {
    public static Specification<User> attrLike(Optional<UserFields> fields, Optional<String> val) {

        if (val.isEmpty())
            return null;
        if (fields.isEmpty())
            return null;

        UserFields finalField = fields.get();
        String finalValue = val.get();
        if(finalValue.isEmpty()||finalValue.isBlank()) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get(finalField.getAttr()), "%" + finalValue + "%"
        );
    }

    public static Specification<User> isActive(Optional<Boolean> enablity) {
        if (enablity.isEmpty())return null;
        boolean enable = enablity.get();
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("enable"), enable
            );
    }

    public static Specification<User> hasRole(Optional<Roles> roles) {
        if (roles.isEmpty()) return null;
        Roles role = roles.get();
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get("roles"), role
        );
    }
}
