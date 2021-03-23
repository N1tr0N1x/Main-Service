package com.eler.MainService.Controllers;

import com.eler.MainService.Models.TeacherAccount;
import com.eler.MainService.Models.UserAccount;
import com.eler.MainService.Models.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
     @PostMapping("/save")
    public String save(@ModelAttribute("Module") Module module) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://localhost:8085/mod/save/",module, String.class);
        
    }


    @JsonIgnoreProperties
    @RequestMapping(value = "/modindex", method = RequestMethod.POST,headers = "Accept=application/json")
    @ResponseBody
    //public String viewHomePage(Model model, @RequestBody List<Module> modules) {
    public ModelAndView viewIndexModule(Model model,@RequestBody String modules) {
        ModelAndView modelAndView = new ModelAndView();
        final ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<Module> mod = new ArrayList<Module>();
        try{
             mod = objectMapper.readValue(modules, new TypeReference<List<Module>>(){});
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //List<Module> mod = modules;
        modelAndView.setViewName("moduleindex");
        modelAndView.addObject("listmodules", mod);
        return modelAndView;
        
    }   

}
