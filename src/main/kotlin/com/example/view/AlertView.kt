package com.example.view

import com.example.Styles
import game.Scorers
import javafx.geometry.Pos
import javafx.scene.control.TextField
import tornadofx.*

class AlertView : Fragment() {
    override val root = vbox {
        setPrefSize(300.0, 150.0)
        paddingTop = 10.0
        spacing = 15.0
        alignment = Pos.CENTER
        label(text = "CONGRATULATIONS"){
            addClass(Styles.normaltext)}
        label("Do you want to save your score?") { addClass(Styles.thintext) }
        hbox {
            alignment = Pos.CENTER
            spacing = 20.0
            button(text = "OK"){
                action {
                    find<Writer>().openWindow()
                    this@AlertView.close()
                }
            }
            button(text = "Close") { action { this@AlertView.close() } }
        }
    }
}

class Writer : Fragment() {
    private val score = Scorers
    private var name = ""
    private val tf = TextField()
    override val root = vbox {
        setPrefSize(250.0, 150.0)
        spacing = 5.0
        label(text = "Enter your name: ") {
            paddingLeft = 20.0
            addClass(Styles.normaltext)
        }
        hbox {
            alignment = Pos.CENTER
            add(tf.also {
                it.maxWidth = 250.0
                it.minWidth = 220.0
            })
        }
        hbox {
            paddingTop = 5.0
            alignment = Pos.CENTER
            spacing = 15.0
            button(text = "Save") {
                action {
                    name = tf.text
                    score.addToList(name)
                    this@Writer.close()
                }
            }
            button(text = "Cancel") { action { this@Writer.close() } }
        }
    }
}
