package game

import javafx.scene.control.Alert
import tornadofx.alert

class GameState {
    fun makeAlert(title : String) {
        alert(
            type = Alert.AlertType.INFORMATION,
            header = title
        )
    }
}
