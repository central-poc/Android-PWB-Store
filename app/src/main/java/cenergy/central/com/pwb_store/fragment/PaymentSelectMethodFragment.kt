package cenergy.central.com.pwb_store.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cenergy.central.com.pwb_store.R
import cenergy.central.com.pwb_store.activity.interfaces.PaymentProtocol
import cenergy.central.com.pwb_store.adapter.PaymentMethodAdapter
import cenergy.central.com.pwb_store.dialogs.ChangeTheOneDialogFragment
import cenergy.central.com.pwb_store.dialogs.interfaces.PaymentTypeClickListener
import cenergy.central.com.pwb_store.model.response.MemberResponse
import cenergy.central.com.pwb_store.model.response.PaymentMethod
import cenergy.central.com.pwb_store.utils.Analytics
import cenergy.central.com.pwb_store.utils.Screen
import cenergy.central.com.pwb_store.view.PowerBuyEditTextBorder
import cenergy.central.com.pwb_store.view.PowerBuyTextView

class PaymentSelectMethodFragment : Fragment() {
    private val analytics by lazy { context?.let { Analytics(it) } }

    private lateinit var paymentProtocol: PaymentProtocol
    private lateinit var recycler: RecyclerView
    private lateinit var paymentTypeClickListener: PaymentTypeClickListener
    private lateinit var selectMethodAdapter: PaymentMethodAdapter

    private var paymentMethods: List<PaymentMethod> = listOf()
    private var deliveryCode: String = ""

    companion object {
        private const val ARG_DELIVERY_CODE = "arg_delivery_code"
        fun newInstance(methodCode: String): PaymentSelectMethodFragment {
            val fragment = PaymentSelectMethodFragment()
            val args = Bundle()
            args.putString(ARG_DELIVERY_CODE, methodCode)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        paymentProtocol = context as PaymentProtocol
        paymentMethods = paymentProtocol.getPaymentMethods()
        paymentTypeClickListener = context as PaymentTypeClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deliveryCode = it.getString(ARG_DELIVERY_CODE, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_payment_select_methods, container, false)
        setupView(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPaymentMethodOptions()
    }

    override fun onResume() {
        super.onResume()
        analytics?.trackScreen(Screen.SELECT_PAYMENT)
    }

    private fun setupPaymentMethodOptions() {
        // add more filter?
        selectMethodAdapter.paymentMethodItems = paymentMethods
    }

    private fun setupView(rootView: View) {
        recycler = rootView.findViewById(R.id.recycler_select_methods)
        selectMethodAdapter = PaymentMethodAdapter(paymentTypeClickListener)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = selectMethodAdapter

    }
}