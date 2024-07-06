package com.thuc0502.generateqrcode

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.util.UUID

fun generateRandomFileName(extension: String): String {
    return UUID.randomUUID().toString() + extension
}
fun FragmentManager.replaceFragment(containerViewId: Int ,fragment: Fragment ,tag: String) {
    this.beginTransaction().apply {
        replace(containerViewId, fragment, tag)
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        addToBackStack(null)
        commit()
    }
}

