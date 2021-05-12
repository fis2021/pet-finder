package org.services;

import org.apache.commons.io.FileUtils;
import org.exceptions.UsernameAlreadyExistsException;
import org.junit.jupiter.api.*;
import org.model.Announcement;
import org.model.Pet;
import org.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnnouncementServiceTest {

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
        FileSystemService.APPLICATION_FOLDER = ".test-registration-example";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        DatabaseService.initDatabase();
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each");
    }

    @Test
    void testDemo() {
        System.out.println("testdemo");
    }

    @Test
    @DisplayName("Database is initialized, and there are no announcements")
    void testDatabaseIsInitializedAndNoAnnouncementIsPersisted() {
        assertThat(AnnouncementService.getAllAnnouncements()).isNotNull();
        assertThat(AnnouncementService.getAllAnnouncements()).isEmpty();
    }

    @Test
    @DisplayName("Announcement is successfully persisted to Database")
    void testAnnouncementIsAddedToDatabase() throws UsernameAlreadyExistsException {
        User admin = new User(ADMIN,ADMIN,"Individual","");
        UserService.addUser(admin);
        Pet pet = new Pet("pet","Dog");
        Announcement announcement = new Announcement(pet,admin,"Adoption");
        AnnouncementService.addAnnouncement(announcement);
        assertThat(AnnouncementService.getAllAnnouncements()).isNotEmpty();
        assertThat(AnnouncementService.getAllAnnouncements()).size().isEqualTo(1);
        Announcement announcement1 = AnnouncementService.getAllAnnouncements().get(0);
        assertThat(announcement1).isNotNull();
        assertThat(announcement1.getUser().getUsername()).isEqualTo(ADMIN);
        assertThat(announcement1.getUser().getRole()).isEqualTo("Individual");
        assertThat(announcement1.getCategory()).isEqualTo("Adoption");
        assertThat(announcement1.getPet().getName()).isEqualTo("pet");
        assertThat(announcement1.getPet().getType()).isEqualTo("Dog");
    }

    @Test
    @DisplayName("Announcement is removed from database")
    void testAnnouncementIsRemoved() throws UsernameAlreadyExistsException {
        User admin = new User(ADMIN,ADMIN,"Individual","");
        UserService.addUser(admin);
        Pet pet = new Pet("pet","Dog");
        Announcement announcement = new Announcement(pet,admin,"Adoption");
        AnnouncementService.addAnnouncement(announcement);
        assertThat(AnnouncementService.getAllAnnouncements()).isNotEmpty();
        assertThat(AnnouncementService.getAllAnnouncements()).size().isEqualTo(1);
        Announcement announcement1 = AnnouncementService.getAllAnnouncements().get(0);
        assertThat(announcement1).isNotNull();
        assertThat(announcement1.getUser().getUsername()).isEqualTo(ADMIN);
        assertThat(announcement1.getUser().getRole()).isEqualTo("Individual");
        assertThat(announcement1.getCategory()).isEqualTo("Adoption");
        assertThat(announcement1.getPet().getName()).isEqualTo("pet");
        assertThat(announcement1.getPet().getType()).isEqualTo("Dog");
        AnnouncementService.removeAnnouncement(announcement1);
        assertThat(AnnouncementService.getAllAnnouncements()).size().isEqualTo(0);
    }

    @Test
    @DisplayName("Get announcement by pet")
    void testGetAnnouncementByPet() throws UsernameAlreadyExistsException {
        User admin = new User(ADMIN,ADMIN,"Individual","");
        UserService.addUser(admin);
        Pet pet = new Pet("pet","Dog");
        Announcement announcement = new Announcement(pet,admin,"Adoption");
        AnnouncementService.addAnnouncement(announcement);
        assertThat(AnnouncementService.getAllAnnouncements()).isNotEmpty();
        assertThat(AnnouncementService.getAllAnnouncements()).size().isEqualTo(1);
        Announcement announcement1 = AnnouncementService.getAllAnnouncements().get(0);
        assertThat(announcement1).isNotNull();
        assertThat(announcement1.getUser().getUsername()).isEqualTo(ADMIN);
        assertThat(announcement1.getUser().getRole()).isEqualTo("Individual");
        assertThat(announcement1.getCategory()).isEqualTo("Adoption");
        assertThat(announcement1.getPet().getName()).isEqualTo("pet");
        assertThat(announcement1.getPet().getType()).isEqualTo("Dog");
        Announcement announcement2 = AnnouncementService.getPetAnnouncement(pet);
        assertThat(announcement1.equals(announcement2));
    }
}