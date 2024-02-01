package ru.clevertec.house.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.clevertec.house.config.PostgresSQLContainerInitializer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HouseHistoryIntegrationTest extends PostgresSQLContainerInitializer {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldGetOwnerHouses() throws Exception {
        //given
        int pageSize = 2;
        int numberPage = 1;
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"uuid\":" +
                "\"5e213358-c398-49be-945b-e2b32a0c4a41\",\"area\":\"Suburb\",\"country\":" +
                "\"Country1\",\"city\":\"City1\",\"street\":\"Street1\",\"number\":\"1\"," +
                "\"createDate\":\"2024-01-16T14:18:08.537\"},{\"uuid\":\"e96346e9-9239-44f8-be11-fcbc86a541eb\"," +
                "\"area\":\"Downtown\",\"country\":\"Country2\",\"city\":\"City2\",\"street\":\"Street2\"," +
                "\"number\":\"2\",\"createDate\":\"2024-01-16T14:18:08.537\"}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/history/houses/owners/5c3a267c-3175-4826-a7d1-488782a62d98")
                        .contentType("application/json")
                        .param("pageSize", String.valueOf(pageSize))
                        .param("numberPage", String.valueOf(numberPage)))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void shouldGetTenantHouses() throws Exception {
        //given
        int pageSize = 2;
        int numberPage = 1;
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"uuid\":" +
                "\"5e213358-c398-49be-945b-e2b32a0c4a41\",\"area\":\"Suburb\",\"country\":" +
                "\"Country1\",\"city\":\"City1\",\"street\":\"Street1\",\"number\":\"1\"," +
                "\"createDate\":\"2024-01-16T14:18:08.537\"},{\"uuid\":\"e96346e9-9239-44f8-be11-fcbc86a541eb\"," +
                "\"area\":\"Downtown\",\"country\":\"Country2\",\"city\":\"City2\",\"street\":\"Street2\"," +
                "\"number\":\"2\",\"createDate\":\"2024-01-16T14:18:08.537\"}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/history/houses/tenants/5c3a267c-3175-4826-a7d1-488782a62d98")
                        .contentType("application/json")
                        .param("pageSize", String.valueOf(pageSize))
                        .param("numberPage", String.valueOf(numberPage)))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void shouldRetrieveOwners() throws Exception {
        //given
        int pageSize = 3;
        int numberPage = 1;
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"uuid\":" +
                "\"5c3a267c-3175-4826-a7d1-488782a62d98\",\"name\":\"John\",\"surname\":" +
                "\"Doe\",\"sex\":\"MALE\",\"passport\":{\"series\":\"AB\",\"number\":" +
                "\"123456\"},\"createDate\":\"2024-01-16T14:18:08.537\",\"updateDate\":" +
                "\"2024-01-16T14:18:08.537\"},{\"uuid\":\"5ec11abf-be3d-440d-893b-d6471c2d4c2b\",\"name\":" +
                "\"Jane\",\"surname\":\"Doe\",\"sex\":\"FEMALE\",\"passport\":{\"series\":" +
                "\"BB\",\"number\":\"789012\"},\"createDate\":\"2024-01-16T14:18:08.537\",\"updateDate\":" +
                "\"2024-01-16T14:18:08.537\"},{\"uuid\":\"6fc2a42f-7ca0-4026-8db9-4e6a87e77b2b\",\"name\":" +
                "\"Michael\",\"surname\":\"Smith\",\"sex\":\"MALE\",\"passport\":{\"series\":\"CC\",\"number\":" +
                "\"111111\"},\"createDate\":\"2024-01-16T14:18:08.537\",\"updateDate\":\"2024-01-16T14:18:08.537\"}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/history/people/owners/5e213358-c398-49be-945b-e2b32a0c4a41")
                        .contentType("application/json")
                        .param("pageSize", String.valueOf(pageSize))
                        .param("numberPage", String.valueOf(numberPage)))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void shouldRetrieveTenants() throws Exception {
        //given
        int pageSize = 3;
        int numberPage = 1;
        String expected = "{\"pageNumber\":1,\"countPage\":1,\"content\":[{\"uuid\":" +
                "\"5c3a267c-3175-4826-a7d1-488782a62d98\",\"name\":\"John\",\"surname\":" +
                "\"Doe\",\"sex\":\"MALE\",\"passport\":{\"series\":\"AB\",\"number\":" +
                "\"123456\"},\"createDate\":\"2024-01-16T14:18:08.537\",\"updateDate\":" +
                "\"2024-01-16T14:18:08.537\"},{\"uuid\":\"5ec11abf-be3d-440d-893b-d6471c2d4c2b\",\"name\":" +
                "\"Jane\",\"surname\":\"Doe\",\"sex\":\"FEMALE\",\"passport\":{\"series\":" +
                "\"BB\",\"number\":\"789012\"},\"createDate\":\"2024-01-16T14:18:08.537\",\"updateDate\":" +
                "\"2024-01-16T14:18:08.537\"},{\"uuid\":\"6fc2a42f-7ca0-4026-8db9-4e6a87e77b2b\",\"name\":" +
                "\"Michael\",\"surname\":\"Smith\",\"sex\":\"MALE\",\"passport\":{\"series\":\"CC\",\"number\":" +
                "\"111111\"},\"createDate\":\"2024-01-16T14:18:08.537\",\"updateDate\":\"2024-01-16T14:18:08.537\"}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/history/people/tenants/5e213358-c398-49be-945b-e2b32a0c4a41")
                        .contentType("application/json")
                        .param("pageSize", String.valueOf(pageSize))
                        .param("numberPage", String.valueOf(numberPage)))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }
}
