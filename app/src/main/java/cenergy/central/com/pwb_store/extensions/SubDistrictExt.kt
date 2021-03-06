package cenergy.central.com.pwb_store.extensions

import cenergy.central.com.pwb_store.model.Postcode
import cenergy.central.com.pwb_store.model.SubDistrict

fun List<SubDistrict>.getPostcodeList(subDistrictId: String): List<Postcode> {
    val postcodeList = arrayListOf<Postcode>()
    forEach { if (it.subDistrictId == subDistrictId) postcodeList.add(it.asPostcode()) }
    return postcodeList
}

fun List<SubDistrict>.toDistinctId(): List<SubDistrict> {
    return this.distinctBy { it.subDistrictId }
}

fun SubDistrict.asPostcode(): Postcode {
    return Postcode(postcodeId = postcodeId, subDistrictId = subDistrictId, postcode = postcode)
}