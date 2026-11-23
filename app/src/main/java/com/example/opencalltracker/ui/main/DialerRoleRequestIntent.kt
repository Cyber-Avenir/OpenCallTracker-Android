package com.example.opencalltracker.ui.main

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent

fun createDialerRoleRequestIntent(context: Context): Intent = context.getSystemService(RoleManager::class.java).createRequestRoleIntent(RoleManager.ROLE_DIALER)
