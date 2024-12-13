package com.frxhaikal_plg.ingrevia.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.frxhaikal_plg.ingrevia.R
import com.google.android.material.textfield.TextInputEditText

class CustomWeightEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = if (!isValidWeight(s)) {
                    context.getString(R.string.weight_must_be_valid)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isValidWeight(weight: CharSequence?): Boolean {
        if (weight.isNullOrEmpty()) return false
        return try {
            val weightValue = weight.toString().toInt()
            weightValue in 20..200
        } catch (e: NumberFormatException) {
            false
        }
    }
}
