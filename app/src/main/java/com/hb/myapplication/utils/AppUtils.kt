package com.hb.myapplication.utils

import android.text.Editable
import com.google.android.material.textfield.TextInputLayout


object AppUtils {

    fun setupInput(input: TextInputLayout) {
        input.setOnKeyListener { _, _, _ ->
            if (isNotEmpty(input.editText!!.text)) {
                input.error = null
            }
            false
        }
    }

    private fun isNotEmpty(text: Editable?): Boolean {
        return text != null && text.isNotEmpty()
    }
}
