package ru.clevertec.house.service.impl.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.enums.Type;
import ru.clevertec.house.repository.HouseHistoryRepository;
import ru.clevertec.house.service.HouseService;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.service.impl.HouseHistoryServiceImpl;
import ru.clevertec.house.util.PaginationResponse;
import ru.clevertec.house.util.TestConstant;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseHistoryServiceImplTest {

    @Mock
    private PersonService personService;

    @Mock
    private HouseService houseService;

    @Mock
    private HouseHistoryRepository historyRepository;

    @InjectMocks
    private HouseHistoryServiceImpl houseHistoryService;

    @Test
    void shouldGetAllIdByUuidHousePeople() {
        //given
        UUID houseUuid = TestConstant.HOUSE_UUID;
        Type typeHistory = Type.OWNER;
        int pageSize = 10;
        int numberPage = 1;
        PaginationResponse<ResponsePerson> sampleResponse = new PaginationResponse<>();
        List<Integer> allIdPeople = List.of(1, 2, 3);
        when(historyRepository.findAllPeople(houseUuid, typeHistory.name())).thenReturn(allIdPeople);
        when(personService.getAllPeopleById(allIdPeople, pageSize, numberPage)).thenReturn(sampleResponse);

        //when
       houseHistoryService.getAllIdPeople(houseUuid, typeHistory, pageSize, numberPage);

       //then
        verify(historyRepository).findAllPeople(houseUuid, typeHistory.name());
        verify(personService).getAllPeopleById(allIdPeople, pageSize, numberPage);
    }

    @Test
    void shouldGetAllIdByUuidPersonHouses() {
        //given
        UUID personUuid = UUID.randomUUID();
        Type typeHistory = Type.OWNER;
        int pageSize = 10;
        int numberPage = 1;
        PaginationResponse<ResponseHouse> sampleResponse = new PaginationResponse<>();
        List<Integer> allIdHouses = List.of(1, 2, 3);
        when(historyRepository.findAllHouses(personUuid, typeHistory.name())).thenReturn(allIdHouses);
        when(houseService.getAllHousesById(allIdHouses, pageSize, numberPage)).thenReturn(sampleResponse);

        //when
        houseHistoryService.getAllIdHouses(personUuid, typeHistory, pageSize, numberPage);

        //then
        verify(historyRepository).findAllHouses(personUuid, typeHistory.name());
        verify(houseService).getAllHousesById(allIdHouses, pageSize, numberPage);
    }
}