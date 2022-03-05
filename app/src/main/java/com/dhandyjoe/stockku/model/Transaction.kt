package com.dhandyjoe.stockku.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Transaction (
    var id: String = "",
    var name: String = "",
    var date: String = ""
): Parcelable