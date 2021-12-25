package game

import java.io.*
import kotlin.jvm.Throws

object Scorers{
    val lst = mutableListOf<String>()
    var time = 0
    private val f = File("src/main/resources/textdata/topscorers.txt")
    init {
        loadFile()
    }
    @Throws(IOException::class)
    private fun loadFile() {
        val br = BufferedReader(FileReader(f))
        var line = br.readLine()
        while (line != null){
            println(line)
            line = line.trim()
            lst.add(line)
            line = br.readLine()
        }
    }

    fun addToList(name : String) {
        lst.add("%-15s %6ds".format(name, time))
    }

    @Throws(IOException::class)
    fun saveToFile(){
        lst.sortByDescending { it }
        val bw = BufferedWriter(FileWriter(f))
        lst.forEach { bw.write("$it\n") }
        bw.close()
    }
}




