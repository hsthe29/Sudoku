package com.example


import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val line by cssclass()
        val label by cssclass()
        val correct by cssclass()
        val incorrect by cssclass()
        val textCss by cssclass()
        val timer by cssclass()
        val sep by cssclass()
        val number by cssclass()
        val btn by cssclass()
        val btncmd by cssclass()
        val guessbtn by cssclass()
        val normaltext by cssclass()
        val guess by cssclass()
        val fixedbutton by cssclass()
        val thintext by cssclass()
    }

    init {
        heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
        line {
            borderColor += box(Color.BLUE)
            padding = box(1.px)
        }
        label {
            fontSize = 30.px
            textFill = Color.RED
        }
        fixedbutton {
            fontSize = 25.px
            textFill = Color.BLACK
            fontWeight = FontWeight.BOLD
            borderColor += box(Color.GREEN)
            backgroundColor += c("#0088ff")
        }
        correct {
            fontSize = 25.px
            textFill = Color.BLACK
            fontWeight = FontWeight.BOLD
            borderColor += box(Color.BLACK)
            backgroundColor += c("#00d0ff")

        }
        incorrect {
            fontSize = 25.px
            textFill = Color.BLACK
            fontWeight = FontWeight.BOLD
            backgroundColor += Color.YELLOW
            borderColor += box(Color.BLACK)
        }
        textCss {
            fontSize = 25.px
            textFill = Color.BLACK
            fontWeight = FontWeight.BOLD
            fontFamily = "Comic Sans MS"
        }
        timer {
            fontSize = 25.px
            textFill = Color.BLACK
            fontWeight = FontWeight.BOLD
        }
        sep {
            borderColor += box(Color.WHITE)
            backgroundColor += c("#00d0ff")
        }
        number {
            fontSize = 15.px
            fontWeight = FontWeight.BOLD
            textFill = Color.BLACK
            //backgroundColor += c("#6d57fa")
        }
        btn {
            fontSize = 15.px
            fontFamily = "Palatino Linotype"
            fontWeight = FontWeight.BOLD
            backgroundColor += c("#80ff95")
            borderColor += box(Color.BLUE)
            borderWidth += box(2.px)
            borderRadius += box(10.0.px)
            backgroundRadius += box(10.0.px)
        }
        btncmd {
            fontSize = 15.px
            fontFamily = "Palatino Linotype"
            fontWeight = FontWeight.BOLD
        }
        guessbtn {
            backgroundColor += c("#737272")
        }
        guess {
            borderColor += box(Color.BLUE)
            padding = box(2.px)
            borderWidth += box(4.px)
        }
        normaltext {
            fontSize = 18.px
            textFill = Color.BLACK
            fontWeight = FontWeight.BOLD
        }
        thintext {
            fontSize = 18.px
            textFill = Color.BLACK
        }
    }
}