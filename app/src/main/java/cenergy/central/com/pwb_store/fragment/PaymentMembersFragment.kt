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
import cenergy.central.com.pwb_store.adapter.MembersAdapter
import cenergy.central.com.pwb_store.activity.interfaces.PaymentProtocol
import cenergy.central.com.pwb_store.manager.listeners.MemberClickListener
import cenergy.central.com.pwb_store.model.EOrderingMember
import cenergy.central.com.pwb_store.model.response.HDLCustomerInfos
import cenergy.central.com.pwb_store.model.response.MemberResponse

class PaymentMembersFragment : Fragment() {

    private var listener: PaymentProtocol? = null
    private var membersHDL: List<HDLCustomerInfos> = listOf()
    private var membersList: List<MemberResponse> = listOf()
    private var eOrderingMembersList: List<EOrderingMember> = listOf()
    private lateinit var recycler: RecyclerView

    companion object {
        fun newInstance(): PaymentMembersFragment {
            val fragment = PaymentMembersFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as PaymentProtocol
        listener?.let {
            membersHDL = it.getHDLMembers()
            eOrderingMembersList = it.getPWBMembers()
            membersList = it.getMembers()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_payment_members, container, false)
        setupView(rootView)
        return rootView
    }

    private fun setupView(rootView: View) {
        recycler = rootView.findViewById(R.id.recycler_view_members)
        val membersAdapter = MembersAdapter()
        membersAdapter.setOnMemberClickListener(context as MemberClickListener)
        recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = membersAdapter
        when {
            membersHDL.isNotEmpty() -> membersAdapter.memberList = membersHDL
            eOrderingMembersList.isNotEmpty() -> membersAdapter.memberList = eOrderingMembersList
            else -> membersAdapter.memberList = membersList
        }
    }
}