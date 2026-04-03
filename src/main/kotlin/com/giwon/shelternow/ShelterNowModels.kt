package com.giwon.shelternow

data class ApiResponse<T>(
    val success: Boolean,
    val data: T,
    val message: String? = null,
) {
    companion object {
        fun <T> ok(data: T): ApiResponse<T> = ApiResponse(success = true, data = data)
    }
}

data class ShelterSummary(
    val shelterId: String,
    val shelterName: String,
    val shelterType: String,
    val district: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val openStatus: String,
    val capacity: Int,
    val distanceMeters: Int? = null,
)

data class ShelterDetail(
    val shelterId: String,
    val shelterName: String,
    val shelterType: String,
    val district: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val openStatus: String,
    val capacity: Int,
    val phone: String?,
    val openingHours: String,
    val note: String,
)

data class ShelterMapGroup(
    val shelterType: String,
    val color: String,
    val shelters: List<ShelterSummary>,
)
