package testkt

import javafx.scene.Parent
import javafx.scene.control.Button
import tornadofx.View
import tornadofx.button
import tornadofx.hbox

class maintest : View("Closing Modals"){
    var ind = 0
    val bt = Array<Button>(4){Button().also { it.text = "${ind++}" }}
    override val root = hbox {
        bt.forEach { add(it.also { requestFocus() })}
        button {
            text = "ujdunvr"
            println(isFocused)
        }

    }
}