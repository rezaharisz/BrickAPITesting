package io.onebrick.sdk.ui.institutions
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import io.onebrick.sdk.CoreBrickSDK
import io.onebrick.sdk.IRequestInstituion
import io.onebrick.sdk.R
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.model.Institution
import io.onebrick.sdk.model.InstitutionGrouped.data
import io.onebrick.sdk.ui.bank.BankCommonActivity
import io.onebrick.sdk.ui.common.BaseActivity
import io.onebrick.sdk.ui.common.ListViewHelper
import io.onebrick.sdk.ui.common.SubmitInstitutionActivity
import io.onebrick.sdk.ui.ewallet.EwalletActivity
import io.onebrick.sdk.util.EWALLET
import io.onebrick.sdk.util.TrackingManager
import io.onebrick.sdk.util.insti_selection_visited
import io.onebrick.sdk.util.insti_suggest_button_clicked
import org.json.JSONObject


class ListInstitution : BaseActivity() {
    private lateinit var institution: Institution
    private lateinit var listViewInstitution:ExpandableListView
    private lateinit var filterTopWrapper: LinearLayout
    private lateinit var adapter: ExpandableListAdapter
    private lateinit var searchView:SearchView
    private lateinit var submitInstitutionButton: Button
    private var expandableListTitle: List<String>? = null
    private lateinit var radioGroup: RadioGroup



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.fetchAllInstitution()
        setContentView(R.layout.activity_list_institution)
        listViewInstitution = findViewById(R.id.lv_institution)
        filterTopWrapper = findViewById(R.id.filter_top)

        radioGroup = findViewById(R.id.radio_group_selected)
        submitInstitutionButton = findViewById(R.id.submit_institution)
        searchView = findViewById(R.id.searchView)
        institution = Institution()
        listViewInstitution.setGroupIndicator(null)

        submitInstitutionButton.setOnClickListener{

            val userProperties = JSONObject()
            userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
            userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

            val eventProperties = JSONObject()
            eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)

            TrackingManager.trackEvent(insti_suggest_button_clicked, applicationContext, application, eventProperties, userProperties)

            openSubmitInstitution()
        }

        showBackButton()
        initCloseButton()
        initLanguageButton(baseContext, this)

        searchView.queryHint = getString(R.string.search)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            adapter.filterByCategories(radio.text.toString())
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                adapter.filter.filter(text.toString())
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                adapter.filter.filter(text.toString())
                return false
            }
        })


        val userProperties = JSONObject()
        userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
        userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

        val eventProperties = JSONObject()
        eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
        eventProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)
        eventProperties.put("public_token", ConfigStorage.barrierToken)

        TrackingManager.trackEvent(insti_selection_visited, applicationContext, application, eventProperties, userProperties)

    }

    private fun openSubmitInstitution() {
        val brickCoreUIIntent = Intent(this, SubmitInstitutionActivity::class.java)
        brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(brickCoreUIIntent)

    }
    private fun initFilter() {

    }

    private fun fetchAllInstitution(){
        showLoadingActivity()
        CoreBrickSDK.listInstitution(object : IRequestInstituion {

            override fun success(response: Institution?) {
                dismissLoadingActivity()
                if (response != null) {
                    institution = response
                    initializedListView()
                    initFilter()
                }
            }

            override fun error(t: Throwable?) {
                dismissLoadingActivity()
            }

        })
    }



    @SuppressLint("InflateParams")
    fun initializedListView() {
        val listData = data

        expandableListTitle = ArrayList(data.keys.sortedByDescending { data[it]?.size })

        adapter = ExpandableListAdapter(
                this,
                listViewInstitution,
                expandableListTitle as ArrayList<String>,
                listData
        )
        val emptyView: View = layoutInflater.inflate(R.layout.empty_view, null)
        addContentView(emptyView, listViewInstitution.layoutParams) // You're using the same params as listView

        listViewInstitution.emptyView = emptyView
        listViewInstitution.setAdapter(adapter)

        var count = 0
        institution.data.groupBy { it.type}.forEach{
            count ++
            repeat(it.value.size) {
                count++
            }
        }

        ListViewHelper().getListViewSize(listViewInstitution, count)

        listViewInstitution.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->

            val data = adapter.dataCopy[(adapter.titleListCopy)[groupPosition]]!![childPosition]
                ConfigStorage.setCurrentInstitution(data)
                if (data.type == EWALLET) {
                    val brickCoreUIIntent = Intent(this, EwalletActivity::class.java)
                    brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(brickCoreUIIntent)

                } else {
                    val brickCoreUIIntent = Intent(this, BankCommonActivity::class.java)
                    brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(brickCoreUIIntent)
                }


            false
        }
    }
}