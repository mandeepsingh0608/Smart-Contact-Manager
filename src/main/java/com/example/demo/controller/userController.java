package com.example.demo.controller;


import com.example.demo.entities.Contact;
import com.example.demo.entities.User;
import com.example.demo.helper.message;
import com.example.demo.repository.contactRepository;
import com.example.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class userController {
    @Autowired
    userRepository userRepository;
    @Autowired
    private contactRepository contactRepository;
// comman method to use data in all controllers
    @ModelAttribute
    public void addCommonData( Model model, Principal principal){
        String username=principal.getName();

        //get the user using username(email)
        System.out.println("username : "+username);


        User user=userRepository.getUserByUserName(username);
        System.out.println(user.getRole());
        model.addAttribute("user",user);

    }
//dashboad home
   @GetMapping("/index")
    public String dashboard(Model model, Principal principal){

        return "normal/dashboard";
    }

    @GetMapping("/addContact")
    public String openAddContactform(Model model){
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new Contact());
        return "normal/addContactForm";
    }


    @GetMapping("/fail")
        public String loginfaild(){
           return ("loginFailed") ;
        }

    // processing at contact form
    @PostMapping("/process-contact")
    public String processConatct(@ModelAttribute("contact") Contact contact,
                                 @RequestParam("profileimage") MultipartFile file,
                                 Principal principal, HttpSession session){

        try{
            String name=principal.getName();
            User user=this.userRepository.getUserByUserName(name);
            if(file.isEmpty()){
                System.out.println("file is empty");
                contact.setImage("userimg.png");

            }else{
              //store the file to the folder and update the name to contact
            contact.setImage(file.getOriginalFilename());
            String savefile=new ClassPathResource("static/img/").getFile().getAbsolutePath();
                //Path path=Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());

                Files.copy(file.getInputStream(), Paths.get(savefile+File.separator+file.getOriginalFilename()));
                System.out.println("image uploaded sucessfully");
            }
            contact.setUser(user);

            user.getContacts().add(contact);

            this.userRepository.save(user);
            System.out.println("Data Conatcts= "+contact);
            //send success message
              session.setAttribute("message", new message("Your contact add successfully..","success"));

            System.out.println("Added to database");
        }catch (Exception e){
            System.out.println("error "+e.getMessage());
            e.printStackTrace();
            //send error message
            session.setAttribute("message", new message("problem while adding Contact !!","danger"));
        }


        return "normal/addContactForm";
    }


    //show contacts handler
    //showing 2 contact per page
    //current page =0 { page}
    @GetMapping("/showcontacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal){
        m.addAttribute("title", "Contacts List");


        // sending contact list to browser from database
      String userName=principal.getName();
      User user=this.userRepository.getUserByUserName(userName);
       //current page
        // conatct per page
        Pageable pageable=PageRequest.of(page,2);
      Page<Contact> contacts= this.contactRepository.findContactByUser(user.getId(),pageable);

       m.addAttribute("contacts",contacts);
       m.addAttribute("currentpage",page);
       m.addAttribute("totalpages", contacts.getTotalPages());
        return "normal/showContacts";
    }

// showing particular contact detail
    @GetMapping("/{contactId}/contact")
    public String showContactDetail(@PathVariable("contactId") Integer contactId, Model model,Principal principal){

        System.out.println("contactID"+contactId);
        Optional<Contact> contactOptional=this.contactRepository.findById(contactId);
        Contact contact=contactOptional.get();

        String username=principal.getName();
        User user=this.userRepository.getUserByUserName(username);
        if(user.getId()==contact.getUser().getId()){
            model.addAttribute("contact",contact);

        }



        return "normal/contact_detail";
    }
//deleting contact handeler

    @GetMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") Integer contactId, Principal principal, HttpSession session) throws IOException {

        String username=principal.getName();
        User user=this.userRepository.getUserByUserName(username);
        Contact contact=this.contactRepository.findById(contactId).get();

        Path path=Paths.get("D:\\java projects\\smartcontact\\target\\classes\\static\\img\\"+contact.getImage());
        if(!contact.getImage().equals("userimg.png")){
            Files.delete(path);
        }

        if(user.getId()==contact.getUser().getId() ){

            this.contactRepository.delete(contact);
        session.setAttribute("message", new message("contact deleted sucessfully","success"));
        }



        return "redirect:/user/showcontacts/0";

}


//update form handler

    @PostMapping("/update-contact/{contactId}")
    public String updateform(@PathVariable("contactId") Integer contactId,Model model){

      Contact contact=this.contactRepository.findById(contactId).get();

        model.addAttribute("title","update contact");
        model.addAttribute("contact",contact);


        return "normal/update_form";
    }


    @PostMapping("/process-update/{contactId}")
    public String updateHandler(@ModelAttribute Contact contact,@PathVariable("contactId") Integer contactId,@RequestParam("profileimage") MultipartFile file,Model model,HttpSession session,Principal principal){
       try{
           // old contact detail

         Contact oldContact=  this.contactRepository.findById(contactId).get();
           System.out.println("Contact = "+contact.toString());
           System.out.println("ContactID"+contact.getContactId());

           //image
           if(file.isEmpty()){
               //file work
               //rewrite
               //delete old photo

               //update new photo
               contact.setImage(oldContact.getImage());

           }else {
               String savefile=new ClassPathResource("static/img/").getFile().getAbsolutePath();
               //Path path=Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());

               Files.copy(file.getInputStream(), Paths.get(savefile+File.separator+file.getOriginalFilename()));
               contact.setImage(file.getOriginalFilename());

           }

           User user=this.userRepository.getUserByUserName(principal.getName());
           contact.setUser(user);

           this.contactRepository.save(contact);


       }catch (Exception e){
           e.printStackTrace();
       }




        System.out.println("contact Name"+contact.getName());
        System.out.println("contact ID"+contact.getContactId());
        return "redirect:/user/"+contact.getContactId()+"/contact";
    }

@GetMapping("/profile")
public String yourprofile(Model model){

        return "normal/profile";
}


}
