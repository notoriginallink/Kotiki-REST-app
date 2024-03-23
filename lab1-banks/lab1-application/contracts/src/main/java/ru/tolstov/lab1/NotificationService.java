package ru.tolstov.lab1;

import ru.tolstov.lab1.notifications.Notification;

public interface NotificationService {
    Notification createNotification(String info);
    void sendNotification(Client client, Notification notification);
}
