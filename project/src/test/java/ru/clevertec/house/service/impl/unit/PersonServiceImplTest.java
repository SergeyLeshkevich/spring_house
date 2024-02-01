package ru.clevertec.house.service.impl.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.exception.NotFoundException;
import ru.clevertec.house.exception.PassportUniqueException;
import ru.clevertec.house.mapper.PersonMapper;
import ru.clevertec.house.repository.PersonRepository;
import ru.clevertec.house.service.impl.HouseServiceImpl;
import ru.clevertec.house.service.impl.PersonServiceImpl;
import ru.clevertec.house.util.HouseTest;
import ru.clevertec.house.util.PersonTest;
import ru.clevertec.house.util.RequestPersonTest;
import ru.clevertec.house.util.ResponsePersonTest;
import ru.clevertec.house.util.TestConstant;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    HouseServiceImpl houseService;

    @Mock
    PersonMapper personMapper;

    @InjectMocks
    PersonServiceImpl personService;

    @Captor
    ArgumentCaptor<Person> personTestCaptor;

    private final HouseTest houseTest = new HouseTest();
    private final PersonTest personTest = new PersonTest();
    private final ResponsePersonTest responsePersonTest = new ResponsePersonTest();
    private final RequestPersonTest requestPersonTest = new RequestPersonTest();


    @Test
    void shouldGetPersonByUuid() {
        //given
        Person person = personTest.build();
        when(personRepository.findByUuid(TestConstant.PERSON_UUID)).thenReturn(Optional.of(person));
        when(personMapper.toDto(person)).thenReturn(responsePersonTest.build());

        //when
        ResponsePerson result = personService.get(TestConstant.PERSON_UUID);

        //then
        assertThat(result).isNotNull();
        verify(personRepository).findByUuid(TestConstant.PERSON_UUID);
        verify(personMapper).toDto(person);
    }

    @Test
    void shouldGetAllPersons() {
        //given
        Page<Person> page = Page.empty();
        page.map(person -> personTest.build());
        when(personRepository.findPagePeople(1, 1)).thenReturn(page);
        when(personMapper.toDtoList(page.getContent())).thenReturn(List.of(responsePersonTest.build()));

        //when
        PaginationResponse<ResponsePerson> actual = personService.getAll(1, 1);

        //then
        assertThat(actual).isEqualTo(new PaginationResponse<>(1, 1, List.of(responsePersonTest.build())));
        verify(personRepository).findPagePeople(1, 1);
        verify(personMapper).toDtoList(page.getContent());
    }

    @Test
    void shouldCreatePerson() {
        //given
        RequestPerson requestPerson = requestPersonTest.build();
        House house = houseTest.build();
        Person person = personTest.build();
        when(houseService.getHouseByUuid(TestConstant.HOUSE_UUID)).thenReturn(house);
        when(personRepository.findByPassport_SeriesAndPassport_Number(TestConstant.PASSPORT_SERIES, TestConstant.PASSPORT_NUMBER))
                .thenReturn(Optional.empty());
        when(personMapper.toEntity(requestPerson)).thenReturn(personTest.build());
        when(personRepository.save(any())).thenReturn(person);
        when(personRepository.findByUuid(any())).thenReturn(Optional.empty());
        when(personMapper.toDto(any())).thenReturn(responsePersonTest.build());

        //when
        ResponsePerson actual = personService.create(requestPerson, TestConstant.HOUSE_UUID);

        //then
        assertThat(actual.name()).isEqualTo(TestConstant.NAME);
        assertThat(actual.sex()).isEqualTo(TestConstant.SEX);
        assertThat(actual.surname()).isEqualTo(TestConstant.SURNAME);
        assertThat(actual.uuid()).isNotNull();
        verify(houseService).getHouseByUuid(TestConstant.HOUSE_UUID);
        verify(personMapper).toEntity(requestPerson);
        verify(personRepository).save(any());
    }


    @Test
    void shouldDeletePerson() {
        //given,when
        personService.delete(TestConstant.PERSON_UUID);

        //then
        verify(personRepository, times(1)).deleteByUuid(TestConstant.PERSON_UUID);
        verify(houseService, times(1)).removeOwnerByUuid(TestConstant.PERSON_UUID);
    }

    @Test
    void shouldThrowPassportValidExceptionForInvalidPassportOnCreate() {
        //given
        RequestPerson requestPerson = requestPersonTest.build();
        House house = houseTest.build();
        when(houseService.getHouseByUuid(TestConstant.HOUSE_UUID)).thenReturn(house);
        when(personRepository.findByPassport_SeriesAndPassport_Number(TestConstant.PASSPORT_SERIES, TestConstant.PASSPORT_NUMBER))
                .thenReturn(Optional.of(personTest.build()));

        //when,then
        assertThatThrownBy(
                () -> personService.create(requestPerson, TestConstant.HOUSE_UUID))
                .isInstanceOf(PassportUniqueException.class)
                .hasMessage("Passport with this number has already been saved");
        verify(houseService, times(1)).getHouseByUuid(TestConstant.HOUSE_UUID);
    }

    @Test
    void shouldThrowNotFoundExceptionForMissingPersonOnUpdate() {
        //given
        RequestPerson requestPerson = requestPersonTest.build();
        when(personRepository.findByUuid(TestConstant.PERSON_UUID)).thenReturn(Optional.empty());

        //when,then
        assertThatThrownBy(
                () -> personService.update(TestConstant.PERSON_UUID, requestPerson))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Person with 5c3a267c-3175-4826-a7d1-488782a62d98 not found");
        verify(personRepository, times(1)).findByUuid(TestConstant.PERSON_UUID);
        verifyNoMoreInteractions(houseService, personMapper, personRepository);
    }

    @Test
    void shouldUpdatePerson() {
        //given
        RequestPerson requestPerson = requestPersonTest.build();
        Person person = personTest.build();
        when(personMapper.merge(person, requestPerson)).thenReturn(personTest.build());
        when(personRepository.save(any())).thenReturn(person);
        when(personRepository.findByUuid(TestConstant.PERSON_UUID)).thenReturn(Optional.of(person));
        when(personMapper.toDto(person)).thenReturn(responsePersonTest.build());

        //when
        ResponsePerson actual = personService.update(TestConstant.PERSON_UUID, requestPerson);

        //then
        assertThat(actual.name()).isEqualTo(TestConstant.NAME);
        assertThat(actual.sex()).isEqualTo(TestConstant.SEX);
        assertThat(actual.surname()).isEqualTo(TestConstant.SURNAME);
        assertThat(actual.uuid()).isNotNull();
        assertThat(actual.updateDate()).isNotNull();
        verify(personMapper).merge(person, requestPerson);
        verify(personRepository).save(any());
        verify(personRepository).findByUuid(any());
    }

    @Test
    void shouldReturnListOfResponsePersonsForGetPeopleByUuidHouse() {
        //given
        ResponsePerson expected = responsePersonTest.build();
        when(personRepository.findAllByHouse_Uuid(TestConstant.HOUSE_UUID)).thenReturn(List.of(personTest.build()));
        when(personMapper.toDtoList(List.of(personTest.build()))).thenReturn(List.of(expected));

        //when
        List<ResponsePerson> actual = personService.getPeopleByUuidHouse(TestConstant.HOUSE_UUID);

        //then
        assertThat(actual).isEqualTo(List.of(expected));
    }

    @Test
    void shouldUpdatePersonFieldsAndReturnResponsePersonOnPatch() {
        //given
        Map<String, Object> updates = Map.of(
                "name", "Name"
        );
        when(personRepository.findByUuid(TestConstant.PERSON_UUID)).thenReturn(Optional.of(personTest.build()));

        //when
        personService.patch(TestConstant.PERSON_UUID, updates);

        //then
        verify(personRepository).save(personTestCaptor.capture());
        Person actual = personTestCaptor.getValue();
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(TestConstant.ID);
        assertThat(actual.getUuid()).isEqualTo(TestConstant.PERSON_UUID);
        assertThat(actual.getName()).isEqualTo("Name");
        assertThat(actual.getSurname()).isEqualTo(TestConstant.SURNAME);
        assertThat(actual.getCreateDate()).isEqualTo(TestConstant.LOCAL_DATE_TIME);
    }

    @Test
    void shouldGetAllPeopleById() {
        //given
        List<Integer> allIdPeople = List.of(1);
        int pageSize = 1;
        int numberPage = 1;
        List<Person> samplePersonList = List.of(personTest.build());
        Page<Person> expectedPage = new PageImpl<>(samplePersonList);
        when(personRepository.findDistinctByIdIs(allIdPeople, PageRequest.of(numberPage-1, pageSize)))
                .thenReturn(expectedPage);
        List<ResponsePerson> exspectedList = List.of(responsePersonTest.build());
        when(personMapper.toDtoList(samplePersonList)).thenReturn(exspectedList);

        //when
        PaginationResponse<ResponsePerson> actual = personService.getAllPeopleById(allIdPeople, pageSize, numberPage);

        //then
        assertThat(actual.getContent()).isEqualTo(exspectedList);
        assertThat(actual.getCountPage()).isEqualTo(expectedPage.getTotalPages());
        assertThat(actual.getPageNumber()).isEqualTo(numberPage);
        verify(personRepository).findDistinctByIdIs(allIdPeople, PageRequest.of(numberPage - 1, pageSize));
    }
}