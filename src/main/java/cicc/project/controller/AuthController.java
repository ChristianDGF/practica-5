package cicc.project.controller;

import cicc.project.entities.User;
import cicc.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login"; 
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/manage/mocks"; 
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        if(userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error=exists";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setActive(true);
        newUser.setRoles(Set.of("ROLE_USER"));
        
        userRepository.save(newUser);
        return "redirect:/login?registered=true";
    }
}