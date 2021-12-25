package game

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleIntegerProperty
import javafx.util.Duration
import tornadofx.Controller

class TimeCounter : Controller() {
    var timeScore = 0
    val timeCounter = SimpleIntegerProperty()
    private var timeline : Timeline? = null
    private var isStop = false

    fun startCounter(){
        stopCounter()
        timeline = Timeline(
            KeyFrame(
                Duration.seconds(TIMEOUT_CHECK_INTERVAL.toDouble()),
                {timeCounter.value += 1}
            )
        )
        timeline!!.cycleCount = Animation.INDEFINITE
        timeline!!.play()
    }
    fun stopCounter(){
        if(timeline != null ) {
            timeScore = timeCounter.value
            timeline!!.stop()
        }
        timeCounter.value = 0
    }

    fun pause() {
        if(timeline != null) {
            if(isStop)
                timeline!!.play()
            else {
                timeScore = timeCounter.value
                timeline!!.stop()
            }
            isStop = !isStop
        }
    }
}