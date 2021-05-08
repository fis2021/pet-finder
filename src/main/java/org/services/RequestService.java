package org.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.model.Announcement;
import org.model.Request;
import org.model.User;

import java.util.ArrayList;
import java.util.Objects;

public class RequestService {
    private static ObjectRepository<Request> requestRepository;

    public static void initRequestRepo(ObjectRepository<Request> requestRepository){
        RequestService.requestRepository = requestRepository;
    }

    public static void sendRequest(Request request)  {
        requestRepository.insert(request);
    }

    public static void updateRequest(Request request){
        requestRepository.update(request);
    }

    public static void removeRequest(Request request){
        requestRepository.remove(request);
    }

    public static ArrayList<Request> getUserSentRequests(String username){
        ArrayList<Request> userSentRequests = new ArrayList<>();
        for(Request crt : requestRepository.find()){
            if(Objects.equals(crt.getSender().getUsername(),username)){
                userSentRequests.add(crt);
            }
        }

        return userSentRequests;
    }

    public static ArrayList<Request> getUserReceivedRequests(String username){
        ArrayList<Request> userReceivedRequests = new ArrayList<>();
        for(Request crt : requestRepository.find()){
            if(Objects.equals(crt.getReceiver().getUsername(),username)){
                userReceivedRequests.add(crt);
            }
        }

        return userReceivedRequests;
    }

    public boolean requestExists(User sender, User receiver, Announcement announcement) {

        for (Request crt : requestRepository.find()) {
            if (Objects.equals(sender, crt.getSender()) && Objects.equals(receiver, crt.getReceiver()) && Objects.equals(announcement, crt.getAnnouncement())) {
                return true;
            }
        }

        return false;
    }

}
