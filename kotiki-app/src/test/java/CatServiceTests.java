import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import ru.tolstov.models.Cat;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.CatServiceImpl;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatServiceTests {
    private static CatRepository catRepository;
    private static OwnerRepository ownerRepository;
    private static EntityManagerFactory entityManagerFactory;
    private static CatServiceImpl catService;

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
        entityManagerFactory.close();
    }

    @Test
    void addCat_ShouldBeSuccessful() {
    }

    @Test
    void addCat_WhenOwnerIDNotValid_ShouldThrowExceptionUnknownEntityID() {

    }

    @Test
    void removeCat_WhenPresentInRepository_ShouldBeSuccessful() {

    }

    @Test
    void removeCat_WhenNotPresentInRepository_NothingShouldHappen() {

    }

    @Test
    void getAllCats_ShouldBeSuccessful() {

    }

    @Test
    void getCatByID_WhenInRepository_ShouldReturnNotEmptyOptional() {

    }

    @Test
    void getCatByID_WhenNotInRepository_ShouldReturnEmptyOptional() {

    }

    @ParameterizedTest
    @MethodSource("catsProviderFactory")
    void makeFriendship_WhenBothCatsInRepository(long catID1, Cat cat1, long catID2, Cat cat2, boolean areFriends) {

    }

    @ParameterizedTest
    @MethodSource("catsProviderFactory")
    void destroyFriendship_WhenBothCatsInRepository(long catID1, Cat cat1, long catID2, Cat cat2, boolean areFriends) {

    }

    static Stream<Object> catsProviderFactory() {
        long catID1 = 1;
        long catID2 = 2;
        long catID3 = 3;

        Cat cat1 = new Cat();
        cat1.setId(catID1);
        cat1.setFriends(new HashSet<>());

        Cat cat2 = new Cat();
        cat2.setId(catID2);
        cat2.setFriends(new HashSet<>());

        Cat cat3 = new Cat();
        cat3.setId(catID3);
        cat3.setFriends(new HashSet<>());

        // cats with ID=1 and ID=2 are friends
        cat1.getFriends().add(cat2);
        cat2.getFriends().add(cat1);

        // cats with ID=1 and ID=3 are friends
        cat1.getFriends().add(cat3);
        cat3.getFriends().add(cat1);

        // cats with ID=2 and ID=3 are not friends

        Stream<Arguments> s1 = Stream.of(Arguments.of(catID1, cat1, catID2, cat2, true));
        Stream<Arguments> s2 = Stream.of(Arguments.of(catID1, cat1, catID3, cat3, true));
        Stream<Arguments> s3 = Stream.of(Arguments.of(catID2, cat2, catID3, cat3, false));

        return Stream.concat(Stream.concat(s1, s2), s3);
    }

    @Test
    void getFriends_ShouldBeSuccessful() {

    }

    @Test
    void friendshipMethods_WhenCatsAreNotInRepository_ShouldThrowException() {

    }
}
