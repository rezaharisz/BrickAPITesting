package io.onebrick.sdk.ui.institutions
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
    var expandableListTitle: List<String>? = null
    private lateinit var radioGroup: RadioGroup



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.fetchAllInstitution()
        setContentView(R.layout.activity_list_institution)
        listViewInstitution = findViewById<ExpandableListView>(R.id.lv_institution)
        filterTopWrapper = findViewById<LinearLayout>(R.id.filter_top)

        radioGroup = findViewById<RadioGroup>(R.id.radio_group_selected)
        submitInstitutionButton = findViewById<Button>(R.id.submit_institution)
        searchView = findViewById<SearchView>(R.id.searchView)
        institution = Institution()
        listViewInstitution.setGroupIndicator(null)

        submitInstitutionButton.setOnClickListener{

            var userProperties = JSONObject()
            userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
            userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

            var eventProperties = JSONObject()
            eventProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)

            TrackingManager.trackEvent(insti_suggest_button_clicked, applicationContext, application, eventProperties, userProperties)

            openSubmitInstitution()
        }

        showBackButton()
        initCloseButton()
        initLanguageButton(baseContext, this)

        searchView.queryHint = getString(R.string.search);

        radioGroup.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = findViewById(checkedId)
                    adapter.filterByCategories(radio.text.toString(), true)
                })
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


        var userProperties = JSONObject()
        userProperties.put("client_id", ConfigStorage.accessTokenRequest.data.clientId)
        userProperties.put("client_email", ConfigStorage.accessTokenRequest.data.clientEmail)

        var eventProperties = JSONObject()
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
    private fun initFilter(institutionList: Institution) {

    }

    private fun fetchAllInstitution(){
        showLoadingActivity()
        CoreBrickSDK.listInstitution(object : IRequestInstituion {

            override fun success(institutions: Institution?) {
                dismissLoadingActivity()
                if (institutions != null) {
                    institution = institutions
                    initializedListView(institution)
                    initFilter(institution)
                }
            }

            override fun error(t: Throwable?) {
                dismissLoadingActivity()
            }

        })
    }



    fun initializedListView(institutionList: Institution) {
        if (listViewInstitution != null) {
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
            institution.data.groupBy { it.type}.forEach{ it
                count ++
                it.value.forEach {
                    count ++
                }
            }

            ListViewHelper().getListViewSize(listViewInstitution, count)

            listViewInstitution.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->

                var data = adapter.dataCopy[(adapter.titleListCopy)[groupPosition]]!![childPosition]
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
}