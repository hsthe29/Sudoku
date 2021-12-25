package game

import javafx.beans.property.SimpleStringProperty

class hitpoint {
    var hp = SimpleStringProperty("")
    init {
        hp.value = "❤❤❤"
    }

    fun fault(){
        hp.value = hp.value.dropLast(1)
    }
}