package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.tolstov.Main;
import ru.tolstov.models.CatColor;
import ru.tolstov.repositories.CatRepository;
import ru.tolstov.services.dto.CatDto;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CatControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CatRepository catRepository;
    public static ObjectMapper mapper;

    @BeforeAll
    public static void init() {
        mapper = new JsonMapper();

        mapper
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());
    }

    @Test
    public void getAll_thenStatus200() throws Exception {
        int expectedSize = 5;

        mvc.perform(get("/cats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(expectedSize))
        );
    }

    @Test
    public void getById_whenExists_thenStatus200() throws Exception {
        long id = 2;
        mvc.perform(get("/cats/%s".formatted(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Stepa"));
    }

    @Test
    public void getFiltered_ByColorAndBreed_thenStatus200() throws Exception {
        CatColor color = CatColor.GREY;
        String breed = "Siamese";
        mvc.perform(get("/cats?color=%s&breed=%s".formatted(color, breed))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].color", is(color.toString())))
                .andExpect(jsonPath("[0].breed").value(breed));
    }

    @Test
    public void save_whenOwnerExists_thenStatus200() throws Exception {
        int initialSize = catRepository.findAll().size();
        int expectedSize = initialSize + 1;

        CatDto dto = new CatDto();
        dto.setName("Testy");
        dto.setBreed("Fluffy");
        dto.setColor(CatColor.BLACK);
        dto.setBirthdate(LocalDate.now());
        dto.setOwner(1L);
        String body = mapper.writeValueAsString(dto);

        mvc.perform(post("/cats")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        int actualSize = catRepository.findAll().size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void delete_whenExists_thenStatus200() throws Exception {
        long id = 3;

        int initialSize = catRepository.findAll().size();
        int expectedSize = initialSize - 1;
        var initialOptional = catRepository.findById(id);
        assertTrue(initialOptional.isPresent());

        mvc.perform(delete("/cats/%s".formatted(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        int actualSize = catRepository.findAll().size();
        var postOptional = catRepository.findById(id);

        assertEquals(expectedSize, actualSize);
        assertTrue(postOptional.isEmpty());
    }
}
