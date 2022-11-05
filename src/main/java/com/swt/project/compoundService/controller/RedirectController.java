package com.swt.project.compoundService.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RedirectController {

    //Redirect to API documentation
    @RequestMapping("/")
    public RedirectView redirectWithUsingRedirectView(
            RedirectAttributes attributes) {
        return new RedirectView("http://lr-server.online:8081/swagger-ui/index.html");
    }
}
