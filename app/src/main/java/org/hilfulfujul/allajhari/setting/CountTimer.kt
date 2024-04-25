package org.hilfulfujul.allajhari.setting

import android.os.CountDownTimer


object CountTimer {
    private var countDownTimer: CountDownTimer? = null
    private var onFinishListener: FinishedCount? = null
    var SET_TIMEOUT_ADS_ON_AFTER: Long = 60000

    fun startTimer(millisSecond: Long, listener: FinishedCount?) {
        onFinishListener = listener
        if (countDownTimer == null) {
            setCountDownTimer(millisSecond)
            countDownTimer!!.start()
        }
    }

    private fun setCountDownTimer(millisSecond: Long) {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        countDownTimer = object : CountDownTimer(millisSecond, 50) {
            override fun onTick(millisUntilFinished: Long) {
                // You can add code here for what should happen during each tick of the countdown
            }

            override fun onFinish() {
                if (onFinishListener != null) {
                    onFinishListener!!.onCountFinish()
                    countDownTimer = null
                }
            }
        }
    }

    interface FinishedCount {
        fun onCountFinish()
    }
}
