package cenergy.central.com.pwb_store.manager.listeners

import cenergy.central.com.pwb_store.model.DeliveryOption

interface DeliveryOptionsListener{
    fun onSelectedOptionListener(deliveryOption: DeliveryOption)
}