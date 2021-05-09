package org.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.exceptions.UsernameAlreadyExistsException;
import org.model.Announcement;
import org.model.Pet;
import org.model.User;

import java.util.ArrayList;
import java.util.Objects;

public class AnnouncementService {

    private static ObjectRepository<Announcement> announcementRepository;

    public static void initAnnouncementRepo(ObjectRepository<Announcement> announcementRepository){
        AnnouncementService.announcementRepository = announcementRepository;
    }

    public static void addAnnouncement(Announcement announcement)  {
        announcementRepository.insert(announcement);
    }

    public static void updateAnnouncement(Announcement announcement){
        announcementRepository.update(announcement);
    }

    public static ObjectRepository<Announcement> getAnnouncementRepository() {return announcementRepository;}

    public static void removeAnnouncement(Announcement announcement){
        announcementRepository.remove(announcement);
    }

    public static ArrayList<Announcement> getUserAnnouncements(String username){
        ArrayList<Announcement> userAds= new ArrayList<>();
        for(Announcement crt : announcementRepository.find()){
            if(Objects.equals(crt.getUser().getUsername(),username)){
                userAds.add(crt);
            }
        }
        return userAds;
    }

    public static Announcement getPetAnnouncement(Pet pet){
        for(Announcement crt : announcementRepository.find()){
            if(crt.getPet().equals(pet)){
                return crt;
            }
        }
        return null;
    }

    public static Announcement getIdAnnouncement(String id){
        for(Announcement crt : announcementRepository.find()){
            if(crt.getID().equals(id)){
                return crt;
            }
        }
        return null;
    }

}
