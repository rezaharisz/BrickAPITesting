package io.onebrick.sdk.ui.common

import android.util.Log
import android.view.ViewGroup
import android.widget.ExpandableListView


class ListViewHelper {
    fun getListViewSize(myListView: ExpandableListView, totalData: Int) {
        val params: ViewGroup.LayoutParams = myListView.layoutParams
        var integer:Int =totalData
        Log.v("BRICK_HEIGHT", integer.toString())
        if(totalData < 2) {
            integer = 300
        }
        params.height =
                (integer*150) + myListView.dividerHeight
        Log.v("BRICK_HEIGHT", params.height.toString())
        myListView.layoutParams = params
    }
}