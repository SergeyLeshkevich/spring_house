package ru.clevertec.house.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.config.PostgresSQLContainerInitializer;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.util.TestConstant;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends PostgresSQLContainerInitializer {

    private final TestEntityManager testEntityManager;
    private final PersonRepository personRepository;

    @Autowired
    public PersonRepositoryTest(TestEntityManager testEntityManager, PersonRepository personRepository) {
        this.testEntityManager = testEntityManager;
        this.personRepository = personRepository;
    }

    @Test
    void shouldDeletePersonByUuid() {
        //given
        personRepository.deleteByUuid(TestConstant.PERSON_UUID);

        //when
        Person actual = testEntityManager.find(Person.class, TestConstant.ID);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void shouldFindPersonByUuid() {
        //given
        Person expected = testEntityManager.find(Person.class, TestConstant.ID);

        //when
        Person actual = personRepository.findByUuid(TestConstant.PERSON_UUID).get();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindPagePersonsById() {
        //given
        List<Integer> listId = List.of(1, 2, 3);
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<Person> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(Person.class, 1));
        expectedList.add(testEntityManager.find(Person.class, 2));
        expectedList.add(testEntityManager.find(Person.class, 3));

        //when
        Page<Person> actual = personRepository.findDistinctByIdIs(listId, pageRequest);

        //then
        assertThat(actual.getTotalPages()).isEqualTo(1);
        assertThat(actual.getSize()).isEqualTo(3);
        assertThat(actual.getContent()).isEqualTo(expectedList);
    }

    @Test
    void shouldFindAllPersonsByHouseUuid() {
        //given,when
        List<Person> actual = personRepository.findAllByHouse_Uuid(TestConstant.HOUSE_UUID);

        //then
        assertThat(actual).hasSize(2);
    }

    @Test
    void shouldFindPersonByPassportSeriesAndNumber() {
        //given
        Person expected = testEntityManager.find(Person.class, TestConstant.ID);

        //when
        Person actual = personRepository
                .findByPassport_SeriesAndPassport_Number("AB",TestConstant.PASSPORT_NUMBER)
                .orElse(null);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
