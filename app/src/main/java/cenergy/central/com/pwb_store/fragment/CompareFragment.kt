package cenergy.central.com.pwb_store.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenergy.central.com.pwb_store.R
import cenergy.central.com.pwb_store.activity.interfaces.CompareProtocol
import cenergy.central.com.pwb_store.adapter.CompareProductAdapter
import cenergy.central.com.pwb_store.extensions.getDetailList
import cenergy.central.com.pwb_store.model.response.CompareProductResponse
import cenergy.central.com.pwb_store.realm.RealmController
import kotlinx.android.synthetic.main.fragment_compare.*

class CompareFragment : Fragment() {

    private var mAdapter: CompareProductAdapter? = null

    private val database = RealmController.getInstance()
    private lateinit var listener: CompareProtocol
    private var compareProductDetailList: List<CompareProductResponse> = arrayListOf()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context != null) {
            //TODO Use this List to make compare product detail
            listener = context as CompareProtocol
            compareProductDetailList = listener.getCompareProductDetailList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_compare, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = context?.let { CompareProductAdapter(it) }
        compareRecyclerView.layoutManager = LinearLayoutManager(context)
        compareRecyclerView.adapter = mAdapter

        val compareProducts = database.compareProducts
        mAdapter?.updateCompareProducts(compareProducts, compareProductDetailList.getDetailList())
    }

    companion object {
        fun newInstance(): CompareFragment {
            val fragment = CompareFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
