package com.example.view

import com.example.Styles
import game.*
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import tornadofx.*

class MainView : View("Sudoku") {
    val listOfButton = Array(10) { Array(10) {Button().also{
        it.setMinSize(50.0, 50.0)
        it.setMaxSize(50.0, 50.0)
    } } }

    private val gridBox = Array(4) { Array(4) { GridPane().also {
        it.addClass(Styles.line)
    } } }

    private val numberPad = Array(9){Button().also {
        it.setMinSize(30.0, 30.0)
    }}

    private val xcoordinate = Array(9){ Label("${it + 1}").also { it.addClass(Styles.number) } }
    private val ycoordinate = Array(9){ Label("${it + 1}").also { it.addClass(Styles.number) } }

    private val valueList = listOf("Easy", "Medium", "Hard")
    private val selectedLevel = SimpleStringProperty("Easy")
    private val mapLevel = mapOf("Easy" to 40, "Medium" to 35, "Hard" to 30)

    private val lockButton = Button().also { it.hide()
                                    it.setPrefSize(60.0, 25.0)
                                    it.addClass(Styles.btn)
                                    it.action {lockButtonAction()}
    }
    val pausebutton = Button().also { it.hide()
                            it.action {
                                lockButton.hide()
                                Scoor.value = ""
                                timer.pause()
                                if(it.text == "Pause"){
                                    game.blur(this)
                                    it.text = "Continue"
                                    onGame = false
                                } else {
                                    game.unblur(this)
                                    it.text = "Pause"
                                    onGame = true
                                }
                                clearCss()
                            }
        it.setPrefSize(90.0, 20.0)
        it.addClass(Styles.btncmd)
    }
    private var scoreText = SimpleStringProperty("")
    private val Scoor = SimpleStringProperty("")
    val timer = TimeCounter()
    private var actionIndex = mutableListOf<Int>()
    private val game = Sudoku()
    val gamestate = GameState()
    val score = Scorers
    val hp = hitpoint()
    private var onGame = false

    init {
        selectedLevel.onChange {
            game.level = mapLevel[it]!!
        }
    }

    override val root = vbox (spacing = 20) {
        hbox {
            paddingTop = 10.0
            spacing = 20.0
            paddingLeft = 40.0
            button (text = "Start") {
                setPrefSize(60.0, 20.0)
                action {
                    scan()
                    lockButton.hide()
                    pausebutton.also { it.text = "Pause" }.show()
                    clearCss()
                    game.createGame(this@MainView)
                    timer.startCounter()
                    Scoor.value = ""
                    onGame = true
                }
                addClass(Styles.btncmd)
            }
            combobox(property = selectedLevel, values = valueList){
                addClass(Styles.btncmd)
                setPrefSize(120.0, 20.0)
            }
            add(pausebutton)
            button (text = "Exit") {
                action {
                    score.saveToFile()
                    actionIndex.clear()
                    timer.stopCounter()
                    this@MainView.close()
                }
                addClass(Styles.btncmd)
                setPrefSize(60.0, 20.0)
            }
        }
        separator{
            addClass(Styles.sep)
        }
        hbox {
            spacing = 20.0
            vbox {
                paddingTop = 50.0
                paddingLeft = 20.0
                spacing = 31.0
                for(i in 0..8){
                    add(ycoordinate[i])
                }
            }

            vbox {
                spacing = 10.0
                hbox {
                    paddingLeft = 22.0
                    spacing = 44.0
                    for(i in 0..8){
                        add(xcoordinate[i])
                    }
                }
                insetIntoBoard()
                gridpane {
                    hgap = 1.0
                    vgap = 1.0
                    addClass(Styles.line)

                    for(i in 0 until 3)
                        row {
                            for(j in 0 until 3)
                                add(gridBox[i][j])
                        }
                }
            }

            vbox {
                paddingLeft = 50.0
                spacing = 40.0
                hbox {
                    hbox {
                        label(text = "HP: ") {
                            addClass(Styles.textCss)
                        }
                        label(hp.hp) {
                            addClass(Styles.label)
                        }
                        minWidth = 150.0
                        maxWidth = 150.0
                    }
                    hbox {
                        alignment = Pos.TOP_CENTER
                        label(text = "   Time: ") {
                            addClass(Styles.textCss)
                        }
                        label(timer.timeCounter.asString()) {
                            addClass(Styles.timer)
                        }
                    }
                }
                hbox(alignment = Pos.CENTER) {
                    label("")
                    add(lockButton)
                    label(Scoor){
                        paddingLeft = 20.0
                        addClass(Styles.normaltext)
                    }
                }
                hbox {
                    spacing = 5.0
                    numberPad.forEachIndexed { index, it -> add(it.also {
                        it.text = "${index + 1}"
                        it.id = "${index + 1}"
                        it.setOnAction {
                            doEvent(it)
                        }
                        addClass(Styles.number)
                    }) }
                    addClass(Styles.line)
                }
                vbox {
                    spacing = 10.0
                    label("Top Scorers") { addClass(Styles.normaltext) }
                    scrollpane {
                        vbox {
                            label(scoreText) { addClass(Styles.thintext) }
                        }
                        prefHeight = 200.0
                    }
                }
            }
        }
    }

