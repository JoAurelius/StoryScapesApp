package com.kampus.storyscapes.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.kampus.storyscapes.R

class TextInputCustomView  : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleRes: Int) : super(context, attrs, defStyleRes) {
        init()
    }

    private fun init () {
        background = ContextCompat.getDrawable(context, R.drawable.text_view_border)
    }

    fun getTextString(): String {
        return text.toString()
    }

    fun setTextString(text: String) {
        setText(text)
    }
}