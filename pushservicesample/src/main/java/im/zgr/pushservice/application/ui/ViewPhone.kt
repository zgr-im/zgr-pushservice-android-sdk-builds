package im.zgr.pushservice.application.ui

import android.content.Context
import android.util.AttributeSet
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

open class ViewPhone: ViewEdit {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        val mask = PredefinedSlots.RUS_PHONE_NUMBER
        MaskFormatWatcher(MaskImpl.createTerminated(mask)).installOn(input)
    }

    var text: String
        get() = input.text.toString().replace(Regex("[^\\d.]"), "")
        set(value) { input.setText(value) }

}