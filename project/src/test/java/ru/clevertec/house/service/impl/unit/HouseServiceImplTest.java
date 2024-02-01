package ru.clevertec.house.service.impl.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.exception.BadRequestException;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.mapper.HouseMapper;
import ru.clevertec.house.repository.HouseRepository;
import ru.clevertec.house.service.PersonService;
import ru.clevertec.house.service.impl.HouseServiceImpl;
import ru.clevertec.house.util.HouseTest;
import ru.clevertec.house.util.PaginationResponse;
import ru.clevertec.house.util.PersonTest;
import ru.clevertec.house.util.RequestHouseTest;
import ru.clevertec.house.util.ResponseHouseTest;
import ru.clevertec.house.util.TestConstant;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private PersonService personService;

    @Mock
    private HouseMapper houseMapper;

    @InjectMocks
    private HouseServiceImpl houseService;

    private final HouseTest houseTest = new HouseTest();
    private final ResponseHouseTest responseHouseTest = new ResponseHouseTest();
    private final RequestHouseTest requestHouseTest = new RequestHouseTest();
    private final PersonTest personTest = new PersonTest();

    @Test
    void shouldGetExistingHouse() {
        //given
        House house = houseTest.build();
        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.of(house));
        when(houseMapper.toDto(house)).thenReturn(responseHouseTest.build());

        //when
        ResponseHouse responseHouse = houseService.get(TestConstant.HOUSE_UUID);

        //then
        assertThat(responseHouse).isNotNull();
        verify(houseRepository).findByUuid(TestConstant.HOUSE_UUID);
        verify(houseMapper).toDto(houseTest.build());
    }

    @Test
    void shouldThrowNotFoundExceptionForMissingHouse() {
        //given
        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(
                () -> houseService.get(TestConstant.HOUSE_UUID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("House with 5e213358-c398-49be-945b-e2b32a0c4a41 not found");

        //then
        verify(houseRepository).findByUuid(TestConstant.HOUSE_UUID);
        verifyNoMoreInteractions(houseMapper);
    }

    @Test
    void shouldGetAllHouses() {
        //given
        Page<House> page = Page.empty();
        page.map(h -> houseTest.build());
        when(houseRepository.findPageHouses(1, 1)).thenReturn(page);
        when(houseMapper.toDtoList(page.getContent())).thenReturn(Collections.singletonList(responseHouseTest.build()));

        //when
        PaginationResponse<ResponseHouse> actual = houseService.getAll(1, 1);

        //then
        assertThat(actual).isEqualTo(new PaginationResponse<>(1, 1, List.of(responseHouseTest.build())));
        verify(houseRepository).findPageHouses(1, 1);
    }

    @Test
    void shouldCreateHouse() {
        //given
        RequestHouse requestHouse = requestHouseTest.build();
        House house = houseTest.build();
        house.setCreateDate(LocalDateTime.now());
        when(houseMapper.toEntity(requestHouse)).thenReturn(house);
        when(houseMapper.toDto(house)).thenReturn(responseHouseTest.build());
        when(houseRepository.save(house)).thenReturn(house);
        when(houseRepository.findByUuid(any())).thenReturn(Optional.empty());

        //when
        ResponseHouse actual = houseService.create(requestHouse);

        //then
        assertThat(actual).isEqualTo(responseHouseTest.build());
        verify(houseRepository).save(house);
        verify(houseMapper).toDto(house);
        verify(houseMapper).toEntity(requestHouse);
    }

    @Test
    void shouldUpdateHouse() {
        //given
        RequestHouse requestHouse = requestHouseTest.build();
        House house = houseTest.build();
        when(houseRepository.findByUuid(house.getUuid())).thenReturn(Optional.of(house));
        when(houseMapper.toDto(house)).thenReturn(responseHouseTest.build());
        when(houseMapper.merge(house, requestHouse)).thenReturn(house);
        when(houseRepository.save(house)).thenReturn(house);

        //when
        ResponseHouse actual = houseService.update(TestConstant.HOUSE_UUID, requestHouse);

        //then
        assertThat(actual).isEqualTo(responseHouseTest.build());
        verify(houseRepository).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionForMissingHouseOnUpdate() {
        //given
        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(
                () -> houseService.get(TestConstant.HOUSE_UUID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("House with 5e213358-c398-49be-945b-e2b32a0c4a41 not found");

        //then
        verify(houseRepository).findByUuid(TestConstant.HOUSE_UUID);
        verifyNoMoreInteractions(houseMapper);
    }

    @Test
    void shouldDeleteHouse() {
        //given
        UUID uuid = TestConstant.HOUSE_UUID;
        House house = House.builder()
                .id(TestConstant.ID)
                .uuid(TestConstant.HOUSE_UUID)
                .build();
        when(houseRepository.findByUuid(uuid)).thenReturn(Optional.of(house));

        //when
        houseService.delete(uuid);

        //then
        verify(houseRepository).deleteHouseByUuid(uuid);
        verify(houseRepository).findByUuid(uuid);
    }

    @Test
    void shouldThrowBadRequestExceptionOnDeleteWithRegisteredPeople() {
        //given
        UUID uuid = TestConstant.HOUSE_UUID;
        House house = House.builder()
                .id(TestConstant.ID)
                .uuid(TestConstant.HOUSE_UUID)
                .owners(Set.of(Person.builder().build()))
                .build();
        when(houseRepository.findByUuid(uuid)).thenReturn(Optional.of(house));

        //when
        assertThatThrownBy(
                () -> houseService.delete(TestConstant.HOUSE_UUID))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The house cannot be removed with registered people");

        //then
        verify(houseRepository).findByUuid(uuid);
        verify(houseRepository, times(0)).deleteHouseByUuid(uuid);
    }

    @Test
    void shouldAddOwner() {
        //given
        Set<Person> set = new HashSet<>();
        set.add(personTest.build());
        House house = House.builder()
                .id(TestConstant.ID)
                .uuid(TestConstant.HOUSE_UUID)
                .owners(set)
                .build();
        when(personService.getPersonByUuid(TestConstant.PERSON_UUID)).thenReturn(personTest.build());
        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.of(house));
        when(houseRepository.save(house)).thenReturn(house);
        when(houseMapper.toDto(house)).thenReturn(responseHouseTest.build());

        //when
        houseService.addOwner(TestConstant.HOUSE_UUID, TestConstant.PERSON_UUID);

        //then
        verify(personService).getPersonByUuid(TestConstant.PERSON_UUID);
        verify(houseRepository).findByUuid(TestConstant.HOUSE_UUID);
        verify(houseRepository).save(house);
        verify(houseMapper).toDto(house);
    }

    @Test
    void shouldPatchHouse() {
        //given
        House house = houseTest.build();
        house.setCreateDate(LocalDateTime.now());
        ResponseHouse responseHouse = responseHouseTest.build();
        Map<String, Object> updateHouse = new HashMap<>();
        updateHouse.put("area", "area");
        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.of(house));
        when(houseRepository.save(house)).thenReturn(house);
        when(houseMapper.toDto(house)).thenReturn(responseHouse);

        //when
        ResponseHouse actual = houseService.patch(TestConstant.HOUSE_UUID, updateHouse);

        //then
        assertThat(actual).isEqualTo(responseHouse);
    }

    @Test
    void shouldThrowBadRequestExceptionOnInvalidFieldInPatch() {
        //given
        UUID houseUuid = UUID.randomUUID();
        Map<String, Object> updateHouse = new HashMap<>();
        updateHouse.put("invalidField", "Some value");
        when(houseRepository.findByUuid(houseUuid)).thenReturn(Optional.of(houseTest.build()));

        //when,then
        assertThrows(BadRequestException.class, () -> houseService.patch(houseUuid, updateHouse));
    }

    @Test
    void shouldGetHouseByUuid() {
        //given
        when(houseRepository.findByUuid(TestConstant.HOUSE_UUID)).thenReturn(Optional.of(houseTest.build()));

        //when
        House actual = houseService.getHouseByUuid(TestConstant.HOUSE_UUID);

        //then
        assertThat(actual).isEqualTo(houseTest.build());
    }

    @Test
    void shouldReturnCorrectPaginationResponseForAllHousesById() {
        //given
        List<Integer> allIdHouses = List.of(1);
        PageRequest pageRequest = PageRequest.of(4, 1);
        Page<House> page = Page.empty();
        page.map(h -> houseTest.build());
        when(houseRepository.findDistinctByIdIs(allIdHouses, pageRequest)).thenReturn(page);
        when(houseMapper.toDtoList(page.getContent())).thenReturn(List.of(responseHouseTest.build()));

        //when
        PaginationResponse<ResponseHouse> actual = houseService.getAllHousesById(allIdHouses, 1, 5);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.getCountPage()).isEqualTo(1);
        assertThat(actual.getPageNumber()).isEqualTo(5);
        assertThat(actual.getContent()).hasSize(1);
        assertThat(actual.getContent()).extracting(ResponseHouse::uuid).containsExactly(TestConstant.HOUSE_UUID);
    }
}




