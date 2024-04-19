package controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.tolstov.Main;
import ru.tolstov.models.CatColor;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class CatControllerTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void getAll_thenStatus200() throws Exception {
        int expectedSize = 5;

        mvc.perform(get("/cats/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(expectedSize))
        );
    }

    @Test
    public void getById_whenExists_thenStatus200() throws Exception {
        long id = 2;
        mvc.perform(get("/cats/id%s".formatted(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Stepa"));
    }

    @Test
    public void getFiltered_ByColorAndBreed_thenStatus200() throws Exception {
        CatColor color = CatColor.GREY;
        String breed = "Siamese";
        mvc.perform(get("/cats/filter?color=%s&breed=%s".formatted(color, breed))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].color", is(color.toString())))
                .andExpect(jsonPath("[0].breed").value(breed));
    }
}
