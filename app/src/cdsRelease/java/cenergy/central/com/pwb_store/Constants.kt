package cenergy.central.com.pwb_store

import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object {
        // region endpoint
        const val BASE_URL_MAGENTO = "https://backend.central.co.th"
        const val CLIENT_MAGENTO = "Bearer 7xkqrabvnqthewp4ym41dgnamnromei0"
        const val PWB_HOST_NAME = "backend.central.co.th"
        // ens region

        const val WEB_HOST_NAME = "https://www.central.co.th"

        // region central
        const val CENTRAL_HOST_NAME = "https://uat-api.central.tech"
        const val CLIENT_SERVICE_NAME = "execute-api"
        const val CLIENT_REGION = "ap-southeast-1"
        const val CLIENT_X_API_KEY = "lIrZy8ZTEvkmu4uDe0m06wqNo91REUN7aWnk6GYi"
        const val CLIENT_ACCESS_KEY = "AKIAIIIW7RXNLIKRTSNQ"
        const val CLIENT_SECRET_KEY = "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"
        // end region

        // region formatter
        val DATE_FORMATTER_VALUE = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        // endregion
    }
}