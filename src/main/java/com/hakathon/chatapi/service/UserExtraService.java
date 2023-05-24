package com.hakathon.chatapi.service;

import com.hakathon.chatapi.model.UserExtra;

import java.util.Optional;

public interface UserExtraService {

    UserExtra validateAndGetUserExtra(String username);

    Optional<UserExtra> getUserExtra(String username);

    UserExtra saveUserExtra(UserExtra userExtra);
}
