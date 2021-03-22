package com.eler.MainService.Controllers;

import com.eler.MainService.Models.TeacherAccount;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/main")
public class MainController {

    @GetMapping("/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("TeacherAccount",new TeacherAccount());
        modelAndView.setViewName("registration");

        return modelAndView;
    }
    
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("TeacherAccount", new TeacherAccount());
        modelAndView.setViewName("login");

        return modelAndView;
    }
    /*@GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("testView");
        return modelAndView;
    }*/

    @PostMapping("/register")
    public TeacherAccount register(@ModelAttribute("TeacherAccount") TeacherAccount teacher) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8083/auth/register/"+teacher.getEmail()+"/"+teacher.getPassword(), TeacherAccount.class);
    }

    @PostMapping("/login")
    public TeacherAccount login(@ModelAttribute("TeacherAccount") TeacherAccount teacher) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8083/auth/login/" + teacher.getEmail() + "/" + teacher.getPassword(),TeacherAccount.class);
    }

}
