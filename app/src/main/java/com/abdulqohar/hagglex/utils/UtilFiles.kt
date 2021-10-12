package com.abdulqohar.hagglex.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.makeSnackbar(message: String){
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
}

const val TOKENKEY = "token"