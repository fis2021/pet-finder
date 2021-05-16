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
        System.out.println("INITIALISING ANNOUNCEMENT REPOSITORY");
        AnnouncementService.announcementRepository = announcementRepository;
    }

    public static void addAnnouncement(Announcement announcement)  {
        System.out.println("\nAdding announcement with ID " + announcement.getID());
        announcementRepository.insert(announcement);
        System.out.println("Announcement inserted successfully");
    }

    public static void updateAnnouncement(Announcement announcement){
        System.out.println("\nUpdating announcement with ID " + announcement.getID());
        announcementRepository.update(announcement);
        System.out.println("Announcement modified successfully");
    }

    public static ObjectRepository<Announcement> getAnnouncementRepository() {return announcementRepository;}

    public static void removeAnnouncement(Announcement announcement){
        announcementRepository.remove(announcement);
        System.out.println("\nRemoved announcement with ID " + announcement.getID());
    }

    public static ArrayList<Announcement> getAnnouncements(){
        System.out.println("\nRetrieving global announcement arrayList from database");
        ArrayList<Announcement> globalAds= new ArrayList<>();
        for(Announcement crt : announcementRepository.find()){
            globalAds.add(crt);
        }
        System.out.println("Operation was completed successfully");
        return globalAds;
    }

    public static ArrayList<Announcement> getUserAnnouncements(String username){
        System.out.println("\nRetrieving announcement arrayList from database for user " + username);
        ArrayList<Announcement> userAds= new ArrayList<>();
        for(Announcement crt : announcementRepository.find()){
            if(Objects.equals(crt.getUser().getUsername(),username)){
                userAds.add(crt);
            }
        }
        System.out.println("Operation was completed successfully");
        return userAds;
    }

    public static ArrayList<Announcement> getCategoryAnnouncements(String category){
        System.out.println("\nRetrieving all" + category.toUpperCase() + " announcements from database");
        ArrayList<Announcement> categAds= new ArrayList<>();
        for(Announcement crt : announcementRepository.find()){
            if(category.equals("All")){
                categAds.add(crt);
            }
            else if(Objects.equals(crt.getCategory(),category)){
                categAds.add(crt);
            }
        }
        System.out.println("Operation was completed successfully");
        return categAds;
    }

    public static ArrayList<Announcement> getPetTypeAnnouncements(String petType){
        System.out.println("\nRetrieving all" + petType.toUpperCase() + " announcements from database");
        ArrayList<Announcement> petTypeAds= new ArrayList<>();
        for(Announcement crt : announcementRepository.find()){
            if(petType.equals("All")){
                petTypeAds.add(crt);
            }
            if(Objects.equals(crt.getPet().getType(),petType)){
                petTypeAds.add(crt);
            }
        }
        System.out.println("Operation was completed successfully");
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
        System.out.println("\nSearching for announcement ID " + id);
        for(Announcement crt : announcementRepository.find()){
            if(crt.getID().equals(id)){
                System.out.println("Announcement was found");
                return crt;
            }
        }
        System.out.println("Announcement with ID " + id + " was not found");
        return null;
    }

    public static List<Announcement> getAllAnnouncements() {
        return announcementRepository.find().toList();
    }
}
