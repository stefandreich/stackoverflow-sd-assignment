package com.sd.stackoverflow.controller;

import com.sd.stackoverflow.dto.UserDTO;
import com.sd.stackoverflow.model.User;
import com.sd.stackoverflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        User addedUser = userService.addUser(user);

        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/getAllUsers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();

        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/getUser/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable(name = "id") Long id) {
        User user = userService.getUser(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/getUserByUsername", method = RequestMethod.GET)
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/deleteUser/{id}")
    public void deleteUserById(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
    }
}
