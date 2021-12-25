package game

import com.example.Styles
import com.example.view.AlertView
import com.example.view.MainView
import tornadofx.addClass
import tornadofx.hide
import tornadofx.removeClass


class Sudoku {
    private var board = Array(10) { IntArray(10) }
    private val tboard = Array(10) { IntArray(10) {0} }
    private val checkRow = Array(10) { BooleanArray(10) }
    private val checkCol = Array(10) { BooleanArray(10) }
    private val checkSq = Array(4) { Array(4) { BooleanArray(10) } }
    private var flag : Boolean = true
    var fixedCoor = Array(9) {BooleanArray(9)}
    private var lstOfFixed = mutableSetOf<String>()
    var listOfFilled = mutableListOf<String>()
    private var filled = 40
    private var fault = 0
    var level = 40

    private fun prepare() {
        lstOfFixed.clear()
        listOfFilled.clear()
        tboard.forEach { it.fill(0) }
        checkRow.forEach { it.fill(true) }
        checkCol.forEach{ it.fill(true) }
        checkSq.forEach { it.forEach { it.fill(true) } }
        fixedCoor.forEach { it.fill(true) }
    }

    private fun typeShuffle() {
        val check = {a : List<Int>, b : List<Int>, c : List<Int> ->
            var flag = false
            for(i in 0 until 9)
                if(a[i] == b[i] || a[i] == c[i] || b[i] == c[i]) {
                    flag = true
                    break
                }
            flag
        }
        val r2 = (3..5).shuffled().first()
        val r3 = (6..8).shuffled().first()

        var lst1 : List<Int>
        var lst2 : List<Int>
        var lst3 : List<Int>

        do {
            lst1 = (1..9).shuffled()
            lst2 = (1..9).shuffled()
            lst3 = (1..9).shuffled()
        } while (check(lst1, lst2, lst3))

        for(i in 0 until 9) {
            tboard[0][i] = lst1[i]
            tboard[r2][i] = lst2[i]
            tboard[r3][i] = lst3[i]
            checkRow[0][lst1[i]] = false
            checkRow[r2][lst2[i]] = false
            checkRow[r3][lst3[i]] = false
            checkCol[i][lst1[i]] = false
            checkCol[i][lst2[i]] = false
            checkCol[i][lst3[i]] = false
            checkSq[0][i / 3][lst1[i]] = false
            checkSq[r2 / 3][i / 3][lst2[i]] = false
            checkSq[r3 / 3][i / 3][lst3[i]] = false
        }
        generateBoard(0, 0)
    }

    fun check(r: Int, c: Int, v: Int) =
        checkRow[r][v] && checkCol[c][v] && checkSq[r / 3][c / 3][v]

    private fun generateBoard(x : Int, y : Int) {
        if (flag) {
            if (tboard[x][y] != 0) {
                if(x == 8 && y == 8){
                    flag = false
                    board = tboard.map{ it.clone() }.toTypedArray()
                }
                else {
                    if (y < 8)
                        generateBoard(x, y + 1)
                    else generateBoard(x + 1, 0)
                }
                return
            }
            for(i in 1..9){
                if(check(x, y, i)){
                    checkRow[x][i] = false
                    checkCol[y][i] = false
                    checkSq[x / 3][y / 3][i] = false
                    tboard[x][y] = i
                    if (x == 8 && y == 8){
                        flag = false
                        board = tboard.map{ it.clone() }.toTypedArray()
                    }
                    else {
                        if (y < 8)
                            generateBoard(x, y + 1)
                        else generateBoard(x + 1, 0)
                    }
                    tboard[x][y] = 0
                    checkRow[x][i] = true
                    checkCol[y][i] = true
                    checkSq[x / 3][y / 3][i] = true
                }
            }
        }
    }

    fun printB(){
        for(i in 0 until 9){
            for(j in 0 until 9)
                print("${board[i][j]} ")
            println()
        }
    }

    private fun calFixed() {
        var x : Int
        var y : Int
        repeat(level) {
            do {
                x = (0..8).shuffled()[0]
                y = (0..8).shuffled()[0]
            } while(!fixedCoor[x][y])
            lstOfFixed.add("$x $y")
            listOfFilled.add("$x $y")
            fixedCoor[x][y] = false
        }
    }

    fun createGame(view: MainView){
        filled = level
        fault = 0
        flag = true
        prepare()
        typeShuffle()
        calFixed()
        for(i in lstOfFixed){
            val (x, y) = i.split(" ").map { it.toInt() }
            view.listOfButton[x][y].also {
                it.addClass(Styles.fixedbutton)
            }.text = "${board[x][y]}"
            tboard[x][y] = board[x][y]
        }
        printB()
    }

    fun putNumber(view : MainView, x : Int, y : Int, id : String) {
        tboard[x][y] = id.toInt()
        listOfFilled.add("$x $y")
        view.listOfButton[x][y].also {
            it.removeClass(Styles.incorrect)
            it.removeClass(Styles.correct)
            it.removeClass(Styles.guessbtn)
        }
        view.listOfButton[x][y].also {
            it.addClass(if (id.toInt() == board[x][y]) Styles.correct else Styles.incorrect)
            if (id.toInt() == board[x][y])
                filled += 1
            else {
                fault += 1
                view.hp.fault()
            }
        }.text = id
        if(filled == 81){
            view.timer.pause()
            view.pausebutton.hide()
            view.score.time = view.timer.timeScore
            view.find<AlertView>().openWindow()
        }
        if(fault == 3){
            view.timer.pause()
            view.pausebutton.hide()
            view.gamestate.makeAlert("YOU LOSE!")
        }
    }

    fun blur(view : MainView){
        for(i in listOfFilled) {
            val (x, y) = i.split(" ").map{it.toInt()}
            view.listOfButton[x][y].text = ""
        }
    }

    fun unblur(view: MainView){
        for(i in listOfFilled) {
            val (x, y) = i.split(" ").map{it.toInt()}
            view.listOfButton[x][y].text = "${tboard[x][y]}"
        }
    }

    fun isNotFixed(x : Int, y : Int) = "$x $y" !in lstOfFixed

    fun isLocked(x : Int, y : Int) = !fixedCoor[x][y]
}

