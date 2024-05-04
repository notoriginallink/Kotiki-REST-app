package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.tolstov.entities.Cat;
import ru.tolstov.models.CatColor;
import ru.tolstov.entities.Owner;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.repositories.OwnerRepository;
import ru.tolstov.services.CatServiceImpl;
import ru.tolstov.services.dto.CatDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CatServiceTests {
    @Mock
    private CatRepository catRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @InjectMocks
    private CatServiceImpl catService;

    private Owner testOwner;
    private Cat testCat;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        testOwner = new Owner();
        testOwner.setId(1L);
        testOwner.setLastName("Aboba");
        testOwner.setFirstName("Amogus");
        testOwner.setCats(new ArrayList<>());

        testCat = new Cat();
        testCat.setId(1L);
        testCat.setName("Biba");
        testCat.setBreed("Siamese");
        testCat.setColor(CatColor.ORANGE);
        testCat.setOwner(testOwner);
        testCat.setFriends(new HashSet<>());

        Mockito.when(ownerRepository.findById(1L)).thenReturn(Optional.ofNullable(testOwner));
        Mockito.when(catRepository.findById(1L)).thenReturn(Optional.ofNullable(testCat));
    }
    @Test
    public void getCatById_WhenPresent_ShouldReturnDto() {
        var expectedDto = new CatDto(testCat);

        var actualOptional = catService.getCatByID(1L);
        assertTrue(actualOptional.isPresent());

        var actualDto = actualOptional.get();

        assertEquals(expectedDto, actualDto);
    }
}
