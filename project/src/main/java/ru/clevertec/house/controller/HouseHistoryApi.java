package ru.clevertec.house.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.clevertec.house.entity.dto.response.ResponseHouse;
import ru.clevertec.house.entity.dto.response.ResponsePerson;
import ru.clevertec.house.util.PaginationResponse;

import java.util.UUID;

@RequestMapping(path = "/history")
@Tag(name = "HouseHistory Api", description = "HouseHistory interface")
public interface HouseHistoryApi {

        @Operation(
                summary = "Get all the houses where a person was the owner",
                tags = {"HouseHistory Operations"},
                description = "Get list id houses. Returns a list id houses by uuid owner.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "List of id houses successfully returned")
                })
        @GetMapping("/houses/owners/{id}")
        ResponseEntity<PaginationResponse<ResponseHouse>> getOwnersHouse(@PathVariable("id") UUID personUuid,
                                                                                Integer pageSize, Integer numberPage);

        @Operation(
                summary = "Get all the houses where a person was the tenant",
                tags = {"HouseHistory Operations"},
                description = "Get list id houses. Returns a list id houses by uuid tenant.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "List of id houses successfully returned")
                })
        @GetMapping("/houses/tenants/{id}")
        ResponseEntity<PaginationResponse<ResponseHouse>> getTenantsHouse(@PathVariable("id") UUID personUuid,
                                                                                 Integer pageSize, Integer numberPage);

        @Operation(
                summary = "Get all the owners of the house",
                tags = {"HouseHistory Operations"},
                description = "Get list owners. Returns a list owners by uuid house.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "List of owners successfully returned")
                })
        @GetMapping("/people/owners/{id}")
        ResponseEntity<PaginationResponse<ResponsePerson>> getOwners(@PathVariable("id") UUID houseUuid,
                                                                            Integer pageSize, Integer numberPage);

        @Operation(
                summary = "Get all the tenants of the house",
                tags = {"HouseHistory Operations"},
                description = "Get list tenants. Returns a list tenants by uuid house.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "List of tenants successfully returned")
                })
        @GetMapping("/people/tenants/{id}")
        ResponseEntity<PaginationResponse<ResponsePerson>> getTenants(@PathVariable("id") UUID houseUuid,
                                                                             Integer pageSize, Integer numberPage);
}
