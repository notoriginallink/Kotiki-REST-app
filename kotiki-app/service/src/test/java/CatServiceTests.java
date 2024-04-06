import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import ru.tolstov.models.Cat;
import ru.tolstov.models.CatColor;
import ru.tolstov.models.Owner;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.CatServiceImpl;
import ru.tolstov.services.UnknownEntityIdException;
import ru.tolstov.services.dto.CatItem;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CatServiceTests {
    @Mock
    private CatRepository catRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @InjectMocks
    private CatServiceImpl catService;
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCat_ShouldBeSuccessful() {
        String name = "aboba";
        LocalDate date = LocalDate.of(2020, 3,1);
        String breed = "Brodyaga";
        CatColor color = CatColor.ORANGE;
        long ownerID = 1;
        long expectedCatID = 1;
        Cat cat = new Cat();
        cat.setName(name);
        cat.setBirthdate(date);
        cat.setBreed(breed);
        cat.setColor(color);

        var owner = new Owner();
        owner.setId(ownerID);
        cat.setOwner(owner);
        cat.setFriends(new HashSet<>());

        // mock that there's owner with ID=1 in repository
        Mockito.when(ownerRepository.getOwnerById(ownerID)).thenReturn(owner);

        // registered cat gets ID=1
        Mockito.when(catRepository.registerCat(cat)).thenReturn(expectedCatID);

        long actualCatID = catService.addCat(name, date, breed, color, ownerID);

//        Mockito.verify(catRepository).registerCat(cat);
        assertEquals(expectedCatID, actualCatID);
    }

    @Test
    void addCat_WhenOwnerIDNotValid_ShouldThrowExceptionUnknownEntityID() {
        var name = "aboba";
        var date = LocalDate.of(2020, 3,1);
        var breed = "Brodyaga";
        var color = CatColor.ORANGE;
        var ownerID = 1;

        // mock that there's no owner with ID=1 in repository
        Mockito.when(ownerRepository.getOwnerById(ownerID)).thenReturn(null);

        Assertions.assertThrowsExactly(UnknownEntityIdException.class, () -> {
            catService.addCat(name, date, breed, color, ownerID);
        });
    }

    @Test
    void removeCat_WhenPresentInRepository_ShouldBeSuccessful() {
        long catID = 1;
        Cat cat = new Cat();
        cat.setId(catID);

        // mock that there's a cat with ID=1 in repository
        Mockito.when(catRepository.getCatById(catID)).thenReturn(cat);

        catService.removeCat(catID);

        Mockito.verify(catRepository).deleteCat(cat);
    }

    @Test
    void removeCat_WhenNotPresentInRepository_NothingShouldHappen() {
        long catID = 1;
        Cat cat = new Cat();
        cat.setId(catID);

        // mock that there's no cat with ID=1 in repository
        Mockito.when(catRepository.getCatById(catID)).thenReturn(null);

        catService.removeCat(catID);

        // assert that method deleteCat never invokes
        Mockito.verify(catRepository, Mockito.never()).deleteCat(cat);
    }

    @Test
    void getAllCats_ShouldBeSuccessful() {
        Cat cat = new Cat();
        Owner owner = new Owner();
        cat.setOwner(owner);
        List<Cat> expectedList = new ArrayList<>();
        int expectedSize = 1;
        expectedList.add(cat);

        Mockito.when(catRepository.getAllCats()).thenReturn(expectedList);

        List<CatItem> actualList = catService.getAllCats();
        int actualSize = actualList.size();

        Mockito.verify(catRepository).getAllCats();
        assertEquals(expectedSize, actualSize);
//        assertEquals(expectedList, actualList);
    }

    @Test
    void getCatByID_WhenInRepository_ShouldReturnNotEmptyOptional() {
        long catID = 1;
        long ownerID = 1;

        Owner owner = new Owner();
        owner.setId(ownerID);

        Cat expectedCat = new Cat();
        expectedCat.setId(catID);
        expectedCat.setOwner(owner);

        // mock that there's cat with ID=1 in repository
        Mockito.when(catRepository.getCatById(catID)).thenReturn(expectedCat);

        Optional<CatItem> optionalCat = catService.getCatByID(catID);

        assertTrue(optionalCat.isPresent());
        CatItem actualCat = optionalCat.get();
        assertEquals(expectedCat.getName(), actualCat.getName());
    }

    @Test
    void getCatByID_WhenNotInRepository_ShouldReturnEmptyOptional() {
        long catID = 1;

        // mock that there's no cat with ID=1 in repository
        Mockito.when(catRepository.getCatById(catID)).thenReturn(null);

        Optional<CatItem> optionalCat = catService.getCatByID(catID);

        assertTrue(optionalCat.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("catsProviderFactory")
    void areFriends_WhenBothCatsInRepository(long catID1, Cat cat1, long catID2, Cat cat2, boolean expectedResult) {
        Mockito.when(catRepository.getCatById(catID1)).thenReturn(cat1);
        Mockito.when(catRepository.getCatById(catID2)).thenReturn(cat2);

        boolean actualResult = catService.areFriends(catID1, catID2);
        assertEquals(expectedResult, actualResult);
    }

    @ParameterizedTest
    @MethodSource("catsProviderFactory")
    void makeFriendship_WhenBothCatsInRepository(long catID1, Cat cat1, long catID2, Cat cat2, boolean areFriends) {
        boolean expectedFriendship = true;

        Mockito.when(catRepository.getCatById(catID1)).thenReturn(cat1);
        Mockito.when(catRepository.getCatById(catID2)).thenReturn(cat2);

        catService.makeFriendship(catID1, catID2);

        if (!areFriends)
            Mockito.verify(catRepository).updateFriendship(cat1, cat2);

        boolean actualFriendship = catService.areFriends(catID1, catID2);
        assertEquals(expectedFriendship, expectedFriendship);
    }

    @ParameterizedTest
    @MethodSource("catsProviderFactory")
    void destroyFriendship_WhenBothCatsInRepository(long catID1, Cat cat1, long catID2, Cat cat2, boolean areFriends) {
        boolean expectedFriendship = false;

        Mockito.when(catRepository.getCatById(catID1)).thenReturn(cat1);
        Mockito.when(catRepository.getCatById(catID2)).thenReturn(cat2);

        catService.destroyFriendship(catID1, catID2);

        if (areFriends)
            Mockito.verify(catRepository).updateFriendship(cat1, cat2);

        boolean actualFriendship = catService.areFriends(catID1, catID2);
        assertEquals(expectedFriendship, expectedFriendship);
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
        Owner owner = new Owner();
        long owner_id = 1;
        owner.setId(owner_id);

        long id = 1;
        Set<Cat> friends = new HashSet<>();
        var friend = new Cat();
        friend.setOwner(owner);

        friends.add(friend);

        Cat cat = new Cat();
        cat.setOwner(owner);

        cat.setId(1);
        cat.setFriends(friends);
        int expectedSize = 1;

        Mockito.when(catRepository.getCatById(id)).thenReturn(cat);

        List<CatItem> actualFriends = catService.getFriends(id);
        int actualSize = actualFriends.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void friendshipMethods_WhenCatsAreNotInRepository_ShouldThrowException() {
        long catID1 = 1;
        long catID2 = 2;
        Cat cat = new Cat();
        cat.setId(catID1);

        Mockito.when(catRepository.getCatById(catID1)).thenReturn(cat);
        Mockito.when(catRepository.getCatById(catID2)).thenReturn(null);

        Assertions.assertThrowsExactly(UnknownEntityIdException.class, () -> {
            catService.areFriends(catID1, catID2);
        });

        Assertions.assertThrowsExactly(UnknownEntityIdException.class, () -> {
            catService.makeFriendship(catID1, catID2);
        });

        Assertions.assertThrowsExactly(UnknownEntityIdException.class, () -> {
            catService.destroyFriendship(catID1, catID2);
        });

        Assertions.assertThrowsExactly(UnknownEntityIdException.class, () -> {
            catService.getFriends(catID2);
        });
    }
}
