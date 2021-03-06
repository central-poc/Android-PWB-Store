package cenergy.central.com.pwb_store.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cenergy.central.com.pwb_store.R
import cenergy.central.com.pwb_store.adapter.viewholder.OrderProductListViewHolder
import cenergy.central.com.pwb_store.model.Item

class OrderProductListAdapter : RecyclerView.Adapter<OrderProductListViewHolder>() {

    var listItems : List<Item> = arrayListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductListViewHolder {
        return OrderProductListViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_order_product, parent, false))
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: OrderProductListViewHolder, position: Int) {
        holder.bindView(listItems[position])
    }
}
