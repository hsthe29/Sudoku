package com.example

import com.example.view.MainView
import game.TimeoutMainView
import javafx.scene.image.Image
import javafx.stage.Stage
import testkt.maintest
import tornadofx.App
import tornadofx.FX

class MyApp: App(MainView::class, Styles::class){
    override fun start(stage: Stage) {
        with(stage) {
            // fixed size
            minWidth = 1000.0
            minHeight = 650.0
            maxWidth = 1000.0
            maxHeight = 650.0
            super.start(this)
        }
        FX.primaryStage.icons += Image("drawable/sudokuicon.png")
    }
}