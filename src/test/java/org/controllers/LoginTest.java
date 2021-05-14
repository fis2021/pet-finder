package org.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.exceptions.UsernameAlreadyExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.model.Announcement;
import org.model.Pet;
import org.model.Request;
import org.model.User;
import org.services.*;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class LoginTest {

    public static final String USER_1 = "user1";
    public static final String PASSWORD_1 = "password1";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".testPetfinder";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        DatabaseService.initDatabase();
    }

    @AfterEach
    void tearDown() {
        DatabaseService.getDatabase().close();
        System.out.println("After each");
    }

    @Start
    void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Petfinder");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private void populateDatabase(){
        User user1 = new User("user1", "password1", "Individual", "0722111111");
        User user2 = new User("user2", "password2", "Individual", "0722111112");
        User user3 = new User("user3", "password3", "Shelter", "0722111113");
        User user4 = new User("user4", "password4", "Individual", "0722111114");
        Pet pet1 = new Pet("pet1","Dog");
        Pet pet2 = new Pet("pet2","Dog");
        Pet pet3 = new Pet("pet3","Dog");
        Pet pet4 = new Pet("pet4","Dog");
        Announcement announcement1 = new Announcement(pet1, user1, "Lost");
        Announcement announcement2 = new Announcement(pet2, user2, "Found");
        Announcement announcement3 = new Announcement(pet3, user3, "Adoption");
        Announcement announcement4 = new Announcement(pet4, user4, "Adoption");
        Request req1 = new Request(user1, user2, announcement2);
        Request req2 = new Request(user1, user3, announcement3);
        Request req3 = new Request(user1, user4, announcement4);
        Request req4 = new Request(user2, user1, announcement1);
        Request req5 = new Request(user2, user3, announcement3);
        Request req6 = new Request(user2, user4, announcement4);
        Request req7 = new Request(user3, user1, announcement1);
        Request req8 = new Request(user3, user2, announcement2);
        Request req9 = new Request(user3, user4, announcement4);
        Request req10 = new Request(user4, user1, announcement1);
        Request req11 = new Request(user4, user2, announcement2);
        Request req12 = new Request(user4, user3, announcement3);
        String crtUsername = "crtUsername";
        String crtPassword = UserService.encodePassword(crtUsername, "crtP@ssword1");
        User crtUser = new User(crtUsername,crtPassword,"Individual","0787665511");

        String shelterUsername = "shelter";
        String crtPass = UserService.encodePassword(shelterUsername, "Parola123!");
        User crtShelter = new User(shelterUsername,crtPass,"Shelter","0787665511");
        try {
            UserService.addUser(user1);
            UserService.addUser(user2);
            UserService.addUser(user3);
            UserService.addUser(user4);
            UserService.addUser(crtUser);
            UserService.addUser(crtShelter);
            AnnouncementService.addAnnouncement(announcement1);
            AnnouncementService.addAnnouncement(announcement2);
            AnnouncementService.addAnnouncement(announcement3);
            AnnouncementService.addAnnouncement(announcement4);
            RequestService.sendRequest(req1);
            RequestService.sendRequest(req2);
            RequestService.sendRequest(req3);
            RequestService.sendRequest(req4);
            RequestService.sendRequest(req5);
            RequestService.sendRequest(req6);
            RequestService.sendRequest(req7);
            RequestService.sendRequest(req8);
            RequestService.sendRequest(req9);
            RequestService.sendRequest(req10);
            RequestService.sendRequest(req11);
            RequestService.sendRequest(req12);
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLogin(FxRobot robot) {
        populateDatabase();
        robot.clickOn("#username");
        robot.write(USER_1);
        robot.clickOn("#password");
        robot.write(PASSWORD_1);
        robot.clickOn("#loginButton");

        assertThat(robot.lookup("#loginMessage").queryText()).hasText("Invalid username/password");
        robot.doubleClickOn("#username");
        robot.write("crtUsername");
        robot.doubleClickOn("#password");
        robot.write("crtP@ssword1");
        robot.clickOn("#loginButton");
    }

    @Test
    void redirectRegister(FxRobot robot){
        robot.clickOn("#redirectToRegisterButton");
    }

}