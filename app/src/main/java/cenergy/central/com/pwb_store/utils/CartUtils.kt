package cenergy.central.com.pwb_store.utils

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import cenergy.central.com.pwb_store.Constants
import cenergy.central.com.pwb_store.R
import cenergy.central.com.pwb_store.extensions.isTwoHourProduct
import cenergy.central.com.pwb_store.helpers.DialogHelper
import cenergy.central.com.pwb_store.manager.ApiResponseCallback
import cenergy.central.com.pwb_store.manager.HttpManagerMagento
import cenergy.central.com.pwb_store.manager.preferences.PreferenceManager
import cenergy.central.com.pwb_store.model.*
import cenergy.central.com.pwb_store.model.body.CartItemBody
import cenergy.central.com.pwb_store.model.body.OptionBody
import cenergy.central.com.pwb_store.model.response.BranchResponse
import cenergy.central.com.pwb_store.model.response.CartResponse
import cenergy.central.com.pwb_store.model.response.CartTotalResponse
import cenergy.central.com.pwb_store.model.response.ShippingInformationResponse
import cenergy.central.com.pwb_store.realm.DatabaseListener
import cenergy.central.com.pwb_store.realm.RealmController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartUtils(private val context: Context) {
    private val prefManager: PreferenceManager = PreferenceManager(context)
    private var callback: AddProductToCartCallback? = null
    private var branchResponse: BranchResponse? = null
    private var options: ArrayList<OptionBody> = arrayListOf()
    private var language = prefManager.getDefaultLanguage()

    // data
    private val db = RealmController.getInstance()
    private var cacheCartItems = db.cacheCartItems

    fun addProductToCart(product: Product,
                         options: ArrayList<OptionBody> = arrayListOf(),
                         callback: AddProductToCartCallback) {
        this.callback = callback
        val cartId = prefManager.cartId
        this.options = options
        this.branchResponse = null // force no have branch

        if (cartId != null) {
            requestAddToCart(cartId, product)
        } else {
            retrieveCart(product)
        }
    }

    fun addProduct2hToCart(product: Product, branchResponse: BranchResponse,
                           callback: AddProductToCartCallback) {
        this.branchResponse = branchResponse
        this.callback = callback

        val cartId = prefManager.cartId

        if (cartId != null) {
            requestAddToCart(cartId, product)
        } else {
            retrieveCart(product)
        }
    }

    private fun retrieveCart(product: Product) {
        HttpManagerMagento.getInstance(context).getCart(object : ApiResponseCallback<String?> {
            override fun success(response: String?) {
                if (response != null) {
                    prefManager.setCartId(response)

                    // next step add to card
                    requestAddToCart(response, product)
                }
            }

            override fun failure(error: APIError) {
                val dialog = DialogHelper(context).errorDialog(error)
                callback?.onFailure(dialog)
            }
        })
    }

    private fun requestAddToCart(cartId: String, product: Product) {
        // is empty cart?
        if (cacheCartItems == null || cacheCartItems.isEmpty()) {
            val cartItemBody = if (branchResponse != null) {
                CartItemBody.create(cartId, product, branchResponse!!)  // ispu
            } else {
                CartItemBody.create(cartId, product, options) // normal
            }
            requestAddToCart(cartId, product, cartItemBody)
        } else {
            // is product ispu
            when (product.isTwoHourProduct()) {
                true -> {
                    if (cacheCartItems.hasProduct2h()) {
                        val body = CartItemBody.create(cartId, product, branchResponse!!)  // ispu
                        requestAddToCart(cartId, product, body)
                    } else {
                        clearCartAndRecreateCart(product)
                    }
                }
                else -> {
                    // product normal
                    if (cacheCartItems.hasProduct2h()) {
                        clearCartAndRecreateCart(product)
                    } else {
                        val body = CartItemBody.create(cartId, product, options)  // normal
                        requestAddToCart(cartId, product, body)
                    }

                }
            }
        }
    }

    private fun requestAddToCart(cartId: String, product: Product, cartItemBody: CartItemBody) {
        HttpManagerMagento.getInstance(context).addProductToCart(cartId, cartItemBody,
                object : ApiResponseCallback<CartItem> {
                    override fun success(response: CartItem?) {
                        if (branchResponse != null) {
                            response?.let { setShippingAddress(cartId, it, product, branchResponse!!) }
                        } else {
                            // product normal
                            response?.let { saveCartItem(it, product, branchResponse) }
                        }
                    }

                    override fun failure(error: APIError) {
                        if (error.errorCode == APIError.INTERNAL_SERVER_ERROR.toString()) {
                            callback?.forceClearCart()
                        } else {
                            callback?.onFailure(error.errorMessage)
                        }
                    }
                })
    }

    private fun setShippingAddress(cartId: String, cartItem: CartItem, product: Product,
                                   branchResponse: BranchResponse) {
        val storeAddress = AddressInformation.createBranchAddress(branchResponse.branch)
        val storePickup = StorePickup(branchResponse.branch.storeId)
        val subscribeCheckOut = SubscribeCheckOut("", null,
                null, null, storePickup)

        HttpManagerMagento.getInstance(context).setSgippingInformation(cartId,
                storeAddress, subscribeCheckOut, DeliveryOption.getStorePickupIspu(),
                object : ApiResponseCallback<ShippingInformationResponse> {
                    override fun success(response: ShippingInformationResponse?) {
                        saveCartItem(cartItem, product, branchResponse)
                    }

                    override fun failure(error: APIError) {
                        callback?.onFailure(error.errorMessage)
                    }
                })
    }

    private fun saveCartItem(cartItem: CartItem, product: Product,
                             branchResponse: BranchResponse? = null) {
        // create cacheCartItem
        val cacheCartItem = if (branchResponse != null) {
            CacheCartItem.asCartItem(cartItem, product, branchResponse)
        } else {
            CacheCartItem.asCartItem(cartItem, product)
        }

        RealmController.getInstance().saveCartItem(cacheCartItem,
                object : DatabaseListener {
                    override fun onSuccessfully() {
                        Toast.makeText(context, context.getString(R.string.added_to_cart),
                                Toast.LENGTH_SHORT).show()
                        callback?.onSuccessfully()
                    }

                    override fun onFailure(error: Throwable) {
                        error.message?.let { callback?.onFailure(it) }
                    }
                })
    }

    fun viewCart(cartId: String, callback: ApiResponseCallback<CartResponse>) {
        HttpManagerMagento(context).cartService.viewCart(language, cartId).enqueue(object : Callback<CartResponse> {
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                if (response.body() != null && response.isSuccessful) {
                    callback.success(response.body())
                } else {
                    callback.failure(APIErrorUtils.parseError(response))
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback.failure(APIError(t))
            }
        })
    }

    fun viewCartTotal(cartId: String, callback: ApiResponseCallback<CartTotalResponse>) {
        HttpManagerMagento(context).cartService.viewCartTotal(language, cartId).enqueue(object : Callback<CartTotalResponse> {
            override fun onResponse(call: Call<CartTotalResponse>, response: Response<CartTotalResponse>) {
                if (response.body() != null && response.isSuccessful) {
                    callback.success(response.body())
                } else {
                    callback.failure(APIErrorUtils.parseError(response))
                }
            }

            override fun onFailure(call: Call<CartTotalResponse>, t: Throwable) {
                callback.failure(APIError(t))
            }
        })
    }

    private fun clearCartAndRecreateCart(product: Product) {
        db.deleteAllCacheCartItem(object : DatabaseListener {
            override fun onSuccessfully() {
                prefManager.clearCartId()
                cacheCartItems = db.cacheCartItems
                retrieveCart(product)
            }

            override fun onFailure(error: Throwable) {
                // TODO: handle on create cart
            }
        })
    }

    private fun List<CacheCartItem>.hasProduct2h(): Boolean {
        if (cacheCartItems == null || cacheCartItems.isEmpty()) {
            return false
        }

        val item = cacheCartItems.find { it.branch != null }
        return item != null
    }
}

interface AddProductToCartCallback {
    fun onSuccessfully()
    fun forceClearCart()
    fun onFailure(messageError: String)
    fun onFailure(dialog: Dialog)
}