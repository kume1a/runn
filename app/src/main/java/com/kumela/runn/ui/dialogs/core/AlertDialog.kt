package com.kumela.runn.ui.dialogs.core

import android.view.View
import android.widget.Button
import android.widget.TextView
import butterknife.BindView
import com.kumela.runn.R
import com.kumela.runn.core.base.BaseDialog

class AlertDialog : BaseDialog() {

    override val layoutRes: Int = R.layout.dialog_alert

    @BindView(R.id.text_title) lateinit var textTitle: TextView
    @BindView(R.id.text_message) lateinit var textMessage: TextView
    @BindView(R.id.button_positive) lateinit var buttonPositive: Button
    @BindView(R.id.button_negative) lateinit var buttonNegative: Button

    private var title: String = ""
    private var message: String = ""
    private var positiveButtonText: String = ""
    private var negativeButtonText: String = ""

    private var onPositiveButtonClick: (() -> Unit)? = null
    private var onNegativeButtonClick: (() -> Unit)? = null

    override fun onViewBound(view: View) {
        textTitle.text = title
        textMessage.text = message

        if (onPositiveButtonClick != null) {
            buttonPositive.text = positiveButtonText
            buttonPositive.setOnClickListener { onPositiveButtonClick!!.invoke() }
        } else {
            buttonPositive.visibility = View.GONE
        }

        if (onNegativeButtonClick != null) {
            buttonNegative.text = negativeButtonText
            buttonNegative.setOnClickListener { onNegativeButtonClick!!.invoke() }
        } else {
            buttonNegative.visibility = View.GONE
        }
    }

    class Builder {
        private var title: String = ""
        private var message: String = ""
        private var positiveButtonText: String = ""
        private var negativeButtonText: String = ""

        private var onPositiveButtonClick: (() -> Unit)? = null
        private var onNegativeButtonClick: (() -> Unit)? = null

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun message(message: String): Builder {
            this.message = message
            return this
        }

        fun setOnPositiveButtonClickListener(text: String, listener: () -> Unit): Builder {
            this.positiveButtonText = text
            this.onPositiveButtonClick = listener
            return this
        }

        fun setOnNegativeButtonClickListener(text: String, listener: () -> Unit): Builder {
            this.negativeButtonText = text
            this.onNegativeButtonClick = listener
            return this
        }

        fun build(): AlertDialog {
            val dialog = AlertDialog()

            dialog.title = title
            dialog.message = message
            dialog.positiveButtonText = positiveButtonText
            dialog.negativeButtonText = negativeButtonText
            dialog.onPositiveButtonClick = onPositiveButtonClick
            dialog.onNegativeButtonClick = onNegativeButtonClick

            return dialog
        }
    }
}