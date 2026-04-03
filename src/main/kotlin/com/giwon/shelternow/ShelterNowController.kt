package com.giwon.shelternow

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ShelterNowController(
    private val shelterNowService: ShelterNowService,
) {
    @GetMapping("/shelters")
    fun searchShelters(
        @RequestParam(required = false) query: String?,
        @RequestParam(required = false) shelterType: String?,
        @RequestParam(required = false) district: String?,
        @RequestParam(required = false) openStatus: String?,
        @RequestParam(required = false, defaultValue = "distance") sortBy: String,
        @RequestParam(required = false) latitude: Double?,
        @RequestParam(required = false) longitude: Double?,
    ): ApiResponse<List<ShelterSummary>> = ApiResponse.ok(
        shelterNowService.searchShelters(query, shelterType, district, openStatus, sortBy, latitude, longitude),
    )

    @GetMapping("/shelters/map")
    fun getShelterMap(): ApiResponse<List<ShelterMapGroup>> = ApiResponse.ok(
        shelterNowService.getShelterMap(),
    )

    @GetMapping("/shelters/{shelterId}")
    fun getShelterDetail(
        @PathVariable shelterId: String,
    ): ApiResponse<ShelterDetail> = ApiResponse.ok(
        shelterNowService.getShelterDetail(shelterId),
    )
}
