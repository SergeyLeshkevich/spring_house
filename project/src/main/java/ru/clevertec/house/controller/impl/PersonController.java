package ru.clevertec.house.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.house.controller.PersonApi;
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.service.PersonServiceForController;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PersonController implements PersonApi {

    private final PersonServiceForController personService;

    @Override
    public ResponseEntity<ResponsePerson> createPerson(RequestPerson person,UUID houseUuid) {
        ResponsePerson responsePerson = personService.create(person, houseUuid);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responsePerson);
    }

    @Override
    public ResponseEntity<ResponsePerson> getPersonById(UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.get(id));
    }

    @Override
    public ResponseEntity<ResponsePerson> updatePersonById(UUID id, RequestPerson person) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.update(id, person));
    }

    @Override
    public ResponseEntity<Void> deletePersonById(UUID id) {
        personService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<PaginationResponse<ResponsePerson>> getAllPersons(int pageSize, int numberPage) {
        return new ResponseEntity<>(personService.getAll(pageSize, numberPage), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ResponsePerson>> getPersonByUUidHouse(UUID uuid) {
        List<ResponsePerson> personList = personService.getPeopleByUuidHouse(uuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personList);
    }

    @Override
    public ResponseEntity<ResponsePerson> patchPerson(UUID id, Map<String, Object> updates) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.patch(id, updates));
    }
}

