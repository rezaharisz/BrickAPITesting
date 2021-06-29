package io.onebrick.sdk.util

const val URL_STAGING_v2 = "https://sandbox.onebrick.io"
const val URL_PRODUCTION_V2 = "https://api.onebrick.io/"

const val AMP_PROD = "d0494808544696fca1aa166b1575ff76"
const val AMP_SANDBOX = "f195edc9416b5214d2d7cfe7da104929"
const val SOURCE = "sdk-android"
const val INTERNET_BANKING = "Internet Banking"
const val MBANKING = "Mobile Banking"
const val EWALLET = "E-Wallet"
const val INCOME_VERIFICATION = "Income Verification"

const val OVO = "OVO"
const val JENIUS = "Jenius/BTPN"
const val MOCKBANK = "mock bank"
const val LANGUAGE = "CUR_LANG"
enum class MESSAGETYPE {
    FAILED,SUCCESS
}
enum class BankType {
    COMMON_BANK,IBANK, WALLET
}


const val login_submit = "login_submit"
const val login_error = "login_error"
const val otp_submitted = "otp_submitted"
const val login_success = "login_success"
const val consent_page_visited = "consent_page_visited"
const val consent_allowed = "consent_allowed"
const val consent_cancel = "consent_cancel"
const val login_visited = "login_visited"
const val insti_selection_visited = "insti_selection_visited"
const val insti_suggest_button_clicked = "insti_suggest_button_clicked"
const val insti_suggest_submitted = "insti_suggest_submitted"