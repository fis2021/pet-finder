package org.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.exceptions.UsernameAlreadyExistsException;
import org.model.Announcement;
import org.model.User;

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

    public static void removeAnnouncement(Announcement announcement){
        announcementRepository.remove(announcement);
    }
}
