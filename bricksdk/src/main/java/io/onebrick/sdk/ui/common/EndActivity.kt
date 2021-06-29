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

        textTitle = findViewById<TextView>(R.id.success_message)
        textSubtitle = findViewById<TextView>(R.id.redirect_message)
        images = findViewById<ImageView>(R.id.brand_logo)
        var institutionType:String = ConfigStorage.institutionData.type;
        institutionType = if (ConfigStorage.institutionData.type == EWALLET) {
            "e-Wallet"
        }
        else  if (ConfigStorage.institutionData.type == INCOME_VERIFICATION) {
            String.format(getString(R.string.account))
        }
        else {
            String.format(getString(R.string.bankAccount))
        }
        textTitle.text =  String.format(getString(R.string.successMessage), institutionType)
        textSubtitle.text =  String.format(getString(R.string.redirect), ConfigStorage.appName)
        Picasso.get().load(ConfigStorage.currentImages).into(images)
    }

    override fun onBackPressed() {
        val intent = Intent(this, LandingActivity::class.java)
        intent.putExtra("CLOSE_TO_MAIN",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
        startActivity(intent)
    }
}