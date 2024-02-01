package ru.clevertec.house.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.clevertec.house.entity.dto.request.RequestHouse;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RequestMapping(path = "/houses")
@Tag(name = "HouseApi", description = "House interface")
public interface HouseApi {

    @Operation(
            summary = "Create new house",
            tags = {"House Operations"},
            description = "House creation. Returns the location of a new resource.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "House successfully created")
            })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ResponseHouse> createHouse(@Valid @RequestBody RequestHouse house);

    @Operation(
            summary = "Get house by uuid",
            tags = {"House Operations"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Returning house")
            })
    @GetMapping("/{id}")
    ResponseEntity<ResponseHouse> getHouseById(@PathVariable("id") UUID id);

    @Operation(
            summary = "Updates all home fields",
            tags = {"House Operations"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Returning updated house")
            })
    @PutMapping("/{id}")
    ResponseEntity<ResponseHouse> updateHouseById(@PathVariable("id") UUID id,
                                                  @Valid @RequestBody RequestHouse requestHouse);

    @Operation(
            summary = "Deletes house by uuid",
            tags = {"House Operations"},
            description = "Deletes house by uuid")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Deletes house by uuid")
            })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteHouseById(@PathVariable("id") UUID id);

    @Operation(
            summary = "Gets all houses",
            tags = {"House Operations"},
            description = "Get houses. Returns a list houses.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of houses successfully returned")
            })
    @GetMapping
    ResponseEntity<PaginationResponse<ResponseHouse>> getAllHouses(
            @RequestParam(defaultValue = "15", name = "pageSize") int pageSize,
            @RequestParam(defaultValue = "1", name = "numberPage") int numberPage);

    @Operation(
            summary = "Adds a tenant to the house",
            tags = {"House Operations"},
            description = "Adds a tenant to the house.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Returning updated house")
            })
    @PutMapping("/tenants")
    ResponseEntity<ResponseHouse> addTenant(@RequestParam("house_uuid") UUID houseUuid,
                                            @RequestParam("person_uuid") UUID personUuid);

    @Operation(
            summary = "Returns the house by owner uuid",
            tags = {"House Operations"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Returns the house by owner uuid")
            })
    @GetMapping("/owners/{uuid}")
    ResponseEntity<List<ResponseHouse>> getHouseByOwnerUuid(@PathVariable("uuid") UUID uuid);

    @Operation(
            summary = "Updates any fields at home",
            tags = {"House Operations"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Returning updated house")
            })
    @PatchMapping("/{id}")
    ResponseEntity<ResponseHouse> patchHouse(@PathVariable UUID id, @RequestBody Map<String, Object> updates);
}
