package game

import javafx.animation.Animation.INDEFINITE
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleLongProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Priority
import javafx.util.Duration
import tornadofx.*
import java.util.*

const val TIMEOUT_CHECK_INTERVAL = 1L
const val TIMEOUTVAL = 15L

object TIMEOUT_EVENT : FXEvent()
object CLOSE_ALL_EVENT : FXEvent()
object START_TIMER_EVENT : FXEvent()
object HEARTBEAT_EVENT : FXEvent()

class TimeoutController : Controller() {

    val timeoutAccural = SimpleLongProperty()

    private var timeline : Timeline? = null

    init {
        subscribe<START_TIMER_EVENT> { startTimer() }
        subscribe<HEARTBEAT_EVENT> { resetTimer() }
    }

    private val checkForTimeout = EventHandler<ActionEvent> {

        timeoutAccural.value +=  TIMEOUT_CHECK_INTERVAL

        if( timeoutAccural >= TIMEOUTVAL ) {

            if(timeline != null )
                timeline!!.stop()

            fire(TIMEOUT_EVENT)
        }
    }

    private fun startTimer() {
        timeline = Timeline(
            KeyFrame(
                Duration.seconds(TIMEOUT_CHECK_INTERVAL.toDouble()),
                checkForTimeout
            )
        )
        timeline!!.cycleCount = INDEFINITE
        timeline!!.play()
    }

    private fun resetTimer() {
        timeoutAccural.value = 0L
    }
}

class TimeoutMainView : View("Closing Modals") {

    private val controller : TimeoutController by inject()

    override val root = vbox {
        vbox {
            label("main view")
            button("Start") {
                action {
                    find<FirstModal>().openModal()
                }
            }
            spacing = 2.0
            padding = Insets(10.0)
            vgrow = Priority.ALWAYS
        }
        separator()
        hbox {
            label("Timeout in:")
            label(SimpleLongProperty(TIMEOUTVAL).subtract(controller.timeoutAccural).asString())
            padding = Insets(10.0)
            spacing = 2.0
        }
        spacing = 2.0
        prefWidth = 667.0
        prefHeight = 376.0
    }

    override fun onDock() {
        fire(START_TIMER_EVENT)
        primaryStage.scene.onMouseMoved = EventHandler<MouseEvent> { fire(HEARTBEAT_EVENT) }
    }
}

abstract class CustomView(title : String) : View(title) {

    init {
        subscribe<TIMEOUT_EVENT> {handleClose() }
        subscribe<CLOSE_ALL_EVENT> { handleClose() }
    }

    private fun handleClose() {
        runLater {
            if (modalStage != null && modalStage!!.isShowing) {
                modalStage!!.close()
            }
        }
    }

    override fun onDock() {
        if( modalStage != null ) {
            modalStage!!.scene.onMouseMoved = EventHandler<MouseEvent> { fire(HEARTBEAT_EVENT) }
        }
    }
}

class FirstModal : CustomView("First Modal") {
    override val root = vbox {
        label("First Modal")
        button("Next") {
            action {
                find<SecondModal>().openModal()
            }
        }
        spacing = 2.0
        padding = Insets(10.0)

        prefWidth = 480.0
        prefHeight = 320.0
    }
}

class SecondModal : CustomView("Second Modal") {
    override val root = vbox {
        label("Second Modal")
        button("Next") {
            action {
                find<ThirdModal>().openModal()
            }
        }
        spacing = 2.0
        padding = Insets(10.0)

        prefWidth = 480.0
        prefHeight = 320.0
    }
}

class ThirdModal : CustomView("Third Modal") {
    override val root = vbox {
        label("Third Modal")
        button("Finish") {
            action {
                fire(CLOSE_ALL_EVENT)
            }
        }
        spacing = 2.0
        padding = Insets(10.0)

        prefWidth = 480.0
        prefHeight = 320.0
    }
}