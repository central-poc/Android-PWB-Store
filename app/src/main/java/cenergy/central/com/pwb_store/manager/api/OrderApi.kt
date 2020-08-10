package cenergy.central.com.pwb_store.manager.api

import android.content.Context
import cenergy.central.com.pwb_store.manager.HttpManagerMagento
import cenergy.central.com.pwb_store.model.APIError
import cenergy.central.com.pwb_store.model.body.PaymentInfoBody
import cenergy.central.com.pwb_store.utils.APIErrorUtils
import cenergy.central.com.pwb_store.utils.getResultError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderApi {

    companion object {
        private const val RESPONSE_REDIRECT_CODE = 302
        private const val HEADER_LOCATION = "Location"
    }

    fun setPaymentInformation(context: Context, cartId: String, paymentMethodBody: PaymentInfoBody,
                              callback: CreateOderCallback) {
        val apiManager = HttpManagerMagento.getInstance(context)

        apiManager.cartService.setPaymentInformation(HttpManagerMagento.CLIENT_NAME_E_ORDERING, apiManager.getUserClientType(),
                apiManager.getLanguage(), cartId, paymentMethodBody).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body())
                } else if (response.code() == RESPONSE_REDIRECT_CODE && response.errorBody() != null) { // 302 redirect
                    val errorBody = response.errorBody()?.string().toString().replace("\"", "")
                    val url = response.headers()[HEADER_LOCATION] ?: ""
                    callback.onSuccessAndRedirect(errorBody, url)
                } else {
                    callback.onFailure(APIErrorUtils.parseError(response))
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.onFailure(t.getResultError())
            }
        })
    }

    interface CreateOderCallback {
        fun onSuccess(oderId: String?)
        fun onSuccessAndRedirect(orderId: String?, url: String)
        fun onFailure(error: APIError)
    }
}