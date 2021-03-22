package com.eler.MainService.Controllers;

import com.eler.MainService.Models.TeacherAccount;
import com.eler.MainService.Models.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("UserAccount",new UserAccount());
        modelAndView.setViewName("registration");

        return modelAndView;
    }
    
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("UserAccount", new UserAccount());
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
    public void register(@ModelAttribute("UserAccount") UserAccount user) {
        user = restTemplate.getForObject("http://Authentication-Service/auth/register/"+user.getEmail()+"/"+user.getPassword(), UserAccount.class);
        //UserAccount newUser = restTemplate.getForObject("http://Authentication-Service/auth/getUser/"+user.getEmail(), UserAccount.class);
        restTemplate.getForObject("http://Teacher-Service/teacher/registerEmptyTeacher/"+user.getIdUser(), TeacherAccount.class);
    }

    @PostMapping("/login")
    public UserAccount login(@ModelAttribute("UserAccount") UserAccount user) {
        return restTemplate.getForObject("http://Authentication-Service/auth/login/" + user.getIdUser() + "/" + user.getPassword(),UserAccount.class);
    }

}
