package cenergy.central.com.pwb_store.manager.listeners

import cenergy.central.com.pwb_store.model.AddressInformation

interface PaymentBillingListener{
    fun saveAddressInformation(shippingAddress: AddressInformation, billingAddress: AddressInformation? = null, t1cNumber: String = "")
}