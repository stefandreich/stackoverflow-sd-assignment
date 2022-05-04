package com.sd.stackoverflow.controller;

import com.sd.stackoverflow.dto.UserDTO;
import com.sd.stackoverflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    @PostMapping("/users/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDTO user) {
        UserDTO addedUser = userService.addUser(user);

        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/getAllUsers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();

        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/getUser/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable(name = "id") Long id) {
        UserDTO user = userService.getUser(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/getUserByUsername", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        UserDTO user = userService.getUserByUsername(username);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO user) {
        UserDTO updatedUser = userService.updateUser(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/deleteUser/{id}")
    public void deleteUserById(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
        UserDTO user = userService.login(username, password);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body("Username or password invalid");
        }
    }
}
