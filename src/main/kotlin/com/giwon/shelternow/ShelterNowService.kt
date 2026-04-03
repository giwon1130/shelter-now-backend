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
        shelter("S-009", "부산시청 무더위쉼터", "무더위쉼터", "연제구", "부산 연제구 중앙대로 1001", 35.1796, 129.0756, "운영중", 110, "051-120", "09:00-21:00", "부산권 대표 쉼터"),
        shelter("S-010", "대구역 민방위대피소", "민방위대피소", "중구", "대구 중구 태평로 161", 35.8798, 128.6285, "상시개방", 260, "053-120", "24시간", "역세권 즉시 대피 가능"),
        shelter("S-011", "광주광역시 한파쉼터", "한파쉼터", "서구", "광주 서구 내방로 111", 35.1595, 126.8526, "운영중", 85, "062-120", "08:00-20:00", "취약계층 우선 운영"),
        shelter("S-012", "대전 임시주거시설", "임시주거시설", "서구", "대전 서구 둔산로 100", 36.3504, 127.3845, "운영준비", 170, "042-120", "재난 시 개방", "생활 물자 기본 제공"),
        shelter("S-013", "인천시청 민방위대피소", "민방위대피소", "남동구", "인천 남동구 정각로 29", 37.4563, 126.7052, "상시개방", 280, "032-120", "24시간", "광역 대피 수용 가능"),
        shelter("S-014", "제주시 한파쉼터", "한파쉼터", "이도2동", "제주 제주시 문연로 6", 33.4996, 126.5312, "운영중", 70, "064-120", "08:00-19:00", "관광객 임시 보호 가능"),
        shelter("S-015", "수원시 무더위쉼터", "무더위쉼터", "수원시", "경기 수원시 팔달구 효원로 241", 37.2636, 127.0286, "운영중", 130, "031-120", "09:00-21:00", "수원권 중심 쉼터"),
        shelter("S-016", "성남시 한파쉼터", "한파쉼터", "성남시", "경기 성남시 분당구 성남대로 550", 37.4200, 127.1260, "운영중", 95, "031-120", "08:00-20:00", "분당권 접근성 우수"),
        shelter("S-017", "고양시 민방위대피소", "민방위대피소", "고양시", "경기 고양시 덕양구 고양시청로 10", 37.6584, 126.8320, "상시개방", 320, "031-120", "24시간", "서북부 대피 거점"),
        shelter("S-018", "용인시 임시주거시설", "임시주거시설", "용인시", "경기 용인시 처인구 중부대로 1199", 37.2410, 127.1776, "운영준비", 180, "031-120", "재난 시 개방", "가족 단위 수용 가능"),
        shelter("S-019", "부천시 무더위쉼터", "무더위쉼터", "부천시", "경기 부천시 길주로 210", 37.5034, 126.7660, "운영중", 105, "032-120", "09:00-20:00", "생활권 밀집 지역"),
        shelter("S-020", "안양시 한파쉼터", "한파쉼터", "안양시", "경기 안양시 동안구 시민대로 235", 37.3943, 126.9568, "운영중", 88, "031-120", "08:00-20:00", "노약자 우선 보호"),
        shelter("S-021", "화성시 민방위대피소", "민방위대피소", "화성시", "경기 화성시 남양읍 시청로 159", 37.1996, 126.8318, "상시개방", 290, "031-120", "24시간", "남부권 광역 대피 지원"),
        shelter("S-022", "의정부시 임시주거시설", "임시주거시설", "의정부시", "경기 의정부시 시민로 1", 37.7381, 127.0337, "운영준비", 160, "031-120", "재난 시 개방", "북부권 임시 체류 지원"),
        shelter("S-023", "춘천시 무더위쉼터", "무더위쉼터", "춘천시", "강원 춘천시 시청길 11", 37.8813, 127.7298, "운영중", 90, "033-120", "09:00-20:00", "강원 북부 거점"),
        shelter("S-024", "원주시 한파쉼터", "한파쉼터", "원주시", "강원 원주시 시청로 1", 37.3422, 127.9202, "운영중", 82, "033-120", "08:00-20:00", "한파 특보 시 야간 연장"),
        shelter("S-025", "청주시 민방위대피소", "민방위대피소", "청주시", "충북 청주시 상당구 상당로 155", 36.6424, 127.4890, "상시개방", 260, "043-120", "24시간", "충북권 중심 대피소"),
        shelter("S-026", "충주시 임시주거시설", "임시주거시설", "충주시", "충북 충주시 으뜸로 21", 36.9910, 127.9259, "운영준비", 140, "043-120", "재난 시 개방", "임시 체류 시설"),
        shelter("S-027", "천안시 무더위쉼터", "무더위쉼터", "천안시", "충남 천안시 서북구 번영로 156", 36.8151, 127.1139, "운영중", 115, "041-120", "09:00-21:00", "충남 북부 생활권"),
        shelter("S-028", "아산시 한파쉼터", "한파쉼터", "아산시", "충남 아산시 시민로 456", 36.7898, 127.0019, "운영중", 77, "041-120", "08:00-20:00", "고령층 우선석 운영"),
        shelter("S-029", "전주시 민방위대피소", "민방위대피소", "전주시", "전북 전주시 완산구 노송광장로 10", 35.8242, 127.1480, "상시개방", 310, "063-120", "24시간", "전북권 광역 대응"),
        shelter("S-030", "군산시 임시주거시설", "임시주거시설", "군산시", "전북 군산시 시청로 17", 35.9677, 126.7367, "운영준비", 150, "063-120", "재난 시 개방", "가족 단위 수용 가능"),
        shelter("S-031", "순천시 무더위쉼터", "무더위쉼터", "순천시", "전남 순천시 장명로 30", 34.9507, 127.4872, "운영중", 96, "061-120", "09:00-20:00", "전남 동부권 쉼터"),
        shelter("S-032", "목포시 한파쉼터", "한파쉼터", "목포시", "전남 목포시 양을로 203", 34.8118, 126.3922, "운영중", 73, "061-120", "08:00-19:00", "해안권 한파 대응"),
        shelter("S-033", "포항시 민방위대피소", "민방위대피소", "포항시", "경북 포항시 남구 시청로 1", 36.0190, 129.3435, "상시개방", 340, "054-120", "24시간", "동해권 재난 대응"),
        shelter("S-034", "구미시 임시주거시설", "임시주거시설", "구미시", "경북 구미시 송정대로 55", 36.1195, 128.3446, "운영준비", 155, "054-120", "재난 시 개방", "산단 인근 수용 지원"),
        shelter("S-035", "창원시 무더위쉼터", "무더위쉼터", "창원시", "경남 창원시 의창구 중앙대로 151", 35.2276, 128.6811, "운영중", 122, "055-120", "09:00-21:00", "경남권 중심 쉼터"),
        shelter("S-036", "진주시 한파쉼터", "한파쉼터", "진주시", "경남 진주시 동진로 155", 35.1800, 128.1076, "운영중", 69, "055-120", "08:00-20:00", "서부경남 겨울 대응"),
        shelter("S-037", "울산시 민방위대피소", "민방위대피소", "남구", "울산 남구 중앙로 201", 35.5384, 129.3114, "상시개방", 280, "052-120", "24시간", "광역시 권역 대피소"),
        shelter("S-038", "세종시 임시주거시설", "임시주거시설", "세종시", "세종 한누리대로 2130", 36.4800, 127.2890, "운영준비", 165, "044-120", "재난 시 개방", "행정중심권 임시 체류 지원"),
    )

    fun searchShelters(
        query: String?,
        shelterType: String?,
        district: String?,
        openStatus: String?,
        sortBy: String,
        latitude: Double?,
        longitude: Double?,
    ): List<ShelterSummary> {
        val normalizedQuery = query?.trim().orEmpty()
        val normalizedType = shelterType?.trim().orEmpty()
        val normalizedDistrict = district?.trim().orEmpty()
        val normalizedStatus = openStatus?.trim().orEmpty()
        val normalizedSort = sortBy.trim().lowercase()

        val results = shelters
            .filter {
                val matchesQuery = normalizedQuery.isBlank() ||
                    it.shelterName.contains(normalizedQuery, ignoreCase = true) ||
                    it.address.contains(normalizedQuery, ignoreCase = true)
                val matchesType = normalizedType.isBlank() || it.shelterType == normalizedType
                val matchesDistrict = normalizedDistrict.isBlank() || it.district == normalizedDistrict
                val matchesStatus = normalizedStatus.isBlank() || it.openStatus == normalizedStatus
                matchesQuery && matchesType && matchesDistrict && matchesStatus
            }
            .map {
                val distance = if (latitude != null && longitude != null) {
                    calculateDistanceMeters(latitude, longitude, it.latitude, it.longitude)
                } else {
                    null
                }
                it.toSummary(distance)
            }

        return when (normalizedSort) {
            "capacity" -> results.sortedWith(
                compareByDescending<ShelterSummary> { it.capacity }
                    .thenBy { it.distanceMeters ?: Int.MAX_VALUE }
                    .thenBy { it.shelterName },
            )

            "name" -> results.sortedBy { it.shelterName }
            else -> results.sortedWith(compareBy<ShelterSummary> { it.distanceMeters ?: Int.MAX_VALUE }.thenBy { it.shelterName })
        }
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
