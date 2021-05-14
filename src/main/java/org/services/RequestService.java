package org.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.model.Announcement;
import org.model.Pet;
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

    public static void closeAllRequestsRelatedToAnnouncement(Announcement announcement){
        for(Request crt : requestRepository.find()){
            if(Objects.equals(crt.getAnnouncement(), announcement) && Objects.equals(crt.getStatus(), "Pending")){
                crt.setStatus("Closed");
                updateRequest(crt);
            }
        }
    }

    public static void closeAllRequestsRelatedToPet(Pet pet){
        for(Request crt : requestRepository.find()){
            if(Objects.equals(crt.getAnnouncement().getPet(), pet) && Objects.equals(crt.getStatus(), "Pending")){
                crt.setStatus("Closed");
                updateRequest(crt);
            }
        }
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

    public static boolean requestExistsAndIsOpen(String senderUsername, String receiverUsername, String announcementID) {
        for (Request crt : requestRepository.find()) {
            if (Objects.equals(senderUsername, crt.getSender().getUsername()) && Objects.equals(receiverUsername, crt.getReceiver().getUsername()) && Objects.equals(announcementID, crt.getAnnouncement().getID()) && Objects.equals(crt.getStatus(),"Pending")) {
                return true;
            }
        }
        return false;
    }

    public static boolean canSendRequest(String senderUsername, String receiverUsername, String announcementID){
        for (Request crt : requestRepository.find()) {
            if (Objects.equals(senderUsername, crt.getSender().getUsername()) && Objects.equals(receiverUsername, crt.getReceiver().getUsername()) && Objects.equals(announcementID, crt.getAnnouncement().getID())) {
                return false;
            }
        }

        return true;
    }

    public static Request findRequestByID(String ID){
        for(Request crt : requestRepository.find()){
            if(crt.getID().equals(ID)){
                return crt;
            }
        }
        return null;
    }

    public static ObjectRepository<Request> getRequestRepository() {
        return requestRepository;
    }
}
