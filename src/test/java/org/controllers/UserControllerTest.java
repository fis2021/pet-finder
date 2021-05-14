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
import org.model.*;
import org.services.*;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class UserControllerTest {
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

        String shPassword = UserService.encodePassword("shelterUser", "crtP@ssword1");

        User crtShelter = new User("shelterUser", shPassword, "Shelter", "0799666555");

        Pet crtPet = new Pet("userPet", "Cat");
        Announcement crtAnnouncement = new Announcement(crtPet,crtUser,"Adoption");
        Request reqTest = new Request(crtUser, user3, announcement3);
        Request reqApprovalTest = new Request(user3, crtUser, crtAnnouncement);

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
            RequestService.sendRequest(reqTest);
            RequestService.sendRequest(reqApprovalTest);
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

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
    void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public void loginUser(FxRobot robot){
        populateDatabase();
        robot.doubleClickOn("#username");
        robot.write("crtUsername");
        robot.doubleClickOn("#password");
        robot.write("crtP@ssword1");
        robot.clickOn("#loginButton");
    }

    @Test
    void testUpdateUserAccount(FxRobot robot) throws InterruptedException {
        loginUser(robot);
        robot.clickOn("#menuButton");
        robot.clickOn("#myProfile");
        robot.clickOn("#toggleEdit");
        robot.clickOn("#country");
        robot.write("Romania");
        robot.clickOn("#region");
        robot.write("Timis");
        robot.clickOn("#town");
        robot.write("Giarmata-Vii");
        robot.clickOn("#street");
        robot.write("Str. Macesului, 20");
        robot.clickOn("#updateButton");
        robot.dropBy(0,-205);
        robot.clickOn();
        robot.write("crtP@ssword1");
        robot.dropBy(50,40);
        robot.clickOn();
        robot.dropBy(-20,-20);
        robot.clickOn();
        robot.clickOn("#menuButton");
        robot.clickOn("#myProfile");
    }
}