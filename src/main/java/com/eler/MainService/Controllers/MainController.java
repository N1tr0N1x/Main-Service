package com.eler.MainService.Controllers;

import com.eler.MainService.Models.TeacherAccount;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
