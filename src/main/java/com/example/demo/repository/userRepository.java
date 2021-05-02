package com.example.demo.repository;



import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<User,Integer> {

    @Query("select u from User u WHERE u.email =:email")
    public User getUserByUserName(@Param("email") String email);


}
