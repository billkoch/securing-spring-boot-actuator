package io.billkoch.examples.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class GreetingController {

    @RequestMapping(method = RequestMethod.GET)
    public String greet(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "greeting";
    }
}
