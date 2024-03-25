package ru.tolstov.lab1.notifications;

import lombok.Value;

import java.util.Calendar;

@Value
public class Notification {
    Calendar date;
    String info;

     @Override
     public String toString() {
         return "Date: %s\n\"%s\"\n".formatted(date.getTime(), info);
     }
}
