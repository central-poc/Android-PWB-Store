package cenergy.central.com.pwb_store.adapter.interfaces

import cenergy.central.com.pwb_store.model.response.BranchResponse

/**
 * Created by Anuphap Suwannamas on 13/9/2018 AD.
 * Email: Anupharpae@gmail.com
 */

interface StoreClickListener {
    fun onItemClicked(branchResponse: BranchResponse)
}