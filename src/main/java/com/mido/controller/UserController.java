package com.mido.controller;

import com.mido.model.Message;
import com.mido.model.User;
import com.mido.repository.IUserService;
import com.mido.service.ChatService;
import com.mido.utility.AppResponse;
import com.mido.utility.AuthData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final ChatService chatService;

    @GetMapping
    public String getUsers(Model model){
        model.addAttribute("title", "users");
        // model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("users", chatService.findAllByUserId(AuthData.getCurrentUserId()));
        return "users";
    }







    @PostMapping("/send")
    public ResponseEntity<Object> sendMessage(@RequestBody Message message){
        return AppResponse.generateResponse("Success", HttpStatus.OK, chatService.saveMessage(message), true);
    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Optional<User> user = userService.findById(id);
        model.addAttribute("user", user.get());
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user){
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
        return "redirect:/users?update_success";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/users?delete_success";
    }
}
