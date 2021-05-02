package com.example.demo.controller;

import com.example.demo.entities.User;
import com.example.demo.helper.message;
import com.example.demo.repository.userRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

   @Autowired
    private userRepository userRepository;



    @GetMapping("/")
    public  String home(Model model){
        model.addAttribute("title", "Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public  String about(Model model){
        model.addAttribute("title", "Smart Contact Manager");
        return "about";
    }
    @GetMapping("/signup")
    public  String signUp(Model model){
        model.addAttribute("title", "Register-Smart Contact Manager");
        model.addAttribute("user",new User());
        return "signup";
    }
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public  String login(Model model){
        model.addAttribute("title", "Login - Smart Contact Manager");
        return "login";
    }

//    this handler for registering users
    // to get values from the browser
    @RequestMapping(value = "/do_register",method = RequestMethod.POST)
//    In arguments ModelAtribute is for get data from user as object, @Requestparam is to get agreement value whether its checked or not
// third argument we get to i.e Model to send data back to the browser, fourth argument we get to display http messages in the browser dynamically
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,@RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model, HttpSession session){

     try{


         if(result.hasErrors()){
             System.out.println("result validation"+result);
             model.addAttribute("user",user);
             return "signup";
         }
         if(!agreement)
         {

             System.out.println("you have not agreed terms and conditions");
             throw  new Exception("you have not select terms and conditions");
         }


         user.setRole("ROLE_USER");
         user.setEnabled(true);
         user.setImageURL("default.png");
         user.setPassword(passwordEncoder.encode(user.getPassword()));


         System.out.println("agreeement"+agreement);
         System.out.println("user"+user);
         User myresult=this.userRepository.save(user);

         model.addAttribute("user",user);//to send back to browser
         session.setAttribute("message",new message("successfully register","alert-success"));

     }catch (Exception e){
         e.printStackTrace();
         model.addAttribute("user",user);
         session.setAttribute("message", new message("someThing went wrong"+e.getMessage(),"alert-danger" ));//send messages back

     }
        return "signup";
    }


}

