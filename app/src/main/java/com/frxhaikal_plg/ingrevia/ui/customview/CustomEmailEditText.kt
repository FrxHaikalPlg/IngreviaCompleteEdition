package com.frxhaikal_plg.ingrevia.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.frxhaikal_plg.ingrevia.R
import com.google.android.material.textfield.TextInputEditText

class CustomEmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = when {
                    !isValidEmail(s) -> context.getString(R.string.invalid_email_format)
                    s != null && s.length < 10 -> context.getString(R.string.email_minimum_length)
                    else -> null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return email?.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() } ?: false
    }
}
