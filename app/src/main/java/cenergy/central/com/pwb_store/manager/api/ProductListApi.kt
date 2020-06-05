package cenergy.central.com.pwb_store.manager.api

import android.app.Activity
import android.content.Context
import android.util.Log
import cenergy.central.com.pwb_store.BuildConfig
import cenergy.central.com.pwb_store.Constants
import cenergy.central.com.pwb_store.manager.ApiResponseCallback
import cenergy.central.com.pwb_store.manager.HttpManagerMagento
import cenergy.central.com.pwb_store.model.APIError
import cenergy.central.com.pwb_store.model.StoreActive
import cenergy.central.com.pwb_store.model.StoreStock
import cenergy.central.com.pwb_store.model.body.FilterGroups
import cenergy.central.com.pwb_store.model.body.ProductListBody
import cenergy.central.com.pwb_store.model.body.SortOrder
import cenergy.central.com.pwb_store.model.response.ProductResponse
import cenergy.central.com.pwb_store.realm.RealmController
import cenergy.central.com.pwb_store.utils.APIErrorUtils
import cenergy.central.com.pwb_store.utils.ParsingUtils
import cenergy.central.com.pwb_store.utils.getResultError
import com.google.gson.Gson
import io.realm.RealmList
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.util.*

class ProductListApi {
    private val database = RealmController.getInstance()

    fun retrieveProducts(context: Context, pageSize: Int, currentPage: Int, filterGroups: ArrayList<FilterGroups>,
                         sortOrders: ArrayList<SortOrder>, callback: ApiResponseCallback<ProductResponse>) {
        val apiManager = HttpManagerMagento.getInstance(context)
        val body = ProductListBody.createBody(pageSize, currentPage, filterGroups, sortOrders)
        val httpUrl = HttpUrl.Builder()
                .scheme("https")
                .host(Constants.PWB_HOST_NAME)
                .addPathSegments("rest/catalog-service/${apiManager.getLanguage()}/V1/products/search")
                .build()

        val json = Gson().toJson(body)
        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)

        val request = Request.Builder()
                .url(httpUrl)
                .post(requestBody)
                .build()

        apiManager.defaultHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                if (response != null) {
                    try {
                        val productResponse = ParsingUtils.parseToProductResponse(response)
                        if (BuildConfig.FLAVOR != "pwbOmniTv") {
                            callback.success(productResponse)
                        } else {
                            (context as Activity).runOnUiThread {
                                checkAvailableStore(context, productResponse, callback)
                            }
                        }
                    } catch (e: Exception) {
                        callback.failure(e.getResultError())
                        Log.e("JSON Parser", "Error parsing data $e")
                    }
                } else {
                    callback.failure(APIErrorUtils.parseError(response))
                }
                response?.close()
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback.failure(e.getResultError())
            }
        })
    }

    fun checkAvailableStore(context: Context, productResponse: ProductResponse, callback: ApiResponseCallback<ProductResponse>) {
        // NOTE: we did count because checking is equal of products response
        var count = 0
        val apiManager = HttpManagerMagento.getInstance(context)

        productResponse.products.forEach { product ->
            apiManager.getAvailableStore(product.sku, false,
                    object : ApiResponseCallback<Pair<List<StoreStock>, Boolean>> {
                        override fun success(response: Pair<List<StoreStock>, Boolean>?) {
                            (context as Activity).runOnUiThread {
                                // isn't from cache?
                                if (response != null && !response.second) {
                                    // save in local db
                                    val realmList = RealmList<StoreStock>()
                                    response.first.mapTo(realmList, { it })
                                    database.saveStoreStock(StoreActive(product.sku, realmList))
                                    // cached endpoint
                                    database.updateCachedEndpoint("${apiManager.getLanguage()}/rest/V1/storepickup/stores/active/${product.sku}")
                                }

                                count += 1
                                product.availableThisStore = handleAvailableHere(response?.first)
                                if (count == productResponse.products.size) {
                                    count = 0
                                    callback.success(productResponse)
                                }
                            }
                        }

                        override fun failure(error: APIError) {
                            (context as Activity).runOnUiThread {
                                count += 1
                                if (count == productResponse.products.size) {
                                    count = 0 // clear
                                    callback.success(productResponse)
                                }
                            }
                        }
                    })
        }
    }

    fun handleAvailableHere(listStoreAvailable: List<StoreStock>?): Boolean {
        val userInformation = database.userInformation
        val retailerId = userInformation?.store?.retailerId
        var stockCurrentStore = false
        if (retailerId != null && listStoreAvailable != null) {
            val store = listStoreAvailable.firstOrNull { it.sellerCode == retailerId }
            if (store != null) {
                stockCurrentStore = store.qty > 0
            }
        }
        return stockCurrentStore
    }
}