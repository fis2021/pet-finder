package org.services;

import org.dizitart.no2.objects.ObjectRepository;
import org.model.Announcement;
import org.model.User;

public class AnnouncementService {

    private static ObjectRepository<Announcement> announcementRepository;

    public static void initAnnouncementRepo(ObjectRepository<Announcement> announcementRepository){
        AnnouncementService.announcementRepository = announcementRepository;
    }
}
