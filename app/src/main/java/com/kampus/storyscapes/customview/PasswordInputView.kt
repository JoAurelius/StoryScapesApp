package com.kampus.storyscapes.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.kampus.storyscapes.R

class CustomPasswordInputView : AppCompatEditText, View.OnTouchListener {

    private lateinit var hideButton: Drawable
    val errorObserver = MutableLiveData<Boolean>()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleRes
    ) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                errorObserver.value = !validatePassword(s.toString()).isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        hidePassword()
        background = ContextCompat.getDrawable(context, R.drawable.text_view_border)
    }

    private fun validatePassword(password: String): CharSequence? {
        error = if (password.length < 8 && password.isNotEmpty()) {
            "Password must be at least 8 characters"
        } else {
            null
        }
        return error
    }

    fun setError(errorMessage: String) {
        error = errorMessage
        val hasError = !error.isNullOrEmpty()
        errorObserver.value = hasError
    }


    private fun hidePassword() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        hideButton = ContextCompat.getDrawable(context, R.drawable.ic_password_visible) as Drawable
        hideButton.setBounds(100, 0, hideButton.intrinsicWidth - 100, hideButton.intrinsicHeight)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, hideButton, null)
    }

    private fun showPassword() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        hideButton = ContextCompat.getDrawable(context, R.drawable.ic_password_hidden) as Drawable
        hideButton.setBounds(100, 0, hideButton.intrinsicWidth - 100, hideButton.intrinsicHeight)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, hideButton, null)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.rawX >= (right - hideButton.bounds.width())) {
                    if (inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        hidePassword()
                    } else {
                        showPassword()
                    }
                    return true
                }
            }
        }
        return false
    }

    fun onTextChanged(onTextChanged: (CharSequence?) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                onTextChanged(s)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}

