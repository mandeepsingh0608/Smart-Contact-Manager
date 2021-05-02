package com.example.demo.repository;

import com.example.demo.entities.Contact;
import com.example.demo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface contactRepository extends JpaRepository<Contact, Integer> {
//pagination

  @Query("from Contact as c where c.user.id =:userId")
  public Page<Contact> findContactByUser(@Param("userId")int userId, Pageable pageable);

  public List<Contact> findByNameContainingAndUser(String name, User user);
}
