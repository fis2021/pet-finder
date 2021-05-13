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
import org.services.DatabaseService;
import org.services.FileSystemService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class HomePageTest {
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
    void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("HomePage");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Test
    void testAddAnnouncement(FxRobot robot){
        robot.doubleClickOn("#username");
        robot.write("Bianca");
        robot.doubleClickOn("#password");
        robot.write("Parola123!");
        robot.clickOn("#loginButton");
        robot.clickOn("#addAnnouncement");
    }

    /*@Test
    void testSignOut(FxRobot robot){
        robot.doubleClickOn("#username");
        robot.write("Bianca");
        robot.doubleClickOn("#password");
        robot.write("Parola123!");
        robot.clickOn("#loginButton");
        robot.clickOn("#signOut");
    }*/

    @Test
    void homeBarMethods(FxRobot robot){
        robot.doubleClickOn("#username");
        robot.write("Bianca");
        robot.doubleClickOn("#password");
        robot.write("Parola123!");
        robot.clickOn("#loginButton");
        robot.clickOn("#home");
    }


    @Test
    void testChooseAnnouncement(FxRobot robot) {
        robot.doubleClickOn("#username");
        robot.write("Bianca");
        robot.doubleClickOn("#password");
        robot.write("Parola123!");
        robot.clickOn("#loginButton");
        robot.clickOn("#category");
        robot.dropBy(0,20);
        robot.clickOn();
        robot.clickOn("#petType");
        robot.dropBy(-40,20);
        robot.clickOn();
        robot.clickOn("#category");
        robot.dropBy(0,20);
        robot.clickOn();
        robot.clickOn("#petType");
        robot.dropBy(-40,20);
        robot.clickOn();
        robot.clickOn("#category");
        robot.dropBy(0,40);
        robot.clickOn();
        robot.clickOn("#petType");
        robot.dropBy(-40,40);
        robot.clickOn();
        robot.clickOn("#category");
        robot.dropBy(0,-20);
        robot.clickOn();
        robot.clickOn("#petType");
        robot.dropBy(-40,-20);
        robot.clickOn();
        robot.dropBy(-100,150);
        robot.doubleClickOn();
    }

}