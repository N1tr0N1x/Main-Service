package com.eler.MainService.Controllers;

import com.eler.MainService.Models.TeacherAccount;
import com.eler.MainService.Models.UserAccount;
import com.eler.MainService.Models.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    RestTemplate restTemplate;

    /*@GetMapping("/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("UserAccount",new UserAccount());
        modelAndView.setViewName("registration");
        return modelAndView;
    }*/
    
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
        restTemplate.postForObject("http://Teacher-Service/teacher/saveTeacher/",new TeacherAccount(user.getIdUser(),user.getEmail(),null,null), TeacherAccount.class);
    }

    @PostMapping("/login")
    public UserAccount login(@ModelAttribute("UserAccount") UserAccount user) {
        return restTemplate.getForObject("http://Authentication-Service/auth/login/" + user.getIdUser() + "/" + user.getPassword(),UserAccount.class);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/new_module")
    public ModelAndView new_module(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("Module",new Module());
        modelAndView.setViewName("new_module");

        return modelAndView;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("Module") Module module) {
        restTemplate.postForObject("http://Course-Service/mod/save",module, String.class);

        return new ModelAndView("redirect:http://localhost:8082/main/modules");
    }
    
    @RequestMapping("/modedit/{id}")
    public ModelAndView viewModEdit(@PathVariable(name = "id") int id){
        Module module = restTemplate.getForObject("http://Course-Service/mod/getModule/"+id, Module.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit_module");
        modelAndView.addObject("module", module);
        return modelAndView;
    }

    @RequestMapping("/moddelete/{id}")
    public ModelAndView DeleteModule(@PathVariable(name = "id") int id){

        restTemplate.getForObject("http://Course-Service/mod/delete/"+id, String.class);

        return new ModelAndView("redirect:http://localhost:8082/main/modules");
    }
    
    @GetMapping("/modules")
    public ModelAndView ShowModuleIndexPage(){
        String data = restTemplate.getForObject("http://Course-Service/mod/modules", String.class);

        ModelAndView modelAndView = new ModelAndView();
        final ObjectMapper objectMapper = new ObjectMapper();

        List<Module> mod = new ArrayList<Module>();
        try {
            mod = objectMapper.readValue(data, new TypeReference<List<Module>>() {
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // List<Module> mod = modules;
        modelAndView.setViewName("moduleIndex");
        modelAndView.addObject("listmodules", mod);
        return modelAndView;
    }

    ////////////////////////////////////////////////////////////////////////////

    
    @GetMapping("/new_teacher")
    public ModelAndView newTeacherView() { 
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new_teacher");
        modelAndView.addObject("teacher", new TeacherAccount());
        return modelAndView; 
    }

    @PostMapping("/save_teacher")
    public ModelAndView SaveTeacher(@ModelAttribute("teacher") TeacherAccount teacher) {
        UserAccount user = restTemplate.postForObject("http://Authentication-Service/auth/register",
                new UserAccount(teacher.getIdTeacher(), teacher.getEmail(), "0000"), UserAccount.class);
        teacher.setIdTeacher(user.getIdUser());
        restTemplate.postForObject("http://Teacher-Service/teacher/saveTeacher",teacher,String.class);

        return new ModelAndView("redirect:http://localhost:8082/main/teachers");
    }
    
    @GetMapping("/teachers")
    public ModelAndView ShowAllTeachers() {
        String data = restTemplate.getForObject("http://Teacher-Service/teacher/teachers", String.class);

        ModelAndView modelAndView = new ModelAndView();
        final ObjectMapper objectMapper = new ObjectMapper();

        List<TeacherAccount> teachers = new ArrayList<TeacherAccount>();
        try {
            teachers = objectMapper.readValue(data, new TypeReference<List<TeacherAccount>>() {
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        modelAndView.setViewName("list_teachers");
        modelAndView.addObject("listTeachers", teachers);
        return modelAndView;
    }

    @RequestMapping("/deleteTeacher/{id}")
    public ModelAndView DeleteTeacher(@PathVariable(name = "id") int idTeacher) {

        restTemplate.getForObject("http://Teacher-Service/teacher/delete/" + idTeacher, String.class);
        restTemplate.getForObject("http://Authentication-Service/auth/delete/" + idTeacher, String.class);

        return new ModelAndView("redirect:http://localhost:8082/main/teachers");
    }

    @RequestMapping("/editTeacher/{id}")
    public ModelAndView viewEditTeacher(@PathVariable(name = "id") int id) {
        TeacherAccount teacher = restTemplate.getForObject("http://Teacher-Service/teacher/getTeacher/" + id, TeacherAccount.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit_teacher");
        modelAndView.addObject("teacher", teacher);
        return modelAndView;
    }

}
