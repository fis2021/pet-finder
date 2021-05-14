package org.controllers;

import javafx.scene.text.Text;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.model.User;
import org.services.DatabaseService;
import org.services.FileSystemService;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class ControllerTest {
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before class");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After class");
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

    @Test
    void testDemo() {
        System.out.println("testdemo");
    }

    @Test
    void testSetUser() throws MalformedURLException {
        User user = new User("admin","Parola123!","Individual","0724353647");
        Controller controller = new Controller();
        controller.setUser(user);
        assertThat(user).isNotNull();
        assertThat(controller.AccountStatus).isNull();
        Text text = new Text("");
        controller.AccountStatus = text;
        controller.AccountStatus.setText("");
        controller.setUser(user);
        assertThat(controller.user).isNotNull();
        //assertThat(controller.AccountStatus).queryText()).hasText("Logged-in as " + user.getUsername());
    }

}