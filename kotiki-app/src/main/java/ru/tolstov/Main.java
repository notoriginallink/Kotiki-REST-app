package ru.tolstov;

import jakarta.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tolstov.models.CatColor;
import ru.tolstov.repositories.LocalCatRepository;
import ru.tolstov.repositories.LocalOwnerRepository;
import ru.tolstov.services.CatServiceImpl;
import ru.tolstov.services.OwnerServiceImpl;

import java.time.LocalDate;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}