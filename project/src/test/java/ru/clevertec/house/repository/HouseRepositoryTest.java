package ru.clevertec.house.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.house.config.PostgresSQLContainerInitializer;
import ru.clevertec.house.entity.House;
import ru.clevertec.house.entity.Person;
import ru.clevertec.house.util.PersonTest;
import ru.clevertec.house.util.TestConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HouseRepositoryTest extends PostgresSQLContainerInitializer {

    private final TestEntityManager testEntityManager;
    private final HouseRepository houseRepository;
    private final PersonTest personTest;

    @Autowired
    public HouseRepositoryTest(TestEntityManager testEntityManager, HouseRepository houseRepository) {
        this.testEntityManager = testEntityManager;
        this.houseRepository = houseRepository;
        personTest = new PersonTest();
    }

    @Test
    void shouldFindPageOfHouses() {
        //given
        List<House> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(House.class, 1));
        expectedList.add(testEntityManager.find(House.class, 2));
        expectedList.add(testEntityManager.find(House.class, 3));

        //when
        Page<House> actual = houseRepository.findPageHouses(3, 1);

        //then
        assertThat(actual.getTotalPages()).isEqualTo(2);
        assertThat(actual.getSize()).isEqualTo(3);
        assertThat(actual.getContent()).isEqualTo(expectedList);
    }

    @Test
    void shouldFindByUuid() {
        //given
        House expected = testEntityManager.find(House.class, TestConstant.ID);

        //when
        House actual = houseRepository.findByUuid(TestConstant.HOUSE_UUID).orElse(null);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldFindAllByOwners() {
        //given
        List<House> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(House.class, 1));
        expectedList.add(testEntityManager.find(House.class, 2));
        Person person = personTest.build();

        //when
        List<House> actual = houseRepository.findAllByOwnersIn(Set.of(person));

        //then
        assertThat(actual).isEqualTo(expectedList);
    }

    @Test
    void shouldFindPageHousesById() {
        //given
        List<Integer> listId = List.of(1, 2, 3);
        PageRequest pageRequest = PageRequest.of(0, 3);
        List<House> expectedList = new ArrayList<>();
        expectedList.add(testEntityManager.find(House.class, 1));
        expectedList.add(testEntityManager.find(House.class, 2));
        expectedList.add(testEntityManager.find(House.class, 3));

        //when
        Page<House> actual = houseRepository.findDistinctByIdIs(listId,pageRequest);

        //then
        assertThat(actual.getContent()).isEqualTo(expectedList);
    }

    @Test
    void shouldDeleteHouseByUuid() {
        //given
        houseRepository.deleteHouseByUuid(TestConstant.HOUSE_UUID);

        //when
        House actual = testEntityManager.find(House.class, TestConstant.ID);

        //then
        assertThat(actual).isNull();
    }
}
