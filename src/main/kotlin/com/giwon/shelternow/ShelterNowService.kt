package com.giwon.shelternow

import org.springframework.stereotype.Service
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@Service
class ShelterNowService {
    private val typeColors = mapOf(
        "무더위쉼터" to "#ef4444",
        "한파쉼터" to "#3b82f6",
        "민방위대피소" to "#0f172a",
        "임시주거시설" to "#16a34a",
    )

    private val shelters = listOf(
        shelter("S-001", "강남구청 무더위쉼터", "무더위쉼터", "강남구", "서울 강남구 학동로 426", 37.5172, 127.0473, "운영중", 80, "02-120", "09:00-18:00", "폭염 특보 시 연장 운영"),
        shelter("S-002", "삼성1동 한파쉼터", "한파쉼터", "강남구", "서울 강남구 삼성로 628", 37.5147, 127.0639, "운영중", 60, "02-120", "08:00-20:00", "한파 주의보 시 즉시 개방"),
        shelter("S-003", "서초구 민방위대피소", "민방위대피소", "서초구", "서울 서초구 서초대로 274", 37.4917, 127.0076, "상시개방", 240, "02-120", "24시간", "지하 대피 공간"),
        shelter("S-004", "영등포 임시주거시설", "임시주거시설", "영등포구", "서울 영등포구 당산로 123", 37.5289, 126.8961, "운영중", 120, "02-120", "24시간", "재난 발생 시 우선 배정"),
        shelter("S-005", "마포구청 무더위쉼터", "무더위쉼터", "마포구", "서울 마포구 월드컵로 212", 37.5663, 126.9014, "운영중", 90, "02-120", "09:00-21:00", "야간 냉방 가능"),
        shelter("S-006", "종로3가 민방위대피소", "민방위대피소", "종로구", "서울 종로구 종로 123", 37.5704, 126.9921, "상시개방", 300, "02-120", "24시간", "환승역 접근성 우수"),
        shelter("S-007", "서울역 한파쉼터", "한파쉼터", "중구", "서울 중구 한강대로 405", 37.5559, 126.9723, "운영중", 75, "02-120", "07:00-22:00", "교통 약자 우선석 운영"),
        shelter("S-008", "잠실 임시주거시설", "임시주거시설", "송파구", "서울 송파구 올림픽로 240", 37.5133, 127.1028, "운영준비", 140, "02-120", "재난 시 개방", "현장 인력 배치 필요"),
    )

    fun searchShelters(
        query: String?,
        shelterType: String?,
        district: String?,
        latitude: Double?,
        longitude: Double?,
    ): List<ShelterSummary> {
        val normalizedQuery = query?.trim().orEmpty()
        val normalizedType = shelterType?.trim().orEmpty()
        val normalizedDistrict = district?.trim().orEmpty()

        return shelters
            .filter {
                val matchesQuery = normalizedQuery.isBlank() ||
                    it.shelterName.contains(normalizedQuery, ignoreCase = true) ||
                    it.address.contains(normalizedQuery, ignoreCase = true)
                val matchesType = normalizedType.isBlank() || it.shelterType == normalizedType
                val matchesDistrict = normalizedDistrict.isBlank() || it.district == normalizedDistrict
                matchesQuery && matchesType && matchesDistrict
            }
            .map {
                val distance = if (latitude != null && longitude != null) {
                    calculateDistanceMeters(latitude, longitude, it.latitude, it.longitude)
                } else {
                    null
                }
                it.toSummary(distance)
            }
            .sortedWith(compareBy<ShelterSummary> { it.distanceMeters ?: Int.MAX_VALUE }.thenBy { it.shelterName })
    }

    fun getShelterMap(): List<ShelterMapGroup> {
        return shelters
            .groupBy { it.shelterType }
            .entries
            .sortedBy { it.key }
            .map { (type, values) ->
                ShelterMapGroup(
                    shelterType = type,
                    color = typeColors[type] ?: "#475569",
                    shelters = values.sortedBy { it.shelterName }.map { it.toSummary() },
                )
            }
    }

    fun getShelterDetail(shelterId: String): ShelterDetail {
        return shelters.firstOrNull { it.shelterId == shelterId }
            ?.toDetail()
            ?: ShelterDetail(
                shelterId = shelterId,
                shelterName = "미확인 쉼터",
                shelterType = "미분류",
                district = "미상",
                address = "주소 정보 없음",
                latitude = 37.5665,
                longitude = 126.9780,
                openStatus = "확인 필요",
                capacity = 0,
                phone = null,
                openingHours = "확인 필요",
                note = "추가 데이터 연동 전 기본 응답",
            )
    }

    private fun shelter(
        shelterId: String,
        shelterName: String,
        shelterType: String,
        district: String,
        address: String,
        latitude: Double,
        longitude: Double,
        openStatus: String,
        capacity: Int,
        phone: String?,
        openingHours: String,
        note: String,
    ): ShelterDetail = ShelterDetail(
        shelterId = shelterId,
        shelterName = shelterName,
        shelterType = shelterType,
        district = district,
        address = address,
        latitude = latitude,
        longitude = longitude,
        openStatus = openStatus,
        capacity = capacity,
        phone = phone,
        openingHours = openingHours,
        note = note,
    )

    private fun ShelterDetail.toSummary(distanceMeters: Int? = null): ShelterSummary =
        ShelterSummary(
            shelterId = shelterId,
            shelterName = shelterName,
            shelterType = shelterType,
            district = district,
            address = address,
            latitude = latitude,
            longitude = longitude,
            openStatus = openStatus,
            capacity = capacity,
            distanceMeters = distanceMeters,
        )

    private fun ShelterDetail.toDetail(): ShelterDetail = this
    private fun calculateDistanceMeters(
        fromLat: Double,
        fromLng: Double,
        toLat: Double,
        toLng: Double,
    ): Int {
        val earthRadius = 6371000.0
        val dLat = Math.toRadians(toLat - fromLat)
        val dLng = Math.toRadians(toLng - fromLng)
        val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(fromLat)) * cos(Math.toRadians(toLat)) *
            sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadius * c).roundToInt()
    }
}
