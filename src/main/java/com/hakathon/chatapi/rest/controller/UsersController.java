package com.hakathon.chatapi.rest.controller;

import com.hakathon.chatapi.model.UserExtra;
import com.hakathon.chatapi.repository.UserExtraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController {
    private final UserExtraRepository userExtraRepository;

/*    @GetMapping("/registration/{userName}")
    public ResponseEntity<Void> register(@PathVariable String userName) {
        System.out.println("handling register user request: " + userName);
        try {
            UserStorage.getInstance().setUser(userName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }*/

    @GetMapping("/fetchAllUsers")
    public List<String> fetchAll() {
        List<String> names = new ArrayList<>();
        for(UserExtra e : userExtraRepository.findAll())
            names.add(e.getUsername());
        return names;
    }
}
