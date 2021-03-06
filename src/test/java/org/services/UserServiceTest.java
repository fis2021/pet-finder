package org.services;

import org.apache.commons.io.FileUtils;
import org.exceptions.InvalidUserException;
import org.exceptions.UsernameAlreadyExistsException;
import org.junit.jupiter.api.*;
import org.model.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

class UserServiceTest {

    public static final String ADMIN = "admin";

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
    @DisplayName("Database is initialized, and there are no users")
    void testDatabaseIsInitializedAndNoUserIsPersisted() {
        assertThat(UserService.getAllUsers()).isNotNull();
        assertThat(UserService.getAllUsers()).isEmpty();
    }

    @Test
    @DisplayName("User is successfully persisted to Database")
    void testUserIsAddedToDatabase() throws UsernameAlreadyExistsException {
        User admin = new User(ADMIN,ADMIN,ADMIN,"");
        UserService.addUser(admin);
        assertThat(UserService.getAllUsers()).isNotEmpty();
        assertThat(UserService.getAllUsers()).size().isEqualTo(1);
        User user = UserService.getAllUsers().get(0);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(ADMIN);
        assertThat(user.getRole()).isEqualTo(ADMIN);
    }

    @Test
    @DisplayName("User can not be added twice")
    void testUserCanNotBeAddedTwice() {
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            User admin = new User(ADMIN,ADMIN,ADMIN,"");
            UserService.addUser(admin);
            UserService.addUser(admin);
        });
    }

    @Test
    @DisplayName("Login")
    void testLogin(){
        assertThrows(InvalidUserException.class, () ->{
           UserService.login("invalid","invalid");
        });
        User admin = new User(ADMIN,ADMIN,ADMIN,"");
        try {
            UserService.addUser(admin);
            assertThat(UserService.login(ADMIN,ADMIN)).isNotNull();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        } catch (InvalidUserException e) {
            e.printStackTrace();
        }
    }
}