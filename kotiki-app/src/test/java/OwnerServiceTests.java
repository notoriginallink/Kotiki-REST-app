import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import ru.tolstov.models.Cat;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.OwnerServiceImpl;

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

    }

    @Test
    void getAllOwners_ShouldBeSuccessful() {

    }

    @Test
    void removeOwner_WhenNotInRepository_NothingShouldHappen() {

    }

    @Test
    void removeOwner_WhenHasCat_ShouldThrowException() {

    }

    @Test
    void removeOwner_WhenHasNoCats_ShouldBeSuccessful() {

    }
}
