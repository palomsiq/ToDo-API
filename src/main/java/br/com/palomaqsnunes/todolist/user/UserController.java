package br.com.palomaqsnunes.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;




@RestController
@RequestMapping("/users")

public class UserController {

    
    @Autowired
    private IUserRepository userRepository;

    //http://localhost:8080/users/
    @PostMapping("/")
    public ResponseEntity<String> create(@RequestBody UserModel userModel){

       var check =  this.userRepository.findByUsername(userModel.getUsername());

       if (check != null) {
           return ResponseEntity.status(400).body("User already exists");
       }
       var passwordHash= BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
       
       userModel.setPassword(passwordHash);

       this.userRepository.save(userModel);

       return ResponseEntity.status(200).body("Created");
    }

}
