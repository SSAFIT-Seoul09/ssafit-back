package com.ssafy.ssafit.user.domain.repository;

import com.ssafy.ssafit.user.domain.model.User;

public interface UserDao {

    int insertUser(User user);

    User findUserByEmail(String email);

    boolean existsByEmail(String email);

    User findUserById(Long userId);

    int updateUser(User user);

    int deleteUser(Long userId);
}
