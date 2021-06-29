//package com.io.io.sdk.ui.institutions
//
//import android.app.Activity
//import android.content.Context
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import io.onebrick.sdk.R
//import TransformMenuModel
//
//
//class ViewHolder {
//    var txtTitle: TextView? = null
//    var txtDescription: TextView? = null
//}
//
//internal class  InstitutionListAdapter(private val context: Activity, private val rawData: List<TransformMenuModel>)
//    : ArrayAdapter<TransformMenuModel>(context, R.layout.institution_list, rawData), Filterable, ExpandableListAdapter {
//    private var groupedDataSource:List<TransformMenuModel> = rawData
//    private val mContext: Context? = null
//    private var lastPosition = -1
//
//
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
//                groupedDataSource = filterResults.values as List<TransformMenuModel>
//
//                if(groupedDataSource.isNotEmpty()) {
//                    var user: List<TransformMenuModel> = groupedDataSource
//                    user?.let { Log.v("BRICK", user.toString())}
//                }
//
//                notifyDataSetChanged()
//            }
//
//            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
//                val queryString = charSequence?.toString()?.toLowerCase()
//                queryString?.let { Log.v("BRICK", it) }
//                val filterResults = Filter.FilterResults()
//
//                filterResults.values = if (queryString == null || queryString.isEmpty())
//                    rawData
//                else
//                    rawData.filter {
//                        it.data?.bankName?.toLowerCase()?.contains(queryString) == true ||
//                                it.data?.type?.toLowerCase()?.contains(queryString) == true
//                    }
//
//                return filterResults
//            }
//        }
//    }
//
//    override fun getCount(): Int {
//        return groupedDataSource.size
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    override fun getItem(p0: Int): TransformMenuModel? {
//        return groupedDataSource[p0]
//    }
//
//    override fun getViewTypeCount(): Int {
//        return 2
//    }
//
//
//    override fun getItemId(p0: Int): Long {
//        return groupedDataSource[p0].data?.id?.toLong()!!
//    }
//
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//
//        val inflater = context.layoutInflater
//        val viewHolder: ViewHolder
//
//        var rowView = inflater.inflate(R.layout.institution_list, null, true)
//        val titleText = rowView.findViewById(R.id.title) as TextView
//        if(this.groupedDataSource[position].data?.bankName != "") {
//            titleText.text = groupedDataSource[position].data?.bankName
//        } else {
//            rowView = inflater.inflate(R.layout.section_layout, null, true)
//            val titleText = rowView.findViewById(R.id.brick_section_title) as TextView
//            titleText.text = groupedDataSource[position].titleString
//        }
//        return rowView
//    }
//}

