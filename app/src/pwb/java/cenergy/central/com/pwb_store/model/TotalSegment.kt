package cenergy.central.com.pwb_store.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONArray


class TotalSegment(
        var code: String? = "",
        var title: String? = "",
        var value: String? = ""
) {
    companion object {
        const val COUPON_KEY = "coupon"
        const val DISCOUNT_KEY = "discount"

        fun getCouponDiscount(s: String?): CouponDiscount? {
            var couponDiscount: CouponDiscount? = null
            try {
                if (s != null) {
                    val arrayJson = JSONArray(s)
                    if (!arrayJson.isNull(0)) {
                        couponDiscount = Gson().fromJson(arrayJson.getString(0),
                                CouponDiscount::class.java)
                    }
                }
            } catch (e: Exception) {
                return couponDiscount
            }

            return couponDiscount
        }
    }
}

class CouponDiscount(
        @SerializedName("coupon_code")
        val couponCode: String,
        @SerializedName("discount_amount")
        val couponAmount: String,
        @SerializedName("discount_amount_formatted")
        val couponAmountFormat: String
)