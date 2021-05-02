package com.example.demo.config;

import com.example.demo.entities.User;
import com.example.demo.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserdetailsServiceImpl implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       // fetching user from database
     User user=userRepository.getUserByUserName(username);

     if(user==null){
         throw new UsernameNotFoundException("could not find user");
     }

 CustomUserDetails customUserDetails=new CustomUserDetails(user);
        return customUserDetails;
    }





}
