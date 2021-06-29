package io.onebrick.sdk.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import io.onebrick.sdk.R
import io.onebrick.sdk.util.LANGUAGE
import com.mohamedabulgasem.loadingoverlay.LoadingAnimation
import com.mohamedabulgasem.loadingoverlay.LoadingOverlay
import java.util.*


@Suppress("DEPRECATION")
open class BaseActivity : Activity() {

    private lateinit var toolbarCommon: View
    private var currentLanguage = "en"
    lateinit var locale: Locale
    private var currentLang: String? = null
    private lateinit var backButton:Button
    private val loadingOverlay: LoadingOverlay by lazy {
        LoadingOverlay.with(
            context = this,
            dimAmount = 1.0f,
            animation = LoadingAnimation(
                rawRes = R.raw.loading,
                dimens = 200
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        toolbarCommon = findViewById(R.id.toolbarCommon)

        showBackButton()
        initCloseButton()
        showErrorMessage(false,"")
        showSuccessMessage(false,"")
        initLanguageButton(baseContext, this,true)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }


    open fun initCloseButton() {
        val view = findViewById<View>(R.id.toolbarCommon)
        var closeButton:Button = view.findViewById(R.id.close_button) as Button
        closeButton.setOnClickListener{

            if (isTaskRoot) {
                finish()
            } else {
                val intent = Intent(this, LandingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
                startActivity(intent)
            }
        }
    }

    fun initLanguageButton(context: Context, from: Activity, show:Boolean=false) {

        val view = findViewById<View>(R.id.toolbarCommon)
        var radioGroup:RadioGroup =  view.findViewById(R.id.languageButton) as RadioGroup
        var worldIcon:ImageView =  view.findViewById(R.id.world_icon) as ImageView
        var ll:LinearLayout =  view.findViewById(R.id.ll) as LinearLayout
        if(!show) {
            worldIcon.visibility = View.GONE
            ll.visibility = View.GONE
        }
        if(loadData(context, "CUR_LANG", "SP") === "id") {
            radioGroup.check(R.id.endID)
        } else {
            radioGroup.check(R.id.endRB)
        }
            radioGroup.setOnCheckedChangeListener { radioGroup, _ ->
                var selectedId = radioGroup.checkedRadioButtonId
                val radioButton: View = radioGroup.findViewById(selectedId)
                val idx: Int = radioGroup.indexOfChild(radioButton)
                val radioButtonChild: RadioButton = radioGroup.getChildAt(idx) as RadioButton
                if(radioButtonChild.tag.toString() == "12") {
                    saveData(context, LANGUAGE, "id", "SP")
                    setLocale(from, "id")
                } else {
                    saveData(context, LANGUAGE, "en", "SP")
                    setLocale(from, "en")
                }

            }
    }

    private fun setLocale(activity: Activity, localeName: String) {
        val currentLanguage = intent.getStringExtra(currentLang).toString()
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this,
                activity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            refresh.putExtra("showNotif","false")
            startActivity(refresh)
            activity.finish()
        } else {

        }
    }
    fun showBackButton() {
        val view = findViewById<View>(R.id.toolbarCommon)
        backButton =  view.findViewById(R.id.back_button) as Button
        backButton.setOnClickListener{

            if (isTaskRoot) {
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
    fun hideBackButton() {
        toolbarCommon = findViewById(R.id.toolbarCommon)
        backButton =  toolbarCommon.findViewById(R.id.back_button) as Button
        backButton.visibility = View.GONE

    }

    override fun onBackPressed() = if (isTaskRoot) {
        finish()
    } else {
        super.onBackPressed()
    }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
    fun showToast(message: String) {
        val toast = Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun redirectToMFAPage() {
        val brickCoreUIIntent = Intent(this, CommonPINActivity::class.java)
        brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(brickCoreUIIntent)
    }

    fun redirectToThankYouPage() {
        val brickCoreUIIntent = Intent(this, EndActivity::class.java)
        brickCoreUIIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(brickCoreUIIntent)
    }


    // ...
    fun dismissLoadingActivity() {
        loadingOverlay.dismiss()
    }

    open fun saveData(context: Context, key: String, value: String, sp: String) {
        val sharedPreferences = context.getSharedPreferences(sp, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    open fun loadData(context: Context, key: String, sp: String): String? {
        val sharedPreferences = context.getSharedPreferences(sp, MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }

    fun showLoadingActivity() {
        loadingOverlay.show()
    }

    fun showErrorMessage(show:Boolean,message:String) {
        var errorMessage :View = findViewById(R.id.error_badge)

        if(!show) {
            errorMessage.visibility = View.GONE
        } else {
            errorMessage.visibility = View.VISIBLE
            var  successMessageTextView:TextView =  errorMessage.findViewById(R.id.error_message_text) as TextView
            successMessageTextView.text = message

        }
    }
    fun showSuccessMessage(show:Boolean,message:String) {
        var successMessage:View = findViewById(R.id.success_badge)
        if(!show) {
            successMessage.visibility = View.GONE
        } else {
            successMessage.visibility = View.VISIBLE
            var  errorMessageTextView:TextView =  successMessage.findViewById(R.id.success_message_text) as TextView
            errorMessageTextView.text = message


        }
    }
}