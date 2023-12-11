package im.zgr.pushservice.application.ui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import im.zgr.pushservice.application.R
import im.zgr.pushservice.application.databinding.ViewEditBinding

open class ViewEdit: RelativeLayout {

    private val binding = ViewEditBinding
        .inflate(LayoutInflater.from(context), this, true)

    val frame: TextInputLayout get() = binding.frame
    val input: TextInputEditText get() = binding.input

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) { setAttrs(attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) { setAttrs(attrs) }

    private fun setAttrs(attrs: AttributeSet? = null) {
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.ViewEdit).apply {
                setHint(this)
                recycle ()
            }
        }
    }

    private fun setHint(array: TypedArray) {
        val attr: Int = R.styleable.ViewEdit_hint
        frame.hint = array.getString(attr)
    }

}