package com.kumela.runn.ui.onboarding

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.constraintlayout.widget.Guideline
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import butterknife.BindView
import com.kumela.runn.R
import com.kumela.runn.core.Constants
import com.kumela.runn.core.base.BaseController
import com.kumela.runn.core.enums.Gender
import com.kumela.runn.ui.core.views.GenderChooser
import com.kumela.runn.ui.core.views.OnboardingIndicator

@SuppressLint("NonConstantResourceId")
class OnboardingController :
    BaseController<OnboardingContract.View, OnboardingContract.Presenter>(),
    OnboardingContract.View {

    override val layoutRes: Int
        get() = R.layout.screen_onboarding

    @BindView(R.id.text_gender_header) lateinit var textGenderHeader: TextView
    @BindView(R.id.text_gender_comment) lateinit var textGenderComment: TextView
    @BindView(R.id.text_male) lateinit var textMale: TextView
    @BindView(R.id.text_female) lateinit var textFemale: TextView
    @BindView(R.id.gender_chooser) lateinit var genderChooser: GenderChooser

    @BindView(R.id.text_weight_header) lateinit var textWeightHeader: TextView
    @BindView(R.id.number_picker_weight) lateinit var numberPickerWeight: NumberPicker
    @BindView(R.id.text_kg) lateinit var textKg: TextView

    @BindView(R.id.text_height_header) lateinit var textHeightHeader: TextView
    @BindView(R.id.number_picker_height) lateinit var numberPickerHeight: NumberPicker
    @BindView(R.id.text_cm) lateinit var textCm: TextView

    @BindView(R.id.page_indicator) lateinit var pageIndicator: OnboardingIndicator
    @BindView(R.id.guideline_next_back) lateinit var guidelineNextBack: Guideline
    @BindView(R.id.button_next) lateinit var buttonNext: Button
    @BindView(R.id.button_back) lateinit var buttonBack: Button

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        buttonBack.setOnClickListener { presenter.onBackClicked() }
        buttonNext.setOnClickListener { presenter.onNextClicked() }

        numberPickerWeight.minValue = Constants.MIN_WEIGHT
        numberPickerWeight.maxValue = Constants.MAX_WEIGHT

        numberPickerHeight.minValue = Constants.MIN_HEIGHT
        numberPickerHeight.maxValue = Constants.MAX_HEIGHT
    }

    override fun showGenderGroup() {
        animateRight(textGenderHeader, show = true)
        animateRight(textGenderComment, show = true)
        animateRight(textMale, show = true)
        animateRight(textFemale, show = true)
        animateRight(genderChooser, show = true)
    }

    override fun hideGenderGroup() {
        animateRight(textGenderHeader, show = false)
        animateRight(textGenderComment, show = false)
        animateRight(textMale, show = false)
        animateRight(textFemale, show = false)
        animateRight(genderChooser, show = false)
    }

    override var gender: Gender
        get() = genderChooser.gender
        set(value) {
            genderChooser.gender = value
        }

    override fun showWeightGroup() {
        animateRight(textWeightHeader, show = true)
        animateRight(numberPickerWeight, show = true)
        animateRight(textKg, show = true)
    }

    override fun hideWeightGroup() {
        animateRight(textWeightHeader, show = false)
        animateRight(numberPickerWeight, show = false)
        animateRight(textKg, show = false)
    }

    override var weight: Int
        get() = numberPickerWeight.value
        set(value) {
            numberPickerWeight.value = value
        }

    override fun showHeightGroup() {
        animateRight(textHeightHeader, show = true)
        animateRight(numberPickerHeight, show = true)
        animateRight(textCm, show = true)
    }

    override fun hideHeightGroup() {
        animateRight(textHeightHeader, show = false)
        animateRight(numberPickerHeight, show = false)
        animateRight(textCm, show = false)
    }

    override var height: Int
        get() = numberPickerHeight.value
        set(value) {
            numberPickerHeight.value = value
        }

    override var pageIndex: Int
        get() = pageIndicator.indicatorIndex
        set(value) {
            pageIndicator.indicatorIndex = value
        }

    override fun hideBackButton() {
        ValueAnimator.ofFloat(.5f, 0f).apply {
            duration = 200L
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                val fraction = valueAnimator.animatedFraction
                guidelineNextBack.setGuidelinePercent(value)
                buttonBack.alpha = 1f - fraction
            }
            doOnEnd {
                buttonBack.visibility = View.GONE
            }
            start()
        }
    }

    override fun showBackButton() {
        ValueAnimator.ofFloat(0f, .5f).apply {
            duration = 200L
            addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Float
                val fraction = valueAnimator.animatedFraction
                guidelineNextBack.setGuidelinePercent(value)
                buttonBack.alpha = fraction
            }
            doOnStart {
                buttonBack.visibility = View.VISIBLE
            }
            start()
        }
    }

    override fun changeNextButtonToFinish() {
        buttonNext.post { buttonNext.text = getString(R.string.finish) }
    }

    override fun changeFinishButtonToNext() {
        buttonNext.post { buttonNext.text = getString(R.string.next) }
    }

    private fun animateRight(view: View, show: Boolean) {
        val alpha = if (show) 1f else 0f
        val translationX = if (show) 0f else 250f

        val animation = view.animate()
            .alpha(alpha)
            .translationX(translationX)
            .setDuration(300L)

        if (show) {
            view.translationX = -250f
            animation.withStartAction { view.visibility = View.VISIBLE }
        } else {
            animation.withEndAction { view.visibility = View.GONE }
        }

        animation.start()
    }

    companion object {
        @JvmStatic
        fun newInstance(): OnboardingController {
            return OnboardingController()
        }
    }
}