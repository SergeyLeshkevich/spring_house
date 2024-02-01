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
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.util.RequestHouseTest;
import ru.clevertec.house.util.TestConstant;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HouseIntegrationTest extends PostgresSQLContainerInitializer {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    private final RequestHouseTest requestHouseTest = new RequestHouseTest();

    @Test
    void shouldRetrieveHouse() throws Exception {
        //given,when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/houses/5e213358-c398-49be-945b-e2b32a0c4a41")
                        .contentType("application/json"))
                .andReturn();
        ResponseHouse actual = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseHouse.class);

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.country()).isEqualTo(TestConstant.COUNTRY);
        assertThat(actual.city()).isEqualTo(TestConstant.CITY);
        assertThat(actual.uuid()).isEqualTo(TestConstant.HOUSE_UUID);
        assertThat(actual.area()).isEqualTo(TestConstant.AREA);
        assertThat(actual.street()).isEqualTo(TestConstant.STREET);
        assertThat(actual.number()).isEqualTo(TestConstant.NUMBER);
        assertThat(actual.createDate()).isEqualTo(TestConstant.DATE_TIME);
    }

    @Test
    void shouldCreateNewHouse() throws Exception {
        //given,when
        RequestHouse requestHouse = requestHouseTest.build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/houses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestHouse)))
                .andReturn();
        ResponseHouse actual = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseHouse.class);

        //then
        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(actual.country()).isEqualTo(TestConstant.COUNTRY);
        assertThat(actual.city()).isEqualTo(TestConstant.CITY);
        assertThat(actual.uuid()).isNotNull();
        assertThat(actual.area()).isEqualTo(TestConstant.AREA);
        assertThat(actual.street()).isEqualTo(TestConstant.STREET);
        assertThat(actual.number()).isEqualTo(TestConstant.NUMBER);
        assertThat(actual.createDate()).isNotNull();
    }

    @Test
    void shouldUpdateHouseDetails() throws Exception {
        //given,when
        String jsonForTest = "{\n" +
                "    \"area\": \"Suburb\",\n" +
                "    \"country\": \"Country1\",\n" +
                "    \"city\": \"City1\",\n" +
                "    \"street\": \"Street1\",\n" +
                "    \"number\": \"1\"\n" +
                "}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/houses/f49da0fe-196f-493b-b837-22f0dbede818")
                        .contentType("application/json")
                        .content(jsonForTest))
                .andReturn();
        ResponseHouse actual = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseHouse.class);

        //then
        assertThat(actual.country()).isEqualTo("Country1");
        assertThat(actual.city()).isEqualTo("City1");
        assertThat(actual.uuid()).isNotNull();
        assertThat(actual.area()).isEqualTo("Suburb");
        assertThat(actual.street()).isEqualTo("Street1");
        assertThat(actual.number()).isEqualTo("1");
        assertThat(actual.createDate()).isNotNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void shouldDeleteHouse() throws Exception {
        //given,when
        MvcResult resultTrue = mockMvc.perform(MockMvcRequestBuilders.delete("/houses/a541402f-d291-48e8-9b34-78c925621749")
                        .contentType("application/json"))
                .andReturn();
        MvcResult resultFalse = mockMvc.perform(MockMvcRequestBuilders.get("/houses/a541402f-d291-48e8-9b34-78c925621749")
                        .contentType("application/json"))
                .andReturn();

        //then
        assertThat(resultTrue.getResponse().getStatus()).isEqualTo(204);
        assertThat(resultFalse.getResponse().getStatus()).isEqualTo(404);
    }

    @Test
    void shouldRetrieveAllHouses() throws Exception {
        //given
        int pageSize = 1;
        int numberPage = 2;
        String expected = "{\"pageNumber\":2,\"countPage\":6,\"content\":[{\"uuid\":" +
                "\"e96346e9-9239-44f8-be11-fcbc86a541eb\",\"area\":\"Downtown\",\"country\":" +
                "\"Country2\",\"city\":\"City2\",\"street\":\"Street2\",\"number\":\"2\",\"createDate\":" +
                "\"2024-01-16T14:18:08.537\"}]}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/houses")
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
    void shouldRetrieveHousesByOwnerUuid() throws Exception {
        //given
        String expected = "[{\"uuid\":\"5e213358-c398-49be-945b-e2b32a0c4a41\",\"area\":" +
                "\"Suburb\",\"country\":\"Country1\",\"city\":\"City1\",\"street\":\"Street1\",\"number\":" +
                "\"1\",\"createDate\":\"2024-01-16T14:18:08.537\"},{\"uuid\":\"e96346e9-9239-44f8-be11-fcbc86a541eb\"," +
                "\"area\":\"Downtown\",\"country\":\"Country2\",\"city\":\"City2\",\"street\":\"Street2\"," +
                "\"number\":\"2\",\"createDate\":\"2024-01-16T14:18:08.537\"}]";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/houses/owners/5ec11abf-be3d-440d-893b-d6471c2d4c2b")
                        .contentType("application/json"))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void shouldPatchHouseDetails() throws Exception {
        //given
        Map<String, Object> updates = Map.of("country", "Country123");

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/houses/5e213358-c398-49be-945b-e2b32a0c4a41")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updates)))
                .andReturn();
        ResponseHouse actual = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseHouse.class);

        //then
        assertThat(actual.area()).isEqualTo("Suburb");
        assertThat(actual.country()).isEqualTo("Country123");
        assertThat(actual.uuid()).isEqualTo(UUID.fromString("5e213358-c398-49be-945b-e2b32a0c4a41"));
        assertThat(actual.city()).isEqualTo("City1");
        assertThat(actual.street()).isEqualTo("Street1");
        assertThat(actual.number()).isEqualTo("1");
        assertThat(actual.createDate()).isEqualTo("2024-01-16T14:18:08.537");
    }

    @Test
    void shouldAddTenantToHouse() throws Exception {
        //given
        String expected = "{\"uuid\":\"1f5e112c-ebdc-4935-ae07-ee05eb1a2de5\",\"area\":\"Suburb\"," +
                "\"country\":\"Country3\",\"city\":\"City3\",\"street\":\"Street3\",\"number\":\"3\"," +
                "\"createDate\":\"2024-01-16T14:18:08.537\"}";

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/houses/tenants")
                        .contentType("application/json")
                        .param("house_uuid", "1f5e112c-ebdc-4935-ae07-ee05eb1a2de5")
                        .param("person_uuid", "b607f4b5-d383-40e3-af42-5aac0f1d653e"))
                .andReturn();
        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }
}
