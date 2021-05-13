package org.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.model.Announcement;
import org.model.Pet;
import org.services.AnnouncementService;
import org.services.DatabaseService;
import org.services.FileSystemService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class AddPetPageTest {
    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test";
        //FileSystemService.initDirectory();
        //FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        DatabaseService.initDatabase();
    }

    @AfterEach
    void tearDown() {
        DatabaseService.getDatabase().close();
        System.out.println("After each");
    }

    @Start
    void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("Add Pet");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Test
    void cancelAddPet(FxRobot robot) {
        robot.doubleClickOn("#username");
        robot.write("HappyPaws");
        robot.doubleClickOn("#password");
        robot.write("Parola123!");
        robot.clickOn("#loginButton");
        robot.clickOn("#managePets");
        robot.clickOn("#addPet");
        robot.clickOn("#cancelButton");
    }


    @Test
    void testAddPet(FxRobot robot) {
        robot.doubleClickOn("#username");
        robot.write("HappyPaws");
        robot.doubleClickOn("#password");
        robot.write("Parola123!");
        robot.clickOn("#loginButton");
        robot.clickOn("#managePets");
        robot.clickOn("#addPet");
        robot.clickOn("#addButton");
        assertThat(robot.lookup("#addMessage").queryText()).hasText("Name and type are required!");
        robot.clickOn("#petName");
        robot.write("fifi");
        robot.clickOn("#petType");
        robot.dropBy(-20,40);
        robot.clickOn();
        robot.clickOn("#petInfo");
        robot.write("cute");
        robot.clickOn("#photo");
        robot.dropBy(100,-100);
        robot.clickOn();
        robot.clickOn("#clearPhoto");
        robot.clickOn("#cancelButton");
    }
}