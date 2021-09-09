package com.example.tttfirsttake

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun tilePressed(v: View){
        val sel: Button = v as Button

        logic(when(sel.id){
            R.id.cell_1 -> 0
            R.id.cell_2 -> 1
            R.id.cell_3 -> 2
            R.id.cell_4 -> 3
            R.id.cell_5 -> 4
            R.id.cell_6 -> 5
            R.id.cell_7 -> 6
            R.id.cell_8 -> 7
            R.id.cell_9 -> 8
            else -> -1
        }, sel)

    }


    var p1tiles = ArrayList<Int>() // X, Y
    var p2tiles = ArrayList<Int>()

    var turn1 = true



    fun logic(c: Int, s: Button){

        if (c in p1tiles || c in p2tiles) {
            Toast.makeText(this, "Slot Occupied!", Toast.LENGTH_SHORT).show()
            return
        }

        val news: TextView = findViewById<TextView>(R.id.news)
        news.setText(if (!turn1) "O's Turn" else "X's Turn")

        if (turn1) p1tiles.add(c) else p2tiles.add(c)
        s.text = if (turn1) "O" else "X"

        if (checkForWin(if (turn1) p1tiles else p2tiles)) {
            news.setText(if (turn1) "O's win!" else "X's win!")
            showResetDialog()
        }

        turn1 = !turn1
    }

    fun checkForWin(p: ArrayList<Int>): Boolean {

        // I'm sure this garbage has a cleaner solution
        return (p.contains(0) && p.contains(1) && p.contains(2)) ||
                (p.contains(3) && p.contains(4) && p.contains(5)) ||
                (p.contains(6) && p.contains(7) && p.contains(8)) ||
                (p.contains(0) && p.contains(3) && p.contains(6)) ||
                (p.contains(1) && p.contains(4) && p.contains(7)) ||
                (p.contains(2) && p.contains(5) && p.contains(8)) ||
                (p.contains(0) && p.contains(4) && p.contains(8)) ||
                (p.contains(2) && p.contains(4) && p.contains(6))

    }

    fun showResetDialog(){
        val d: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        d.setTitle("Game Over!")
        d.setMessage("Begin New Game?")
        d.setPositiveButton("yes") { _, _ -> reset()}
        val a: AlertDialog = d.create()
        a.setCanceledOnTouchOutside(false)
        a.show()
    }

    fun reset(){
        turn1 = true
        val news: TextView = findViewById<TextView>(R.id.news)
        news.setText("O's Turn")
        p1tiles.clear()
        p2tiles.clear()
        for (i in 0..8){
            val b: Button = findViewById<Button>(R.id.cell_1 + i)
            b.text = ""
        }

    }

}