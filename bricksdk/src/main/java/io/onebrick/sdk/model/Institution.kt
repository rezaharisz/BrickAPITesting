package io.onebrick.sdk.model

import android.util.Log
import com.google.gson.annotations.SerializedName

data class Institution(
        val status: Long = 1,
        val message: String = "",
        val data: List<InstitutionData> = emptyList()
)

data class InstitutionData(
        val id: Long = 1,
        val bankName: String = "",
        val bankCode: String? = null,
        val countryName: String = "",
        val type: String = ""
)

class TransformMenuModel(title: String, section: Boolean, institutionData: InstitutionData) {
    private var titleString: String = title
    private var isSection:Boolean = section
    var data: InstitutionData? = institutionData

    fun getSection(): Boolean {
        return this.isSection
    }
}

data class SubmitInstitutionPayload(
        @SerializedName("institutionName") val institutionName: String? = null,
        @SerializedName("institutionType") val institutionType: String? = null

)

data class InstitutionResponseSubmit(
        val status: Long = 1,
        val message: String = "",
        val data: String = ""
)

internal object InstitutionGrouped {
    val data: HashMap<String, List<InstitutionData>>
        get() {
            val expandableListDetail = HashMap<String, List<InstitutionData>>()

            ConfigStorage.institutionList.data.groupBy { it.type}.forEach{ it
                val arrayOfInstitution: MutableList<InstitutionData> = ArrayList()
                it.value.forEach { itData ->
                    arrayOfInstitution.add(itData)
                }
                Log.v("BRICK",it.key)
                expandableListDetail[it.key] = arrayOfInstitution
            }
            Log.v("BRICK",expandableListDetail.toString())
            return expandableListDetail
        }
}