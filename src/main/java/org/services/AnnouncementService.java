package org.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.exceptions.UsernameAlreadyExistsException;
import org.model.Announcement;
import org.model.Pet;
import org.model.User;

import java.util.ArrayList;
import java.util.List;
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

    public static ArrayList<Announcement> getAnnouncements(){
        ArrayList<Announcement> globalAds= new ArrayList<>();
        for(Announcement crt : announcementRepository.find()){
            globalAds.add(crt);
        }
        return globalAds;
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

    public static ArrayList<Announcement> getCategoryAnnouncements(String category){
        ArrayList<Announcement> categAds= new ArrayList<>();
        for(Announcement crt : announcementRepository.find()){
            if(category.equals("All")){
                categAds.add(crt);
            }
            else if(Objects.equals(crt.getCategory(),category)){
                categAds.add(crt);
            }
        }
        return categAds;
    }

    public static ArrayList<Announcement> getPetTypeAnnouncements(String petType){
        ArrayList<Announcement> petTypeAds= new ArrayList<>();
        for(Announcement crt : announcementRepository.find()){
            if(petType.equals("All")){
                petTypeAds.add(crt);
            }
            if(Objects.equals(crt.getPet().getType(),petType)){
                petTypeAds.add(crt);
            }
        }
        return petTypeAds;
    }

    public static ArrayList<Announcement> getCategoryPetTypeAnnouncements(String category,String petType){
        if(category.equals("All") && petType.equals("All")){
            return getAnnouncements();
        }
        else {
            if(category.equals("All")){
                return getPetTypeAnnouncements(petType);
            }
            if(petType.equals("All")){
                return getCategoryAnnouncements(category);
            }
            ArrayList<Announcement> filteredAds = new ArrayList<>();
            for (Announcement crt : announcementRepository.find()) {
                if (Objects.equals(crt.getCategory(), category) && Objects.equals(crt.getPet().getType(), petType)) {
                    filteredAds.add(crt);
                }
            }
            return filteredAds;
        }
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

    public static List<Announcement> getAllAnnouncements() {
        return announcementRepository.find().toList();
    }

}
