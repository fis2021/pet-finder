package org.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.services.DatabaseService;
import org.services.FileSystemService;
import org.services.UserService;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class RegistrationTest {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "password";

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        DatabaseService.initDatabase();
    }

    @Start
    void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("registerScene.fxml"));
        primaryStage.setTitle("Petfinder");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Test
    void testRegistration(FxRobot robot) {
        robot.clickOn("#username");
        robot.write(USERNAME);
        robot.clickOn("#password");
        robot.write(PASSWORD);
        robot.clickOn("#registerButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Username,password, role and phone number are required");
        assertThat(UserService.getAllUsers()).size().isEqualTo(0);

        robot.clickOn("#registerButton");
        robot.clickOn("#phone");
        robot.write("0721");

        robot.clickOn("#role");
        robot.dropBy(0,20);
        robot.rightClickOn();

        robot.clickOn("#registerButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Username,password, role and phone number are required");
        robot.dropBy(100,-110);
        robot.rightClickOn();
        robot.doubleClickOn();

        robot.doubleClickOn("#password");
        robot.write("Password12!@");
        robot.clickOn("#registerButton");
        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Invalid phone number");

        robot.doubleClickOn("#phone");
        robot.write("07211111");
        robot.clickOn("#registerButton");
        /*assertThat(robot.lookup("#registrationMessage").queryText()).hasText(
                String.format("Password must be at least 8 characters long and contain:\n1 uppercase letter\n1 lowercase letter\n1 number\n1 special char")
        );*/

        /*robot.clickOn("#username");
        robot.write("1");
        robot.clickOn("#registerButton");

        assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Account created successfully!");
        assertThat(UserService.getAllUsers()).size().isEqualTo(2);
        */
    }
}