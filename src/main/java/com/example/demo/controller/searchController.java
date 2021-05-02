package com.example.demo.controller;

import com.example.demo.entities.Contact;
import com.example.demo.entities.User;
import com.example.demo.repository.contactRepository;
import com.example.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class searchController {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private contactRepository contactRepository;


    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal){

        User user=this.userRepository.getUserByUserName(principal.getName());
        System.out.println(query);
        List<Contact> contacts= this.contactRepository.findByNameContainingAndUser(query,user);
        return ResponseEntity.ok(contacts);
    }

}
