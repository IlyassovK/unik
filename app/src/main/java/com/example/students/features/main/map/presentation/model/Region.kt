package com.example.students.features.map

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point
import kotlinx.parcelize.Parcelize

object Regions {

    // TODO: 13.10.2020 remove this enum after RegionItem.class will be extend with geo coordinates
    enum class Region(id: Int) {
        ALL_KZ(0),
        ALMATY(1),
        ASTANA(2);

        fun nameRegion(): String {
            return when (this) {
                ALL_KZ -> "Казахстан"
                ALMATY -> "Алматы"
                ASTANA -> "Нур-Султан"
            }
        }

        fun geoRegion(): Point {
            return when (this) {
                ALL_KZ -> Point(48.0196, 66.9237)
                ALMATY -> Point(43.25654, 76.92848)
                ASTANA -> Point(51.169392, 71.449074)
            }
        }

        companion object {
            fun get(code: Int?): Region? {
                for (c in values()) {
                    if (c.ordinal == code)
                        return c
                }
                return null
            }
        }
    }

    /**
     * only one place to keep default Point (All Kazakhstan)
     */
    val KZ_REGION_POINT: Point = Point(48.0196, 66.9237)

    /**
     * only one place to keep default RegionItem (all KZ)
     * item id was received from api .../ref/regions
     */
    // TODO: 10.10.2020 add all params (lat., lon.) after api will be modified
    val KZ_REGION: RegionItem = RegionItem(
        1000,
        "All Kazakhstan",
        latitude = KZ_REGION_POINT.latitude,
        longitude = KZ_REGION_POINT.longitude
    )
}

@Parcelize
data class RegionItem(
    @SerializedName("id")
    val id: Int? = -1,
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("priority")
    val priority: Int? = 0,
    @SerializedName("timeOffset")
    val timeOffset: Long? = 0,
    @SerializedName("latitude")
    val latitude: Double? = 0.0,
    @SerializedName("longitude")
    val longitude: Double? = 0.0,
) : Parcelable {
    companion object {
        const val ALL_KAZAKHSTAN = 1000
    }
}
