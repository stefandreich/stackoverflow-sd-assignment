package com.sd.stackoverflow;

import com.sd.stackoverflow.dto.UserDTO;
import com.sd.stackoverflow.mapper.UserMapper;
import com.sd.stackoverflow.model.User;
import com.sd.stackoverflow.repository.IUserRepository;
import com.sd.stackoverflow.service.UserService;
import com.sd.stackoverflow.service.customexceptions.InvalidDataException;
import lombok.NoArgsConstructor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.mockito.ArgumentMatchers.any;

@NoArgsConstructor(force = true)
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserTest extends AbstractJUnit4SpringContextTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepository iUserRepository;

    @Mock
    private UserMapper userMapper;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().silent();

    @Test
    public void addUser_OK() {
        UserDTO userDTO = dummyUserDTO();
        userDTO.setPassword("willsmith99!");
        Mockito.when(iUserRepository.save(any(User.class))).thenReturn(dummyUser());
        Mockito.when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        Mockito.when(userMapper.toEntity(any(UserDTO.class))).thenReturn(dummyUser());

        try {
            userService.addUser(userDTO);
            assert(true);
        } catch (InvalidDataException e) {
            assert(false);
        }
    }

    @Test
    public void addUser_weakPassword() {
        UserDTO userDTO = dummyUserDTO();
        userDTO.setPassword("aaaaa");
        Mockito.when(iUserRepository.save(any(User.class))).thenReturn(dummyUser());
        Mockito.when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);
        Mockito.when(userMapper.toEntity(any(UserDTO.class))).thenReturn(dummyUser());

        try {
            userService.addUser(userDTO);
            assert(false);
        } catch (InvalidDataException e) {
            assert(true);
        }
    }

    private UserDTO dummyUserDTO() {
        return UserDTO.builder()
                .username("jackiechan")
                .email("jackie@com")
                .build();
    }

    private User dummyUser() {
        return User.builder()
                .username("jackiechan")
                .email("jackie@com")
                .build();
    }
}
