package ru.tolstov;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.tolstov.notifications.Notification;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Client {
    @EqualsAndHashCode.Include
    private final Phone phoneNumber;
    private final String firstName;
    private final String lastName;
    private String address;
    private String passportID;
    private final Bank bank;
    private final List<Notification> notifications;

    @Override
    public String toString() {
        return "Phone: %s\nFirst name: %s; Last name: %s\nAddress: %s; PassportID: %s\n"
                .formatted(phoneNumber.getNumber(), firstName, lastName, address, phoneNumber);
    }
}
