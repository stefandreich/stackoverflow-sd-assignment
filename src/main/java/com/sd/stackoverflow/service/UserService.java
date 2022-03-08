package com.sd.stackoverflow.service;

import com.sd.stackoverflow.dto.UserDTO;
import com.sd.stackoverflow.model.User;
import com.sd.stackoverflow.repository.IUserRepository;
import com.sd.stackoverflow.service.customexceptions.ConflictException;
import com.sd.stackoverflow.service.customexceptions.InvalidDataException;
import com.sd.stackoverflow.service.customexceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USERNAME_VALIDATION_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+";
    private static final int MIN_PASSWORD_LENGTH = 7;

    private final IUserRepository iUserRepository;

    public List<UserDTO> getAllUsers() throws ResourceNotFoundException {
        List<UserDTO> userList = iUserRepository.findAll().stream()
                .map(UserDTO::fromUserEntity)
                .collect(Collectors.toList());

        if (userList.isEmpty()) {
            throw new ResourceNotFoundException("No users found in database!");
        }

        return userList;
    }

    public User getUser(Long id) {
        Optional<User> foundUser = iUserRepository.findById(id);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("User with id " + id + " not found.");
        }

        return foundUser.get();
    }

    public User getUserByUsername(String username) throws ResourceNotFoundException {
        User foundUser = iUserRepository.findUserByUsername(username);

        if (foundUser == null) {
            throw new ResourceNotFoundException("Users with username " + username + " not found");
        }

        return foundUser;
    }

    /*public User addUser(User givenUser) throws ResourceNotFoundException {
        User foundUser = iUserRepository.findUserByUsername(givenUser.getUsername());
        if (foundUser != null) {
            throw new ResourceNotFoundException("Username " + givenUser.getUsername() + " already found. Cannot perform create operation.");
        }

        givenUser.setUsername(givenUser.getUsername());
        givenUser.setEmail(givenUser.getEmail());
        givenUser.setPassword(givenUser.getPassword());

        return iUserRepository.save(givenUser);
    }*/

    public User addUser(User givenUser) throws InvalidDataException {
        if (!validateUsername(givenUser.getUsername())) {
            throw new InvalidDataException("Invalid username.");
        }

        String resultPasswordValidation = validatePassword(givenUser.getPassword());
        if (!resultPasswordValidation.equals("Password OK")) {
            throw new InvalidDataException(resultPasswordValidation);
        }

        User foundUser = iUserRepository.findUserByUsername(givenUser.getUsername());

        if (foundUser != null) {
            throw new ConflictException("Username " + givenUser.getUsername() + " already found. Cannot perform create operation.");
        }

        return iUserRepository.save(givenUser);
    }

    public User updateUser(User user) throws ResourceNotFoundException {
        User initialUser = iUserRepository.findById(user.getUserId()).orElse(null);

        if (initialUser == null) {
            throw new ResourceNotFoundException("User with id " + user.getUserId() + " cannot be found.");
        } else {
            return iUserRepository.save(user);
        }
    }

    public void deleteUser(Long id) throws ResourceNotFoundException {
        Optional<User> foundUser = iUserRepository.findById(id);

        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("Given user was not found. Delete operation could not be performed.");
        } else {
            iUserRepository.delete(foundUser.get());
        }
    }

    // validations for username & password
    private boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }

        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);

        return m.find();
    }

    private boolean containsDigit(String s) {
        if (s != null && !s.isEmpty()) {
            for (char c : s.toCharArray()) {
                if (Character.isDigit(c)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean validateUsername(String username) {
        return Pattern.compile(USERNAME_VALIDATION_REGEX).matcher(username).matches();
    }

    public String validatePassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return "Password too short!";
        }

        if (!containsSpecialCharacter(password)) {
            return "Password must contain at least one special character!";
        }

        if (!containsDigit(password)) {
            return "Password must contain at least one digit!";
        }

        return "Password OK";
    }

}
