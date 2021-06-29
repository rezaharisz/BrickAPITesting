package io.onebrick.sdk.model

import android.util.Log
import com.google.gson.annotations.SerializedName

public data class Institution(
        val status: Long = 1,
        val message: String = "",
        val data: List<InstitutionData> = emptyList()
)

public data class InstitutionData(
        val id: Long = 1,
        val bankName: String = "",
        val bankCode: String? = null,
        val countryName: String = "",
        val type: String = ""
)


/**
 * convert into menu models
 */
public class TransformMenuModel {
    var titleString: String
    var isSection:Boolean = false
    var data: InstitutionData? = null

    constructor(title: String, section: Boolean, institutionData: InstitutionData) {
        data = institutionData
        isSection = section
        titleString = title
    }

    fun getSection(): Boolean {
        return this.isSection
    }
}

public data class SubmitInstitutionPayload(
        @SerializedName("institutionName") val institutionName: String? = null,
        @SerializedName("institutionType") val institutionType: String? = null

)

public data class InstitutionResponseSubmit(
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
                it.value.forEach {
                    arrayOfInstitution.add(it)
                }
                Log.v("BRICK",it.key)
                expandableListDetail[it.key] = arrayOfInstitution
            }
            Log.v("BRICK",expandableListDetail.toString())
            return expandableListDetail
        }
}