package com.mido.controller;

import com.mido.service.ActiveUserStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;


@Log4j2
@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    ActiveUserStore activeUserStore;
    @GetMapping
    public String homePage(){
        return "home";
    }
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("title", "Login");
        return "auth/login";
    }
    @GetMapping("/error")
    public String error(){
        return "error";
    }










    @GetMapping("/loggedUsers")
    public String getLoggedUsers(Locale locale, Model model) {
        model.addAttribute("users", activeUserStore.getUsers());
        log.info("getUsername ################### is >> " + activeUserStore.getUsers().get(0));
        return "logged_users";
    }

}
