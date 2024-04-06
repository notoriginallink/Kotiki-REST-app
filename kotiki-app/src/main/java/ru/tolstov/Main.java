package ru.tolstov;

import ru.tolstov.models.CatColor;
import ru.tolstov.repositories.LocalCatRepository;
import ru.tolstov.repositories.LocalOwnerRepository;
import ru.tolstov.services.CatServiceImpl;
import ru.tolstov.services.OwnerServiceImpl;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // REPOSITORIES
        var catRepo = new LocalCatRepository();
        var ownerRepo = new LocalOwnerRepository();

        // SERVICES
        var catService = new CatServiceImpl(catRepo, ownerRepo);
        var ownerService = new OwnerServiceImpl(ownerRepo);

        var me = ownerService.createOwner("Danya", "Tolstov", LocalDate.of(2004, 2, 25));
        var you = ownerService.createOwner("Aboba", "Amogus", LocalDate.of(2004, 6, 6));

        var id1 = catService.addCat("Oleg", LocalDate.now(), "Russian", CatColor.ORANGE, me);
        var id2 = catService.addCat("Plusha", LocalDate.now(), "Angorian", CatColor.WHITE, you);
        var id3 = catService.addCat("Sema", LocalDate.now(), "Persian", CatColor.GREY, me);

        catService.makeFriendship(id1, id2);
    }
}