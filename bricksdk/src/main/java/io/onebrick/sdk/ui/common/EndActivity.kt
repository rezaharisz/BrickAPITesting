package io.onebrick.sdk.ui.common

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import io.onebrick.sdk.R
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.util.EWALLET
import io.onebrick.sdk.util.INCOME_VERIFICATION
import com.squareup.picasso.Picasso

class EndActivity : BaseActivity() {

    private lateinit var textTitle: TextView
    private lateinit var textSubtitle: TextView
    private lateinit var images: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you_page)

        textTitle = findViewById(R.id.success_message)
        textSubtitle = findViewById(R.id.redirect_message)
        images = findViewById(R.id.brand_logo)
        val institutionType: String = when (ConfigStorage.institutionData.type) {
            EWALLET -> {
                "e-Wallet"
            }
            INCOME_VERIFICATION -> {
                String.format(getString(R.string.account))
            }
            else -> {
                String.format(getString(R.string.bankAccount))
            }
        }
        textTitle.text =  String.format(getString(R.string.successMessage), institutionType)
        textSubtitle.text =  String.format(getString(R.string.redirect), ConfigStorage.appName)
        Picasso.get().load(ConfigStorage.currentImages).into(images)
    }

    override fun onBackPressed() {
        val intent = Intent(this, LandingActivity::class.java)
        intent.putExtra("CLOSE_TO_MAIN",true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
        startActivity(intent)
    }
}