package com.eler.MainService.Controllers;

import org.springframework.core.io.Resource;
import com.eler.MainService.Models.TeacherAccount;
import com.eler.MainService.Models.UserAccount;
import com.eler.MainService.Models.MyFile;
import com.eler.MainService.Models.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    RestTemplate restTemplate;

    boolean loggedIn = false;
    boolean isAdminLoggedIn = false;
    int idUser;

    /*@GetMapping("/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("testView");
        return modelAndView;
    }*/

    @GetMapping("/home")
    public ModelAndView home(){
        String data = restTemplate.getForObject("http://Course-Service/mod/modules", String.class);

        ModelAndView modelAndView = new ModelAndView();
        final ObjectMapper objectMapper = new ObjectMapper();

        List<Module> modules = new ArrayList<Module>();
        try {
            modules = objectMapper.readValue(data, new TypeReference<List<Module>>() {
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // List<Module> mod = modules;
        modelAndView.setViewName("list_module_check");
        modelAndView.addObject("modules", modules);
        return modelAndView;
    }

    @RequestMapping("/showModule/{id}")
    public ModelAndView showModule(@PathVariable(name = "id") int id) {
        Module module = restTemplate.getForObject("http://Course-Service/mod/getModule/" + id, Module.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("module_view");
        modelAndView.addObject("module", module);

        String filesData = restTemplate.getForObject("http://Course-Service/mod/get_files_by_module_id/" + module.getIdModule(), String.class);

        final ObjectMapper objectMapper = new ObjectMapper();

        List<MyFile> files = new ArrayList<MyFile>();
        try {
            files = objectMapper.readValue(filesData, new TypeReference<List<MyFile>>() {
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("files", files);

        return modelAndView;
    }

    @RequestMapping("/showModuleTeacher/{idModule}/{idTeacher}")
    public ModelAndView showModuleTeacher(@PathVariable(name = "idModule") int idModule,
            @PathVariable(name = "idTeacher") int idTeacher) {

        if (!loggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("module_teacher_view");

        Module module = restTemplate.getForObject("http://Course-Service/mod/getModule/" + idModule, Module.class);
        modelAndView.addObject("module", module);

        TeacherAccount teacher = restTemplate.getForObject("http://Teacher-Service/teacher/getTeacher/" + idTeacher,TeacherAccount.class);
        modelAndView.addObject("teacher", teacher);


        String filesData = restTemplate.getForObject("http://Course-Service/mod/get_files_by_module_id/"+module.getIdModule(), String.class);

        final ObjectMapper objectMapper = new ObjectMapper();

        List<MyFile> files = new ArrayList<MyFile>();
        try {
            files = objectMapper.readValue(filesData, new TypeReference<List<MyFile>>() {
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("files", files);

        //modelAndView.addObject("file","http://localhost:8088/store/files/158001540_729054664475750_717103923840496317_n.jpg");
        return modelAndView;
    }

    //@GetMapping("/file/{name}")
    //public Resource serveFile(@PathVariable("name") String name){
        //ResponseEntity<Resource> r = restTemplate.getForEntity("http://File-Storage-Service/store/files/"+name, Resource.class);
        //return r.getBody();
    //}
    
    @GetMapping("/file/{name}")
    public ModelAndView downloadFile(@PathVariable("name") String name) {

        return new ModelAndView("redirect:http://localhost:8082/store/files/"+name);
    }

    @GetMapping("/teacher_profile/{idTeacher}")
    public ModelAndView showTeacherProfile(@PathVariable(name = "idTeacher") int idTeacher){
        if(!loggedIn){
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("teacher_view");


        TeacherAccount teacher = restTemplate.getForObject("http://Teacher-Service/teacher/getTeacher/" + idTeacher,TeacherAccount.class);
        modelAndView.addObject("teacher", teacher);

        final ObjectMapper objectMapper = new ObjectMapper();

        String dataCours = restTemplate.getForObject("http://Course-Service/mod/modules/"+teacher.getEmail()+"/cours", String.class);
        List<Module> modulesCours = new ArrayList<Module>();
        try {
            modulesCours = objectMapper.readValue(dataCours, new TypeReference<List<Module>>() {
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("modulesCours", modulesCours);


        String dataTd = restTemplate.getForObject("http://Course-Service/mod/modules/" + teacher.getEmail() + "/td",
                String.class);
        List<Module> modulesTd = new ArrayList<Module>();
        try {
            modulesTd = objectMapper.readValue(dataTd, new TypeReference<List<Module>>() {
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("modulesTd", modulesTd);



        String dataTp = restTemplate.getForObject("http://Course-Service/mod/modules/" + teacher.getEmail() + "/tp",
                String.class);
        List<Module> modulesTp = new ArrayList<Module>();
        try {
            modulesTp = objectMapper.readValue(dataTp, new TypeReference<List<Module>>() {
            });
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelAndView.addObject("modulesTp", modulesTp);

        return modelAndView;
    }

    //////////////////////////////////////////////////////////////////
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("UserAccount", new UserAccount());
        modelAndView.setViewName("login");
        return modelAndView;
    }
    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("UserAccount") UserAccount user) {
        user = restTemplate.postForObject("http://Authentication-Service/auth/login", user,UserAccount.class);
        if(user!=null){
            loggedIn = true;
            idUser = user.getIdUser();
        }else{
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        if(user.getIdUser()==0){
            //ADMIN
            isAdminLoggedIn = true;
            return new ModelAndView("redirect:http://localhost:8082/main/admin_home/");
        }else{
            return new ModelAndView("redirect:http://localhost:8082/main/teacher_profile/" + user.getIdUser());
        }
        
    }
    
    @GetMapping("/logout")
    public ModelAndView logout() {
        loggedIn = false;
        isAdminLoggedIn = false;
        idUser = 0;
        return new ModelAndView("redirect:http://localhost:8082/main/login");
    }
    @GetMapping("/admin_home")
    public ModelAndView adminHome() {
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin_dashboard");
        return modelAndView;
    }

    
    /////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/new_module")
    public ModelAndView new_module(){
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("Module",new Module());
        modelAndView.setViewName("new_module");

        return modelAndView;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute("Module") Module module) {
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        restTemplate.postForObject("http://Course-Service/mod/save",module, String.class);

        return new ModelAndView("redirect:http://localhost:8082/main/modules");
    }
    
    @RequestMapping("/modedit/{id}")
    public ModelAndView viewModEdit(@PathVariable(name = "id") int id){
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        Module module = restTemplate.getForObject("http://Course-Service/mod/getModule/"+id, Module.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit_module");
        modelAndView.addObject("module", module);
        return modelAndView;
    }

    @RequestMapping("/moddelete/{id}")
    public ModelAndView DeleteModule(@PathVariable(name = "id") int id){

        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }

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
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new_teacher");
        modelAndView.addObject("teacher", new TeacherAccount());
        return modelAndView; 
    }

    @PostMapping("/save_teacher")
    public ModelAndView SaveTeacher(@ModelAttribute("teacher") TeacherAccount teacher) {
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        UserAccount user = restTemplate.postForObject("http://Authentication-Service/auth/register",
                new UserAccount(teacher.getIdTeacher(), teacher.getEmail(), "0000"), UserAccount.class);
        teacher.setIdTeacher(user.getIdUser());
        restTemplate.postForObject("http://Teacher-Service/teacher/saveTeacher",teacher,String.class);

        return new ModelAndView("redirect:http://localhost:8082/main/teachers");
    }
    
    @GetMapping("/teachers")
    public ModelAndView ShowAllTeachers() {
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
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
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        restTemplate.getForObject("http://Teacher-Service/teacher/delete/" + idTeacher, String.class);
        restTemplate.getForObject("http://Authentication-Service/auth/delete/" + idTeacher, String.class);

        return new ModelAndView("redirect:http://localhost:8082/main/teachers");
    }

    @RequestMapping("/editTeacher/{id}")
    public ModelAndView viewEditTeacher(@PathVariable(name = "id") int id) {
        if (!isAdminLoggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        TeacherAccount teacher = restTemplate.getForObject("http://Teacher-Service/teacher/getTeacher/" + id, TeacherAccount.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit_teacher");
        modelAndView.addObject("teacher", teacher);
        return modelAndView;
    }
    @GetMapping("/new_file/{email}/{idModule}")
    public ModelAndView saveFileForm(@PathVariable(name = "email") String email, @PathVariable(name = "idModule") int idModule) {
        if (!loggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        MyFile myFile = new MyFile();
        myFile.setModuleID(new Long(idModule));
        myFile.setTeacherEmail(email);
        //TeacherAccount teacher = restTemplate.getForObject("http://Teacher-Service/teacher/getTeacher/" + id, TeacherAccount.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new_file");
        modelAndView.addObject("myFile", myFile);
        modelAndView.addObject("idModule", idModule);
        return modelAndView;
    }
    @PostMapping("/save_file/{idModule}")
    public ModelAndView SaveFile(@RequestParam("data") MultipartFile data,@ModelAttribute("myFile") MyFile myFile,@PathVariable("idModule") long idModule ) throws IOException {
        if (!loggedIn) {
            return new ModelAndView("redirect:http://localhost:8082/main/login");
        }
        String url = "http://File-Storage-Service/store/storefile";
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(convert(data)));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        String link = response.getBody();
        myFile.setLink(link);
        myFile.setModuleID(idModule);

        restTemplate.postForObject("http://Course-Service/mod/save_file", myFile, String.class);

        return new ModelAndView("redirect:http://localhost:8082/main/showModuleTeacher/" + idModule + "/" + idUser);
    }
    
    public static File convert(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }

}
