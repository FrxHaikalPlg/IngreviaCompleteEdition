package com.frxhaikal_plg.ingrevia.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.frxhaikal_plg.ingrevia.R
import com.google.android.material.textfield.TextInputEditText

class CustomPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (!isValidPassword(s)) {
                    context.getString(R.string.password_must)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isValidPassword(password: CharSequence?): Boolean {
        return !password.isNullOrEmpty() && password.length >= 8
    }
}