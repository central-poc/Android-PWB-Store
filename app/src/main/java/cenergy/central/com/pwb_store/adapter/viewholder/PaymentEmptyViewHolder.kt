package cenergy.central.com.pwb_store.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import cenergy.central.com.pwb_store.R
import cenergy.central.com.pwb_store.dialogs.interfaces.PaymentTypeClickListener
import cenergy.central.com.pwb_store.model.response.PaymentMethod

class PaymentEmptyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.tv_title)
    val button: Button = itemView.findViewById(R.id.choose_payment_method)

    fun bindView(paymentMethod: PaymentMethod, listener: PaymentTypeClickListener) {
        title.text = paymentMethod.title
        itemView.setOnClickListener { listener.onPaymentTypeClickListener(paymentMethod) }
        button.setOnClickListener { listener.onPaymentTypeClickListener(paymentMethod) }
    }
}