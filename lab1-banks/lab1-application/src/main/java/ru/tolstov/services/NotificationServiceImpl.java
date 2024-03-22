package ru.tolstov.services;

import ru.tolstov.Client;
import ru.tolstov.NotificationService;
import ru.tolstov.notifications.Notification;

public class NotificationServiceImpl implements NotificationService {
    @Override
    public Notification createNotification(String info) {
        return new Notification(CentralBankServiceImpl.currentDate(), info);
    }

    @Override
    public void sendNotification(Client client, Notification notification) {
        client.getNotifications().add(notification);
    }
}
