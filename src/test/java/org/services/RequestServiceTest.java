package org.services;

import org.apache.commons.io.FileUtils;
import org.dizitart.no2.objects.ObjectRepository;
import org.exceptions.UsernameAlreadyExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.model.Announcement;
import org.model.Pet;
import org.model.Request;
import org.model.User;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RequestServiceTest {
    User sender = new User("sender", "password1", "Individual", "0787665544");
    User receiver = new User("receiver", "password2", "Individual", "0723222111");
    Pet pet = new Pet("pet1","Dog");
    Announcement announcement = new Announcement(pet,receiver,"Adoption");
    Request req = new Request(sender,receiver,announcement);

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
    void databaseIsEmptyAfterCreation(){
        List<Request> requestList = RequestService.getRequestRepository().find().toList();
        assertThat(requestList).isNotNull();
        assertThat(requestList).isEmpty();
    }

    @Test
    void sendRequest() {
        RequestService.sendRequest(req);
        assertThat(RequestService.findRequestByID(req.getID())).isNotNull();
    }

    @Test
    void updateRequest() {
        RequestService.sendRequest(req);
        assert (RequestService.findRequestByID(req.getID()).getStatus().equals("Pending"));
        req.setStatus("Declined");
        RequestService.updateRequest(req);
        assert (RequestService.findRequestByID(req.getID()).getStatus().equals("Declined"));
    }

    @Test
    void closeAllRequestsRelatedToAnnouncement() {
        populateDatabase();
        List<Request> requestList = RequestService.getRequestRepository().find().toList();
        Request request = requestList.get(0);

        RequestService.closeAllRequestsRelatedToAnnouncement(request.getAnnouncement());

        boolean allRelatedRequestsAreClosed = true;
        for(Request crt : RequestService.getRequestRepository().find()){
            if(crt.getAnnouncement().equals(request.getAnnouncement())&&crt.getStatus().equals("Pending")){
                allRelatedRequestsAreClosed = false;
            }
        }

        assert allRelatedRequestsAreClosed;
    }

    @Test
    void closeAllRequestsRelatedToPet() {populateDatabase();
        populateDatabase();
        List<Request> requestList = RequestService.getRequestRepository().find().toList();
        Request request = requestList.get(0);

        RequestService.closeAllRequestsRelatedToPet(request.getAnnouncement().getPet());

        boolean allRelatedRequestsAreClosed = true;
        for(Request crt : RequestService.getRequestRepository().find()){
            if(crt.getAnnouncement().equals(request.getAnnouncement().getPet())&&crt.getStatus().equals("Pending")){
                allRelatedRequestsAreClosed = false;
            }
        }

        assert allRelatedRequestsAreClosed;
    }

    @Test
    void removeRequest() {
        populateDatabase();
        List<Request> requestList = RequestService.getRequestRepository().find().toList();
        Request request = requestList.get(0);

        RequestService.removeRequest(request);

        assert (RequestService.findRequestByID(request.getID()) == null);
    }

    @Test
    void requestExistsAndIsOpen() {
        populateDatabase();
        List<Request> requestList = RequestService.getRequestRepository().find().toList();
        Request request1 = requestList.get(0);
        Request request2 = requestList.get(1);

        request1.setStatus("Pending");
        request2.setStatus("Closed");
        RequestService.updateRequest(request1);
        RequestService.updateRequest(request2);

        assert RequestService.requestExistsAndIsOpen(request1.getSender().getUsername(),request1.getReceiver().getUsername(), request1.getAnnouncement().getID());
        assert !RequestService.requestExistsAndIsOpen(request2.getSender().getUsername(),request2.getReceiver().getUsername(), request2.getAnnouncement().getID());
    }

    @Test
    void canSendRequest() {
        populateDatabase();
        List<Request> requestList = RequestService.getRequestRepository().find().toList();
        Request request1 = requestList.get(0);

        assert !RequestService.canSendRequest(request1.getSender().getUsername(), request1.getReceiver().getUsername(), request1.getAnnouncement().getID());
        assert RequestService.canSendRequest(sender.getUsername(),receiver.getUsername(),announcement.getID());
    }
}