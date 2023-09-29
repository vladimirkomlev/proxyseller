package com.proxyseller.testtask.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/page")
class UserPageController {

    @GetMapping
    String page() {
        return "page"
    }
}
