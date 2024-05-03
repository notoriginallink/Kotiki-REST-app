package ru.tolstov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

// admin - $2a$10$YeTWMESION9r/9IQEvHB4OvPcd6M6fzLXaNOMZrIiNB00EBqVXnDm
// ZLenka - $2a$10$GnFlW5NRRKsZKaV3ZzZi6.rjCq3/30lkJnfwcxf9m2UdOmSPRxY/C
// qwerty - $2a$10$bteKvzcQni4Vaa/uQ6q5pul320GrO7LynWvTBg0WDtL0ypdKw3Ete
// star - $2a$10$rcoheU1HMSea94VVYYeSge7FG23qoCogGFGQeQpIqyVnOecqgtHqW