package com.proxyseller.testtask.security

import com.proxyseller.testtask.data.SwitterUser
import com.proxyseller.testtask.data.SwitterUserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/register")
class RegistrationController {

    SwitterUserRepository userRepository;
    PasswordEncoder passwordEncoder;

    RegistrationController(SwitterUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository
        this.passwordEncoder = passwordEncoder
    }

    @GetMapping
    String registerForm() {
        return "registration"
    }

    @PostMapping
    String processRegistration(RegistrationForm form) {
        SwitterUser user = form.toUser(passwordEncoder)
        userRepository.save(user)
        return "redirect:/login"
    }
}
