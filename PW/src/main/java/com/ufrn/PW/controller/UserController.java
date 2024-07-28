package com.ufrn.PW.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ufrn.PW.domain.Usuario;
import com.ufrn.PW.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/cadusuario")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Usuario());
        return "cadusuario";
    }

    @PostMapping("/cadusuario")
    public String registerUser(@RequestParam("username") String username, 
                               @RequestParam("password") String password, 
                               @RequestParam(value = "isAdmin", required = false) boolean isAdmin) {
        Usuario user = new Usuario();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setIsAdmin(isAdmin);

        userService.create(user);
        return "redirect:/login";
    }
}
