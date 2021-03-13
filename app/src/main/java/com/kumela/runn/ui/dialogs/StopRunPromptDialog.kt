package com.kumela.runn.ui.dialogs

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import butterknife.BindView
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseDialog
import org.greenrobot.eventbus.EventBus

@SuppressLint("NonConstantResourceId")
class StopRunPromptDialog : BaseDialog() {

    enum class Event {
        CLOSE,
        FINISH,
        DISCARD_AND_EXIT
    }

    override val layoutRes: Int = R.layout.dialog_stop_run_prompt

    @BindView(R.id.button_close) lateinit var buttonClose: ImageButton
    @BindView(R.id.button_finish) lateinit var buttonFinish: Button
    @BindView(R.id.button_discard_and_exit) lateinit var buttonDiscardAndExit: Button

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        val bus = EventBus.getDefault()
        buttonClose.setOnClickListener { bus.post(Event.CLOSE) }
        buttonFinish.setOnClickListener { bus.post(Event.FINISH) }
        buttonDiscardAndExit.setOnClickListener { bus.post(Event.DISCARD_AND_EXIT) }
    }
}