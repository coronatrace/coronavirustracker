package com.coronatrace.covidtracker.data.source.remote

import android.content.Context
import com.coronatrace.covidtracker.auth.Auth

class AppAPI(context: Context) {

    private var isAuthenticated = false

    // Initialise Auth
    init {
        if (!isAuthenticated) {
            val auth = Auth(context)
           isAuthenticated = true
        }
    }


}