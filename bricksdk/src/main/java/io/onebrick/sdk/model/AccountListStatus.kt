package io.onebrick.sdk.model

public data class AccountListStatus (
        val status: Long? = null,
        val message: String? = null,
        val data: List<AccountListStatusData>? = null
)

public data class AccountListStatusData (
        val accountId: String? = null,
        val accountHolder: String? = null,
        val accountNumber: String? = null,
        val balances: AccountListStatusBalances? = null
)

public data class AccountListStatusBalances (
        val available: Double? = null,
        val current: Double? = null
)
