package io.onebrick.sdk.ui.institutions

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.onebrick.sdk.R
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.model.InstitutionData
import io.onebrick.sdk.ui.common.ListViewHelper
import io.onebrick.sdk.model.InstitutionGrouped.data
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ExpandableListAdapter internal constructor(
        private val context: Context,
        private val listView:ExpandableListView,
        private var titleList: List<String>,
        private var dataList: HashMap<String, List<InstitutionData>>
) : BaseExpandableListAdapter(), Filterable {
    var dataCopy: HashMap<String, List<InstitutionData>> = dataList
    var titleListCopy:  List<String> = titleList

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataCopy[this.titleListCopy[listPosition]]!![expandedListPosition]
    }
    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }
    override fun getChildView(
            listPosition: Int,
            expandedListPosition: Int,
            isLastChild: Boolean,
            convertView: View?,
            parent: ViewGroup
    ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as InstitutionData
        if (convertView == null) {
            val layoutInflater =
                    this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.expandedListItem)
        expandedListTextView.text = expandedListText.bankName
        return convertView
    }
    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataCopy[this.titleListCopy[listPosition]]!!.size
    }
    override fun getGroup(listPosition: Int): Any {
        return this.titleListCopy[listPosition]
    }
    override fun getGroupCount(): Int {
        return this.titleListCopy.size
    }
    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }
    override fun getGroupView(
            listPosition: Int,
            isExpanded: Boolean,
            convertView: View?,
            parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                    this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }

        val mExpandableListView = parent as ExpandableListView
        mExpandableListView.expandGroup(listPosition)
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.expandedListItem)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()

                if (charString.isEmpty()) {
                    dataCopy = data
                    titleListCopy = ArrayList(data.keys.sortedByDescending { data[it]?.size })
                } else {
                    dataCopy = dataList
                    titleListCopy = ArrayList(dataCopy.keys.sortedByDescending { dataCopy[it]?.size })
                    val expandableListDetail = HashMap<String, List<InstitutionData>>()
                    for (row in titleList.indices) {

                        val institutionList = dataCopy[titleList[row]]

                        val instData :MutableList<InstitutionData> = mutableListOf()

                        if (institutionList != null) {
                            for(instDetail in institutionList) {

                                if(instDetail.bankName.toLowerCase(Locale.getDefault()).contains(charString.toLowerCase(
                                        Locale.getDefault()
                                    )
                                    )
                                ) {
                                    instData.add(instDetail)
                                }
                            }
                        }
                        if(instData.size > 0){
                           expandableListDetail[titleList[row]] = instData
                        }

                    }
                    dataCopy = expandableListDetail

                }
                Log.v("BRICK", dataCopy.toString())
                val filterResults = FilterResults()
                filterResults.values = dataCopy
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                var totalData = 0
                dataCopy = filterResults.values as HashMap<String, List<InstitutionData>>
                titleListCopy = arrayListOf(dataCopy.keys.toString())
                titleListCopy = dataCopy.keys.toList().sortedByDescending { data[it]?.size }
                titleListCopy.forEach {
                    totalData += dataCopy[it]?.size!!
                }

                Log.v("BRICK", totalData.toString())
                notifyDataSetChanged()

            }
        }
    }

    fun filterByCategories(categories: String?) {
        Log.v("BRICK", categories.toString())
        var totalData = 0

        if(categories == "All" || categories == "Semua") {
            dataCopy = dataList
            titleListCopy = titleList
            ConfigStorage.institutionList.data.groupBy { it.type}.forEach{
                repeat(it.value.size) {
                    totalData++
                }
            }
            ListViewHelper().getListViewSize(listView, totalData +titleListCopy.size)
            notifyDataSetChanged()
        } else {
            dataCopy = dataList
            titleListCopy = titleList

            val filteredMap = dataCopy.filterKeys { it.contains(categories.toString()) }

            dataCopy = filteredMap as HashMap<String, List<InstitutionData>>
            titleListCopy = arrayListOf(categories.toString())
            totalData = dataCopy[categories]?.size!!
            Log.v("BRICK", dataCopy[categories]?.size.toString())

            Log.v("BRICK", totalData.toString())
            ListViewHelper().getListViewSize(listView, totalData +1)
            notifyDataSetChanged()
        }

    }
    override fun hasStableIds(): Boolean {
        return false
    }
    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
}