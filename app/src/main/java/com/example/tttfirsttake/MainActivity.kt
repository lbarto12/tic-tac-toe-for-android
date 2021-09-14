package com.example.tttfirsttake

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.security.acl.NotOwnerException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Tic Tac Toe")


        // LOAD FROM PREFERENCES
        try {
            val pref = getPreferences(MODE_PRIVATE)
            turn1 = pref.getBoolean("turn1", true)
            autoReset = pref.getBoolean("autoReset", false)
            val p1temp = pref.getString("p1", "")?.split(" ")
            val p2temp = pref.getString("p2", "")?.split(" ")


            if (p1temp != null && p2temp != null){
                for (i in p1temp.subList(0, p1temp.size - 1)) p1tiles.add(i.toInt())
                for (i in p2temp.subList(0, p2temp.size - 1)) p2tiles.add(i.toInt())
            }



        } catch (e: Exception){
            print(e)
        }

        updateFromRefresh()


        // SET WIDGET FUNCTION
        reset.setOnClickListener {
            reset()
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }


    private var p1tiles = ArrayList<Int>()
    private var p2tiles = ArrayList<Int>()
    private var turn1: Boolean = true


    companion object { var autoReset: Boolean = false }

    fun tilePressed(v: View) {
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

    private fun logic(c: Int, s: Button){

        if (c in p1tiles || c in p2tiles) {
            Toast.makeText(this, "Slot Occupied!", Toast.LENGTH_SHORT).show()
            return
        }

        val news: TextView = findViewById<TextView>(R.id.news)
        news.text = if (!turn1) "O's Turn" else "X's Turn"

        if (turn1) p1tiles.add(c) else p2tiles.add(c)
        s.text = if (turn1) "O" else "X"

        if (checkForWin(if (turn1) p1tiles else p2tiles)) {
            news.text = if (turn1) "O's win!" else "X's win!"
            showResetDialog(if (turn1) "O's win!" else "X's win!")
            return
        } else if (p1tiles.size + p2tiles.size == 9){
            news.text = "Tie Game!"
            showResetDialog("Tie Game!")
            return
        }
        turn1 = !turn1
    }

    // Just to make checking easier
    private class ArrayCheck(private var nums: ArrayList<Int>){
        fun all(vararg n: Int): Boolean{
            for (i in n){
                if (i !in nums) return false
            }
            return true
        }
    }

    private fun checkForWin(p: ArrayList<Int>): Boolean {

        val c = ArrayCheck(p)
        return  c.all(0, 1, 2) or
                c.all(3, 4, 5) or
                c.all(6, 7, 8) or
                c.all(0, 3, 6) or
                c.all(1, 4, 7) or
                c.all(2, 5, 8) or
                c.all(0, 4, 8) or
                c.all(2, 4, 6)
    }

    private fun showResetDialog(s: String){
        if (MainActivity.autoReset){
            reset()
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
            return
        }
        val d: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        d.setTitle(s)
        d.setMessage("Begin New Game?")
        d.setPositiveButton("yes") { _, _ -> reset()}
        d.setNegativeButton("no") {_, _ -> closeContextMenu()}
        val a: AlertDialog = d.create()
        a.setCanceledOnTouchOutside(false)
        a.show()

        // LOCK BUTTONS
        lockButtons(true)
    }

    private fun reset(){
        turn1 = true
        val news: TextView = findViewById<TextView>(R.id.news)
        news.text = "O's Turn"
        p1tiles.clear()
        p2tiles.clear()
        for (i in 0..8){
            val b: Button = findViewById<Button>(R.id.cell_1 + i)
            b.text = ""
        }
        lockButtons(false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updatePrefs()
    }

    override fun onPause() {
        super.onPause()
        updatePrefs()
    }

    private fun updatePrefs() {
        val pref = getPreferences(MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()

        var p1 = ""
        var p2 = ""

        for (i in p1tiles) p1 += "$i "
        for (i in p2tiles) p2 += "$i "

        editor.putBoolean("turn1", turn1)
        editor.putBoolean("autoReset", autoReset)
        editor.putString("p1", p1)
        editor.putString("p2", p2)

        editor.apply()
    }

    private fun updateFromRefresh(){
        for (i in 0..8){
            val b: Button = findViewById<Button>(R.id.cell_1 + i)
            if (i in p1tiles) b.text = "O"
            else if (i in p2tiles) b.text = "X"
        }
        val news: TextView = findViewById<TextView>(R.id.news)
        news.text = if (turn1) "O's Turn" else "X's Turn"

    }

    private fun lockButtons(bool: Boolean) {
        for (i in 0..8){
            val b: Button = findViewById<Button>(R.id.cell_1 + i)
            b.isEnabled = !bool
        }
    }

}