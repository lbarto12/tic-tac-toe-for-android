package com.example.tttfirsttake

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)


        autoResetSwitch.isChecked = MainActivity.autoReset

        autoResetSwitch.setOnCheckedChangeListener { _, _ ->
            MainActivity.autoReset = autoResetSwitch.isChecked
        }

    }


}