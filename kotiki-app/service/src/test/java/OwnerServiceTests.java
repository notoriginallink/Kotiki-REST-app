import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.tolstov.models.Cat;
import ru.tolstov.models.Owner;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.OwnerServiceImpl;
import ru.tolstov.services.dto.OwnerItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OwnerServiceTests {
    @Mock
    private CatRepository catRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Spy
    List<Cat> cats;
    @InjectMocks
    private OwnerServiceImpl ownerService;
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOwner_ShouldBeSuccessful() {
        String firstName = "amogus";
        String lastName = "abobus";
        LocalDate birthdate = LocalDate.of(2020, 2, 2);
        long expectedID = 1;
        Owner owner = new Owner();

        Mockito.when(ownerRepository.registerOwner(owner)).thenReturn(expectedID);

        long actualID = ownerService.createOwner(firstName, lastName, birthdate);

        assertEquals(expectedID, actualID);
    }

    @Test
    void getAllOwners_ShouldBeSuccessful() {
        Owner owner = new Owner();
        List<Owner> expectedList = new ArrayList<>();
        int expectedSize = 1;
        expectedList.add(owner);

        Mockito.when(ownerRepository.getAllOwners()).thenReturn(expectedList);

        List<OwnerItem> actualList = ownerService.getAllOwners();
        int actualSize = actualList.size();

        assertEquals(expectedSize, actualSize);
//        assertEquals(expectedList, actualList);
    }

    @Test
    void removeOwner_WhenNotInRepository_NothingShouldHappen() {
        long ownerID = 1;
        Owner owner = new Owner();
        owner.setId(ownerID);

        // mock that there's no owner with ID=1
        Mockito.when(ownerRepository.getOwnerById(ownerID)).thenReturn(null);

        ownerService.removeOwner(ownerID);

        Mockito.verify(ownerRepository, Mockito.never()).deleteOwner(owner);
    }

    @Test
    void removeOwner_WhenHasCat_ShouldThrowException() {
        long ownerID = 1;
        Owner owner = new Owner();
        owner.setId(ownerID);
        owner.setCats(cats);

        // mock that there's owner with ID=1
        Mockito.when(ownerRepository.getOwnerById(ownerID)).thenReturn(owner);

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
        Mockito.when(ownerRepository.getOwnerById(ownerID)).thenReturn(owner);

        // mock that owner has no cats
        Mockito.doReturn(true).when(cats).isEmpty();

        ownerService.removeOwner(ownerID);

        Mockito.verify(ownerRepository).deleteOwner(owner);
    }
}
