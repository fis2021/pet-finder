package org.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.exceptions.UserDoesNotExistException;
import org.exceptions.UsernameAlreadyExistsException;
import org.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.services.FileSystemService.getPathToFile;

public class DatabaseService {

    private static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("petfinder.db").toFile())
                .openOrCreate("User1", "P@ssw0rd!");

        userRepository = database.getRepository(User.class);
    }

    public static User login(String username, String password) throws UserDoesNotExistException{
        User crt;

        crt = attemptLogin(username, password);

        if(crt == null){
            throw new UserDoesNotExistException(username);
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

    public static void addUser(String username, String password, String role) throws UsernameAlreadyExistsException {
        checkUserDoesNotAlreadyExist(username);
        userRepository.insert(new User(username, encodePassword(username, password), role));
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    private static String encodePassword(String salt, String password) {
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


}
