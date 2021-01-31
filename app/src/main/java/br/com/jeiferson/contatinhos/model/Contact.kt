package br.com.jeiferson.contatinhos.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Contact(
    val name: String = "",
    var phoneNumber: String = "",
    var email: String = ""
): Parcelable
