import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.tolstov.models.Cat;
import ru.tolstov.models.Owner;
import ru.tolstov.repositories.LocalOwnerRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.OwnerServiceImpl;
import ru.tolstov.services.dto.OwnerItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OwnerServiceTests {
    private static OwnerRepository ownerRepository;
    private static EntityManagerFactory entityManagerFactory;
    @Spy
    List<Cat> cats;
    private OwnerServiceImpl ownerService;
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        ownerRepository = Mockito.spy(new LocalOwnerRepository());
        ownerService = new OwnerServiceImpl(entityManagerFactory, ownerRepository);
    }

    @BeforeAll
    public static void initDB() {
        entityManagerFactory = Persistence.createEntityManagerFactory("kotiki-test-database");
    }

    @AfterAll
    public static void closeDB() {
        entityManagerFactory.close();;
    }

    @Test
    void createOwner_ShouldBeSuccessful() {
        String firstName = "amogus";
        String lastName = "abobus";
        LocalDate birthdate = LocalDate.of(2020, 2, 2);
        long expectedID = 1;
        Owner owner = new Owner();

        Mockito.doReturn(expectedID).when(ownerRepository).registerOwner(owner);

        long actualID = ownerService.createOwner(firstName, lastName, birthdate);

        assertEquals(expectedID, actualID);
    }

    @Test
    void getAllOwners_ShouldBeSuccessful() {
        Owner owner = new Owner();
        owner.setId(1);
        Owner owner1 = new Owner();
        owner1.setId(2);

        List<Owner> owners = new ArrayList<>();
        int expectedSize = 2;
        owners.add(owner);
        owners.add(owner1);

        List<OwnerItem> expectedList = new ArrayList<>();
        expectedList.add(new OwnerItem(owner));
        expectedList.add(new OwnerItem(owner1));

        Mockito.doReturn(owners).when(ownerRepository).getAllOwners();

        List<OwnerItem> actualList = ownerService.getAllOwners();
        int actualSize = actualList.size();

        assertEquals(expectedSize, actualSize);
        Assertions.assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    void removeOwner_WhenNotInRepository_NothingShouldHappen() {
        long ownerID = 1;

        // mock that there's no owner with ID=1
        Mockito.doReturn(null).when(ownerRepository).getOwnerById(ownerID);

        ownerService.removeOwner(ownerID);

        Mockito.verify(ownerRepository, Mockito.never()).deleteOwner(Mockito.any());
    }

    @Test
    void removeOwner_WhenHasCat_ShouldThrowException() {
        long ownerID = 1;
        Owner owner = new Owner();
        owner.setId(ownerID);
        owner.setCats(cats);

        // mock that there's owner with ID=1
        Mockito.doReturn(owner).when(ownerRepository).getOwnerById(ownerID);

        // mock that owner has cats
        Mockito.doReturn(false).when(cats).isEmpty();

        assertThrows(RuntimeException.class, () -> {
            ownerService.removeOwner(ownerID);
        });
    }

    @Test
    void removeOwner_WhenHasNoCats_ShouldBeSuccessful() {
        long ownerID = 1;
        Owner owner = new Owner();
        owner.setId(ownerID);
        owner.setCats(cats);

        // mock that there's owner with ID=1
        Mockito.doReturn(owner).when(ownerRepository).getOwnerById(ownerID);

        // mock that owner has no cats
        Mockito.doReturn(true).when(cats).isEmpty();

        // do not call real database
        Mockito.doNothing().when(ownerRepository).deleteOwner(Mockito.any());

        ownerService.removeOwner(ownerID);

        Mockito.verify(ownerRepository).deleteOwner(owner);
    }
}
