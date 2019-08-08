package cenergy.central.com.pwb_store.activity.interfaces

import cenergy.central.com.pwb_store.model.*
import cenergy.central.com.pwb_store.model.response.MemberResponse
import cenergy.central.com.pwb_store.model.response.PaymentMethod
import java.util.*

/**
 * Created by Anuphap Suwannamas on 12/9/2018 AD.
 * Email: Anupharpae@gmail.com
 */

interface PaymentProtocol {
    fun getItems(): List<CartItem>
    fun getPWBMembers(): List<PwbMember>
    fun getMembers(): List<MemberResponse>
    fun getDeliveryOptions(): List<DeliveryOption>
    fun getShippingAddress(): AddressInformation?
    fun getBillingAddress(): AddressInformation?
    fun getSelectedDeliveryType(): DeliveryType?
    fun getEnableDateShipping(): ArrayList<ShippingSlot>
    fun getBranches(): ArrayList<Branch>
    fun getSelectedBranch(): Branch?
    fun getPWBMemberByIndex(index: Int): PwbMember?
    fun getPaymentMethods(): List<PaymentMethod>
    fun getT1CardNumber(): String
}