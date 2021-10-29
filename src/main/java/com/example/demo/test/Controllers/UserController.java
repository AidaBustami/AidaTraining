package com.example.demo.test.Controllers;
import com.example.demo.test.Models.User;
import com.example.demo.test.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userservice;

    @PostMapping("/create_user")
    public User createUser()
    {
        User user = new User();
        userservice.save(user);
        return user ;
   }
}
