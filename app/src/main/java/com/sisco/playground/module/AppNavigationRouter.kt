package com.sisco.playground.module

import android.content.Context
import android.content.Intent
import android.util.Log

const val PARENT_PACKAGE = "com.sisco"
const val PACKAGE_PROFILE = "$PARENT_PACKAGE.setting"

fun openProfileActivity(context: Context){
    try {
        // Membuat intent
        val intent = Intent(context, Class.forName("com.sisco.profile.ProfileScreen"))
        // Menambahkan data menuju activity selanjutnya
        context.startActivity(intent)
    }catch (e: Exception){
        Log.e("navigation", "$e")
    }
}