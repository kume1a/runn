package com.kumela.dialogcontroller.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StyleRes
import com.kumela.dialogcontroller.DialogController
import com.kumela.dialogcontroller.R

class AlertDialog(@StyleRes themeId: Int = 0) : DialogController(themeId) {

    private var title: String = ""
    private var message: String = ""
    private var positiveButtonText: String = ""
    private var negativeButtonText: String = ""

    private var onPositiveButtonClick: (() -> Unit)? = null
    private var onNegativeButtonClick: (() -> Unit)? = null

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.alert_dialog, container, false)

        val textTitle = view.findViewById<TextView>(R.id.text_title)
        val textMessage = view.findViewById<TextView>(R.id.text_message)
        val buttonPositive = view.findViewById<Button>(R.id.button_positive)
        val buttonNegative = view.findViewById<Button>(R.id.button_negative)

        textTitle.text = title
        textMessage.text = message

        if(onPositiveButtonClick != null) {
            buttonPositive.text = positiveButtonText
            buttonPositive.setOnClickListener { onPositiveButtonClick!!.invoke() }
        } else {
            buttonPositive.visibility = View.GONE
        }

        if(onNegativeButtonClick != null) {
            buttonNegative.text = negativeButtonText
            buttonNegative.setOnClickListener { onNegativeButtonClick!!.invoke() }
        } else {
            buttonNegative.visibility = View.GONE
        }

        return view
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
            val dialog =  AlertDialog()

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