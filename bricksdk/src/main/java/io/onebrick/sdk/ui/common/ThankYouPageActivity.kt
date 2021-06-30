package io.onebrick.sdk.ui.common

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import io.onebrick.sdk.R
import io.onebrick.sdk.model.ConfigStorage
import com.squareup.picasso.Picasso

class ThankYouPageActivity : BaseActivity() {

    private lateinit var textTitle: TextView
    private lateinit var textSubtitle: TextView
    private lateinit var images:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        textTitle = findViewById(R.id.success_message)
        textSubtitle = findViewById(R.id.redirect_message)
        images = findViewById(R.id.brand_logo)
        initLanguageButton(baseContext,this)
        Log.v("BRICK", ConfigStorage.responseOTPRequest.toString())
        textTitle.text =  String.format(getString(R.string.successMessage), ConfigStorage.institutionData.type)
        textSubtitle.text =  String.format(getString(R.string.redirect), ConfigStorage.appName)
        Picasso.get().load(ConfigStorage.currentImages).into(images)
    }
}