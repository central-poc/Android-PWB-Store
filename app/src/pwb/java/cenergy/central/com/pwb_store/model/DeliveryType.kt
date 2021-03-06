package cenergy.central.com.pwb_store.model


enum class DeliveryType(val methodCode: String) {
    EXPRESS("express") {
        override fun toString(): String = "ส่งด่วน"
    },
    STANDARD("standard") {
        override fun toString(): String = "ส่งธรรมดา (โดย KERRY)"
    },
    STORE_PICK_UP("sts") {
        override fun toString(): String = "รับที่สาขา"
    },
    HOME("hdl") {
        override fun toString(): String = "กำหนดวันจัดส่ง (โดยรถพาวเวอร์บาย)"
    },
    STORE_PICK_UP_ISPU("ispu") {
        override fun toString(): String = "1 Hours Pickup"
    };
    companion object {
        private val map = values().associateBy(DeliveryType::methodCode)
        fun fromString(value: String) = map[value]
    }
}
