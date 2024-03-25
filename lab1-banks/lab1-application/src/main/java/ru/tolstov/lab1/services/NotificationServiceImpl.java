package ru.tolstov.lab1.services;

import ru.tolstov.lab1.Client;
import ru.tolstov.lab1.NotificationService;
import ru.tolstov.lab1.notifications.Notification;

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
