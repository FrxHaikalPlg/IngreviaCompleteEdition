package com.frxhaikal_plg.ingrevia.ui.customview

import android.content.Context
import android.util.AttributeSet
import com.frxhaikal_plg.ingrevia.R
import com.google.android.material.textfield.TextInputEditText

class CustomAgeEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val age = s.toString().toInt()
                    when {
                        age < 13 -> error = context.getString(R.string.minimum_age_error)
                        age > 100 -> error = context.getString(R.string.maximum_age_error)
                        else -> error = null
                    }
                }
            }
        })
    }
}
