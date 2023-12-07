package com.carlasarai.TicketsKotlin.core

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyBoard(){
    val inn = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inn.hideSoftInputFromWindow(windowToken, 0)
}