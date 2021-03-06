package cenergy.central.com.pwb_store.model

import cenergy.central.com.pwb_store.Constants.Companion.DEFAULT_SOLD_BY
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.text.NumberFormat
import java.util.*

open class CompareProduct(@PrimaryKey var sku: String = "",
                          var name: String? = "",
                          var price: Double? = 0.0,
                          var specialPrice: Double? = 0.0,
                          var imageUrl: String = "",
                          var inStock: Boolean = false,
                          var maxQty: Int? = 0,
                          var qtyInStock: Int? = 0,
                          var brand: String? = "",
                          var soldBy: String? = null) : RealmObject(), IViewType {

    // for set view type in adapter
    var viewTypeID: Int = 0

    override fun getViewTypeId(): Int {
        return viewTypeID
    }

    override fun setViewTypeId(id: Int) {
        this.viewTypeID = id
    }

    fun normalPrice(unit: String): String {
        return String.format(Locale.getDefault(), "%s %s", unit, NumberFormat.getInstance(Locale.getDefault()).format(price))
    }

    companion object {
        const val FIELD_SKU = "sku"
        @JvmStatic
        fun asCompareProduct(product: Product): CompareProduct {
            return CompareProduct(sku = product.sku, name = product.name,
                    price = product.price,
                    specialPrice = product.specialPrice,
                    imageUrl = product.getImageUrl(),
                    inStock = product.extension?.stokeItem?.isInStock ?: false,
                    brand = product.brand,
                    maxQty = product.extension?.stokeItem?.maxQTY ?: 1,
                    qtyInStock = product.extension?.stokeItem?.qty ?: 0,
                    soldBy = product.soldBy ?: DEFAULT_SOLD_BY)
        }
    }
}