package com.ssafy.ssafit.user.domain.repository;

import com.ssafy.ssafit.user.domain.model.User;

public interface UserDao {

    void insertUser(User user);

    User findUserByEmail(String email);

    boolean existsByEmail(String email);
}
