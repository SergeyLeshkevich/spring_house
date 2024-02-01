package ru.clevertec.house.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.house.controller.HouseApi;
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.service.HouseServiceForController;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class HouseController implements HouseApi {

    private final HouseServiceForController houseService;

    @Override
    public ResponseEntity<ResponseHouse> createHouse(RequestHouse house) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(houseService.create(house));
    }

    @Override
    public ResponseEntity<ResponseHouse> getHouseById(UUID id) {
        ResponseHouse house = houseService.get(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(house);
    }

    @Override
    public ResponseEntity<ResponseHouse> updateHouseById(UUID id, RequestHouse requestHouse) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.update(id, requestHouse));
    }

    @Override
    public ResponseEntity<Void> deleteHouseById(UUID id) {
        houseService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Override
    public ResponseEntity<PaginationResponse<ResponseHouse>> getAllHouses(int pageSize, int numberPage) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.getAll(pageSize, numberPage));
    }

    @Override
    public ResponseEntity<ResponseHouse> addTenant(UUID houseUuid, UUID personUuid) {
        ResponseHouse responseHouse = houseService.addOwner(houseUuid, personUuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseHouse);
    }

    @Override
    public ResponseEntity<List<ResponseHouse>> getHouseByOwnerUuid(UUID uuid) {
        List<ResponseHouse> houses = houseService.getHouseByOwnerUuid(uuid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houses);
    }

    @Override
    public ResponseEntity<ResponseHouse> patchHouse(UUID id, Map<String, Object> updates) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(houseService.patch(id, updates));
    }
}