package com.example.loginApplication.Service;

import com.example.loginApplication.Domain.User;
import com.example.loginApplication.Exception.Domain.EmailExistException;
import com.example.loginApplication.Exception.Domain.EmailNotFoundException;
import com.example.loginApplication.Exception.Domain.UserNotFoundException;
import com.example.loginApplication.Exception.Domain.UsernameExistsException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {

    //User creates new account
    User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, EmailExistException, UsernameExistsException, MessagingException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);

    //
    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistsException, IOException;
    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNonLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistsException, IOException;
    void deleteUser(Long id);
    void resetPassword(String email) throws MessagingException, EmailNotFoundException;

    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistsException, IOException;

}
