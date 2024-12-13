package com.frxhaikal_plg.ingrevia.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.frxhaikal_plg.ingrevia.R
import com.google.android.material.textfield.TextInputEditText

class CustomHeightEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (!isValidHeight(s)) {
                    context.getString(R.string.height_must_be_valid)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isValidHeight(height: CharSequence?): Boolean {
        if (height.isNullOrEmpty()) return false
        return try {
            val heightValue = height.toString().toInt()
            heightValue in 100..250
        } catch (e: NumberFormatException) {
            false
        }
    }
}
