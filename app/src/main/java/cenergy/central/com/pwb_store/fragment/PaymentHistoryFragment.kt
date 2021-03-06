package cenergy.central.com.pwb_store.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenergy.central.com.pwb_store.R
import cenergy.central.com.pwb_store.adapter.HistoryAdapter
import cenergy.central.com.pwb_store.manager.listeners.HistoryClickListener
import cenergy.central.com.pwb_store.realm.RealmController
import cenergy.central.com.pwb_store.view.PowerBuyTextView

class PaymentHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtResult: PowerBuyTextView
    private var listener: HistoryClickListener? = null
    private var orders = RealmController.getInstance().orders

    companion object {
        fun newInstance(): PaymentHistoryFragment {
            val fragment = PaymentHistoryFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as HistoryClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = LayoutInflater.from(context).inflate(R.layout.fragment_history_payment, container, false)
        setupView(rootView)
        return rootView
    }

    private fun setupView(rootView: View) {
        recyclerView = rootView.findViewById(R.id.recycler_view_history)
        txtResult = rootView.findViewById(R.id.txt_result)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = listener?.let { HistoryAdapter(it, this.orders) }
        if (this.orders.size > 0) {
            recyclerView.visibility = View.VISIBLE
            txtResult.visibility = View.INVISIBLE
        } else {
            recyclerView.visibility = View.INVISIBLE
            txtResult.visibility = View.VISIBLE
        }
    }
}