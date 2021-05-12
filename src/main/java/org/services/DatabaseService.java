package org.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.exceptions.InvalidUserException;
import org.exceptions.UsernameAlreadyExistsException;
import org.model.Announcement;
import org.model.Request;
import org.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static org.services.FileSystemService.getPathToFile;

public class DatabaseService {

    private static ObjectRepository<User> userRepository;
    private static ObjectRepository<Announcement> announcementRepository;
    private static ObjectRepository<Request> requestRepository;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("petfinder.db").toFile())
                .openOrCreate("test", "parola");

        userRepository = database.getRepository(User.class);
        announcementRepository = database.getRepository(Announcement.class);
        requestRepository = database.getRepository(Request.class);

        UserService.initUserRepo(userRepository);
        AnnouncementService.initAnnouncementRepo(announcementRepository);
        RequestService.initRequestRepo(requestRepository);
    }
}
