package com.journal.journalApp.controller;

import com.journal.journalApp.entity.User;
import com.journal.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAll();
    }

    @PostMapping
    public void createUser(@RequestBody User user){
        userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable ObjectId id){
        userService.deleteById(id);
    }

    @PutMapping("/{userName}")
    public void updateUser(@RequestBody User user, @PathVariable String userName){
        User existingUser = userService.findByUserName(userName );
        if(existingUser != null){
            existingUser.setUserName(user.getUserName());
            existingUser.setPassword(user.getPassword());
            userService.saveUser(existingUser);
        }
    }
}
