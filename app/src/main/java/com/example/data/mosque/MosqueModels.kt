package com.example.data.mosque

import com.example.data.model.AppLanguage

enum class MosqueCategory(val id: String) {
    MOSQUE("mosque"),
    JAMI_MOSQUE("jami_mosque"),
    PRAYER_ROOM("prayer_room"),
    EID_GROUND("eid_ground");

    fun label(language: AppLanguage): String = when (this) {
        MOSQUE -> when (language) {
            AppLanguage.URDU -> "مسجد"
            AppLanguage.ARABIC -> "مسجد"
            AppLanguage.HINDI -> "मस्जिद"
            AppLanguage.ENGLISH -> "Mosque"
        }
        JAMI_MOSQUE -> when (language) {
            AppLanguage.URDU -> "جامع مسجد"
            AppLanguage.ARABIC -> "مسجد جامع"
            AppLanguage.HINDI -> "जामा मस्जिद"
            AppLanguage.ENGLISH -> "Jami Mosque"
        }
        PRAYER_ROOM -> when (language) {
            AppLanguage.URDU -> "نماز گاہ / مصلیٰ"
            AppLanguage.ARABIC -> "مصلى"
            AppLanguage.HINDI -> "नमाज़ कक्ष / मुसल्ला"
            AppLanguage.ENGLISH -> "Prayer Room / Musalla"
        }
        EID_GROUND -> when (language) {
            AppLanguage.URDU -> "عیدگاہ"
            AppLanguage.ARABIC -> "مصلى العيد"
            AppLanguage.HINDI -> "ईदगाह"
            AppLanguage.ENGLISH -> "Eid Prayer Ground"
        }
    }
}

enum class OsmVerificationLevel {
    VERIFIED_MUSLIM_PLACE_OF_WORSHIP,
    PRAYER_ROOM_MUSALLA,
    BUILDING_MOSQUE_CANDIDATE;

    fun label(language: AppLanguage): String = when (this) {
        VERIFIED_MUSLIM_PLACE_OF_WORSHIP -> when (language) {
            AppLanguage.URDU -> "تصدیق شدہ مسجد ٹیگ (OSM)"
            AppLanguage.ARABIC -> "مسجد موثق الوسوم (OSM)"
            AppLanguage.HINDI -> "सत्यापित मस्जिद टैग (OSM)"
            AppLanguage.ENGLISH -> "Tagged Muslim Place of Worship"
        }
        PRAYER_ROOM_MUSALLA -> when (language) {
            AppLanguage.URDU -> "نماز گاہ / مصلیٰ (OSM)"
            AppLanguage.ARABIC -> "مصلى / قاعة صلاة (OSM)"
            AppLanguage.HINDI -> "नमाज़ कक्ष / मुसल्ला (OSM)"
            AppLanguage.ENGLISH -> "Tagged Prayer Room / Musalla"
        }
        BUILDING_MOSQUE_CANDIDATE -> when (language) {
            AppLanguage.URDU -> "مسجد طرزِ عمارت (فعال حیثیت غیر مصدقہ)"
            AppLanguage.ARABIC -> "مبنى مسجد (الحالة النشطة غير مؤكدة)"
            AppLanguage.HINDI -> "मस्जिद इमारत (सक्रिय स्थिति अपुष्ट)"
            AppLanguage.ENGLISH -> "Mosque Building (Active status unverified)"
        }
    }
}

enum class LocationPrecisionMode {
    GPS_PRECISE,
    APPROXIMATE,
    MANUAL_PRESET,
    PERMISSION_DENIED
}

data class MapBoundingBox(
    val south: Double,
    val west: Double,
    val north: Double,
    val east: Double
) {
    val centerLat: Double get() = (south + north) / 2.0
    val centerLon: Double get() = (west + east) / 2.0

    fun isValid(): Boolean =
        south in -90.0..90.0 &&
            north in -90.0..90.0 &&
            west in -180.0..180.0 &&
            east in -180.0..180.0 &&
            north > south &&
            east > west
}

data class MosqueItem(
    val id: String,
    val osmType: String,
    val osmId: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Double,
    val category: MosqueCategory,
    val address: String? = null,
    val phone: String? = null,
    val website: String? = null,
    val openingHours: String? = null,
    val wheelchair: String? = null,
    val lastUpdatedMs: Long = System.currentTimeMillis(),
    val verificationLevel: OsmVerificationLevel = OsmVerificationLevel.VERIFIED_MUSLIM_PLACE_OF_WORSHIP,
    val denomination: String? = null,
    val osmTagsSummary: String = ""
) {
    val osmUrl: String
        get() = "https://www.openstreetmap.org/$osmType/$osmId"
}

data class GeocodedArea(
    val displayName: String,
    val shortName: String,
    val latitude: Double,
    val longitude: Double,
    val isOfflinePreset: Boolean = false
)

data class MosqueSearchState(
    val mosques: List<MosqueItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFromCache: Boolean = false,
    val isFreshLiveResult: Boolean = false,
    val cacheTimestampMs: Long? = null,
    val isCacheStale: Boolean = false,
    val searchCenterLat: Double = 0.0,
    val searchCenterLon: Double = 0.0,
    val searchLabel: String = "",
    val radiusMeters: Int = 5000,
    val precisionMode: LocationPrecisionMode = LocationPrecisionMode.PERMISSION_DENIED,
    val hasSearched: Boolean = false,
    val activeEndpoint: String = OverpassMosqueService.DEFAULT_ENDPOINTS.first(),
    val preferredEndpoint: String? = null,
    val isRateLimited: Boolean = false,
    val lastQueryMode: String = ""
)

enum class MapStylePreset(
    val id: String,
    val styleUrl: String,
    val hasBuiltIn3dLayer: Boolean,
    val maxCameraZoom: Double
) {
    HYBRID_3D("hybrid_3d", "asset://mosque_hybrid_3d_style.json", true, 21.0),
    SATELLITE_HD("satellite_hd", "asset://mosque_satellite_hd_style.json", false, 17.5),
    LIBERTY("liberty", "https://tiles.openfreemap.org/styles/liberty", true, 21.0),
    BRIGHT("bright", "https://tiles.openfreemap.org/styles/bright", false, 21.0)
}

enum class OSRMRouteProfile(
    val id: String,
    val osrmProfile: String
) {
    DRIVING("driving", "driving"),
    WALKING("walking", "foot"),
    CYCLING("cycling", "bike")
}

data class RouteStepInfo(
    val instruction: String,
    val roadName: String,
    val distanceMeters: Double,
    val durationSeconds: Double?
)

data class MosqueRouteResult(
    val profile: OSRMRouteProfile,
    val originLat: Double,
    val originLon: Double,
    val destinationLat: Double,
    val destinationLon: Double,
    val coordinates: List<Pair<Double, Double>>,
    val distanceMeters: Double,
    val durationSeconds: Double?,
    val roadSummary: String?,
    val steps: List<RouteStepInfo>,
    val providerHost: String,
    val fetchedAtMs: Long
)

data class MosqueRouteState(
    val targetMosqueId: String? = null,
    val targetMosqueName: String? = null,
    val profile: OSRMRouteProfile = OSRMRouteProfile.DRIVING,
    val isLoading: Boolean = false,
    val route: MosqueRouteResult? = null,
    val errorMessage: String? = null
)

