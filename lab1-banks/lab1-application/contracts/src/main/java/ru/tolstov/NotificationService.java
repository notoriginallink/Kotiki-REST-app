package ru.tolstov;

import ru.tolstov.notifications.Notification;

public interface NotificationService {
    Notification createNotification(String info);
    void sendNotification(Client client, Notification notification);
}
