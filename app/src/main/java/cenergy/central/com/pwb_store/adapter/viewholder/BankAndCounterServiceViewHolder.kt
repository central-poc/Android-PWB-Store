package cenergy.central.com.pwb_store.adapter.viewholder

import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import cenergy.central.com.pwb_store.R
import cenergy.central.com.pwb_store.adapter.PaymentMethodViewHolder
import cenergy.central.com.pwb_store.dialogs.interfaces.PaymentItemClickListener
import cenergy.central.com.pwb_store.model.PaymentMethodView

class BankAndCounterServiceViewHolder(itemView: View, private val listener: PaymentItemClickListener)
    : PaymentMethodViewHolder<PaymentMethodView.PaymentItemView>(itemView) {
    private val radioPayment: RadioButton = itemView.findViewById(R.id.radioPayment)
    private val description: TextView = itemView.findViewById(R.id.tv_description)

    override fun bindView(item: PaymentMethodView.PaymentItemView) {
        radioPayment.text = itemView.context.getString(R.string.pay_by_bank_transfer)
        description.text = ""
        itemView.setOnClickListener { listener.onClickedItem(item.paymentMethod) }
    }
}