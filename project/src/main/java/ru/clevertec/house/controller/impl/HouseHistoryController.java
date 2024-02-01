package ru.clevertec.house.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.house.controller.HouseHistoryApi;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.enums.Type;
import ru.clevertec.house.service.HouseHistoryService;
import ru.clevertec.house.util.PaginationResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class HouseHistoryController implements HouseHistoryApi {

    private final HouseHistoryService historyService;

    @Override
    public ResponseEntity<PaginationResponse<ResponseHouse>> getOwnersHouse(UUID personUuid,Integer pageSize,
                                                                            Integer numberPage) {
        PaginationResponse<ResponseHouse> houses = historyService.getAllIdHouses(personUuid, Type.OWNER, pageSize, numberPage);
        return new ResponseEntity<>(houses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginationResponse<ResponseHouse>> getTenantsHouse(UUID personUuid,Integer pageSize,
                                                                             Integer numberPage) {
        PaginationResponse<ResponseHouse> houses = historyService.getAllIdHouses(personUuid, Type.TENANT, pageSize, numberPage);
        return new ResponseEntity<>(houses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginationResponse<ResponsePerson>> getOwners(UUID houseUuid,Integer pageSize,
                                                                        Integer numberPage) {
        PaginationResponse<ResponsePerson> people = historyService.getAllIdPeople(houseUuid, Type.OWNER, pageSize, numberPage);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PaginationResponse<ResponsePerson>> getTenants(UUID houseUuid,Integer pageSize,
                                                                         Integer numberPage) {
        PaginationResponse<ResponsePerson> people = historyService.getAllIdPeople(houseUuid, Type.TENANT, pageSize, numberPage);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }
}
