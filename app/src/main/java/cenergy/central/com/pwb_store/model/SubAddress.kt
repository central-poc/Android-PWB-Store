package cenergy.central.com.pwb_store.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

/**
 * Created by Anuphap Suwannamas on 26/8/2018 AD.
 * Email: Anupharpae@gmail.com
 */

open class SubAddress(
        @SerializedName("tel_mobile")
        var mobile: String = "",
        @SerializedName("house_no")
        var houseNumber: String = "",
        var building: String = "",
        var soi: String = "",
        @SerializedName("t1c_no")
        var t1cNo: String = "",
        var district: String = "",
        var subDistrict: String = "",
        var postcode: String = "",
        @SerializedName("district_id")
        var districtId: String = "",
        @SerializedName("subdistrict_id")
        var subDistrictId: String = "",
        @SerializedName("postcode_id")
        var postcodeId: String = "") : RealmObject()