    private fun insetIntoBoard() {
        for(i in 0..8){
            for (j in 0..8){
                gridBox[i / 3][j / 3].vgap = 1.0
                gridBox[i / 3][j / 3].hgap = 1.0
                gridBox[i / 3][j / 3].addRow( i - i / 3 * 3, listOfButton[i][j]
                    .also {
                        it.id = "$i $j"
                        it.setOnAction {
                            actionPerformed(it)
                        }
                    })
            }
        }
    }

    private fun doEvent(e : ActionEvent){
        println((e.source as Button).id)
        val id = (e.source as Button).id
        if(actionIndex.isNotEmpty() && game.isNotFixed(actionIndex[0], actionIndex[1])
            && !game.isLocked(actionIndex[0], actionIndex[1])){
            game.putNumber(this, actionIndex[0], actionIndex[1], id)
            lockButton.show()
        }
    }

    private fun actionPerformed(e : ActionEvent){
        if(onGame) {
            clearCss()
            lockButton.hide()
            actionIndex = (e.source as Button).id.split(" ")
                .map { it.toInt() }.toMutableList()
            if (game.isNotFixed(actionIndex[0], actionIndex[1])) {
                listOfButton[actionIndex[0]][actionIndex[1]].addClass(Styles.guessbtn).addClass(Styles.guess)
                lockButton.text = if (game.isLocked(actionIndex[0], actionIndex[1]))
                    "Edit" else "Lock"
                if (listOfButton[actionIndex[0]][actionIndex[1]].text != "")
                    lockButton.show()
            } else {
                listOfButton[actionIndex[0]][actionIndex[1]].addClass(Styles.guess)
            }
            Scoor.value = "Current position: x: ${actionIndex[1] + 1}, y: ${actionIndex[0] + 1}"
        }
    }

    private fun scan() {
        scoreText.value = ""
        for(i in 0..8)
            for(j in 0..8)
                listOfButton[i][j].also {
                    it.removeClass(Styles.incorrect)
                    it.removeClass(Styles.correct)
                    it.removeClass(Styles.fixedbutton)
                }.text = ""
        for(i in score.lst)
            scoreText.value += (i + "\n")
        println(scoreText.value)
    }
    private fun clearCss(){
        if(actionIndex.isNotEmpty()) {
            listOfButton[actionIndex[0]][actionIndex[1]].removeClass(Styles.guessbtn).removeClass(Styles.guess)
            actionIndex.clear()
        }
    }
    private fun lockButtonAction() {
        val (x, y) = actionIndex
        if (lockButton.text == "Lock") {
            lockButton.text = "Edit"
            game.fixedCoor[x][y] = false
        } else {
            lockButton.text = "Lock"
            game.fixedCoor[x][y] = true
        }
    }
}





























