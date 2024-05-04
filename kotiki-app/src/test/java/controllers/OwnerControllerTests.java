package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.tolstov.Main;
import ru.tolstov.config.SecurityConfig;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Main.class, SecurityConfig.class})
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OwnerControllerTests {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    public static ObjectMapper mapper;

    @BeforeAll
    public static void init() {
        mapper = new JsonMapper();

        mapper
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setup(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getById_whenExists_thenStatus200() throws Exception {
        long id = 1;
        mvc.perform(get("/owners/%s".formatted(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "Danya", userDetailsServiceBeanName = "userDetailsService")
    public void getById_whenExistsAndItself_thenStatus200() throws Exception {
        long id = 2;
        mvc.perform(get("/owners/%s".formatted(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "Danya", userDetailsServiceBeanName = "userDetailsService")
    public void getById_whenExistsAndNotItself_thenStatus400() throws Exception {
        long id = 1;
        mvc.perform(get("/owners/%s".formatted(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getById_whenNotExists_thenStatus404() throws Exception {
        long id = 100;
        mvc.perform(get("/owners/%s".formatted(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getCats_whenOwnerExists_thenStatus200() throws Exception {
        long id = 2;
        int expectedSize = 3;

        mvc.perform(get("/owners/%s/cats".formatted(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedSize)));
    }
}
