package com.example.lab4

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Spinner
import android.widget.Button
import android.content.Intent




class MainActivity : AppCompatActivity() {
    private lateinit var boardSizeSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boardSizeSpinner = findViewById(R.id.boardSizeSpinner)
        val startButton = findViewById<Button>(R.id.startGameButton)

        startButton.setOnClickListener {
            val selectedSize = boardSizeSpinner.selectedItem.toString().substring(0, 1).toInt()
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("BOARD_SIZE", selectedSize)
            startActivity(intent)
        }
    }
}
