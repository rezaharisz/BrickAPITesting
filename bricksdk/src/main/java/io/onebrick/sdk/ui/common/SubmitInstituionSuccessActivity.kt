package io.onebrick.sdk.ui.common

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import io.onebrick.sdk.R
import io.onebrick.sdk.model.ConfigStorage
import io.onebrick.sdk.ui.institutions.ListInstitution

class SubmitInstituionSuccessActivity : BaseActivity() {

    private lateinit var buttonBack:Button
    private lateinit var listButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_instituion_success)

        buttonBack = findViewById(R.id.connect)
        listButton = findViewById(R.id.button_cancel)

        initLanguageButton(baseContext, this)
        showBackButton()
        initCloseButton()

        listButton.text = String.format(
            getString(R.string.backToAppName),
            ConfigStorage.appName
        )
        listButton.setOnClickListener{
            finish()
            val intent = Intent(this, LandingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
            startActivity(intent)
        }

        buttonBack.setOnClickListener{
            finish()
            val intent = Intent(this, ListInstitution::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
            startActivity(intent)
        }

    }


}