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
import ru.clevertec.house.entity.dto.request.RequestPerson;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.util.PaginationResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RequestMapping(path = "/persons")
@Tag(name = "PersonApi", description = "Person interface")
public interface PersonApi {

    @Operation(
            summary = "Create a new person",
            tags = "Person Operations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person successfully created")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ResponseEntity<ResponsePerson> createPerson(@Valid @RequestBody RequestPerson person,
                                                       @RequestParam("houseUuid") UUID houseUuid);

    @Operation(
            summary = "Gets person by uuid",
            tags = "Person Operations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning person")
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<ResponsePerson> getPersonById(@PathVariable("id") UUID id);

    @Operation(
            summary = "Update person by uuid",
            tags = "Person Operations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning updated person")
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<ResponsePerson> updatePersonById(@PathVariable("id") UUID id,
                                                           @Valid @RequestBody RequestPerson person);

    @Operation(
            summary = "Delete person by uuid",
            tags = "Person Operations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deletes person by uuid")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePersonById(@PathVariable("id") UUID id);

    @Operation(
            summary = "Get all persons",
            tags = "Person Operations",
            description = "Get all persons. Returns a list of persons.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of persons successfully returned")
    })
    @GetMapping(value = "/all")
    ResponseEntity<PaginationResponse<ResponsePerson>> getAllPersons(
            @RequestParam(defaultValue = "15", name = "pageSize") int pageSize,
            @RequestParam(defaultValue = "1", name = "numberPage") int numberPage);

    @Operation(
            summary = "Returns persons by house UUID",
            tags = "Person Operations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns persons by house UUID")
    })
    @GetMapping("/houses/{uuid}")
    ResponseEntity<List<ResponsePerson>> getPersonByUUidHouse(@PathVariable("uuid") UUID uuid);

    @Operation(
            summary = "Updates any fields for a person",
            tags = "Person Operations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returning updated person")
    })
    @PatchMapping("/{id}")
     ResponseEntity<ResponsePerson> patchPerson(@PathVariable UUID id,
                                                @RequestBody Map<String, Object> updates);
}
