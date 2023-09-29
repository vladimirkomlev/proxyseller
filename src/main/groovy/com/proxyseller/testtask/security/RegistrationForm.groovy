package com.proxyseller.testtask.security

import com.proxyseller.testtask.data.SwitterUser
import org.springframework.security.crypto.password.PasswordEncoder

class RegistrationForm {

    String username;
    String password;

    SwitterUser toUser(PasswordEncoder passwordEncoder) {
        return new SwitterUser(username, passwordEncoder.encode(password));
    }
}
