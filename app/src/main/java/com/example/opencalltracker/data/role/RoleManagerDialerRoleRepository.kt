package com.example.opencalltracker.data.role

import android.app.role.RoleManager
import android.content.Context
import com.example.opencalltracker.domain.repository.DialerRoleRepository

class RoleManagerDialerRoleRepository(private val context: Context) : DialerRoleRepository {
    override fun isDefaultDialer(): Boolean = context.getSystemService(RoleManager::class.java).isRoleHeld(RoleManager.ROLE_DIALER)
}
