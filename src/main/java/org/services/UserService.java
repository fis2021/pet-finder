package org.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.exceptions.InvalidUserException;
import org.exceptions.UsernameAlreadyExistsException;
import org.model.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;


public class UserService {

    private static ObjectRepository<User> userRepository;

    public static void initUserRepo(ObjectRepository<User> userRepository){
        UserService.userRepository = userRepository;
    }

    public static User findUserByUsername(String username){
        for(User user : userRepository.find()){
            if(Objects.equals(username,user.getUsername())){
                return user;
            }
        }

        return null;
    }

    public static User login(String username, String password) throws InvalidUserException{
        User crt;

        crt = attemptLogin(username, password);

        if(crt == null){
            throw new InvalidUserException();
        }

        return crt;
    }

    public static User attemptLogin(String username, String password){
        for (User user : userRepository.find()) {
            if(Objects.equals(username, user.getUsername()) && Objects.equals(encodePassword(username, password), user.getPassword())){
                return user;
            }
        }

        return null;
    }

    public static void addUser(User user) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(user.getUsername());
        userRepository.insert(user);
    }

    public static void updateUser(User user){
        userRepository.update(user);
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    public static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }

    public static List<User> getAllUsers() {
        return userRepository.find().toList();
    }

}
