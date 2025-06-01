package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout

class GameActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var timerText: TextView
    private lateinit var buttonBackToMenu: Button
    private lateinit var winnerText: TextView

    private var boardSize = 3
    private lateinit var board: Array<Array<Button>>
    private var isPlayerOneTurn = true
    private var moveTimer: CountDownTimer? = null
    private val MOVE_TIME = 10_000L
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        boardSize = intent.getIntExtra("BOARD_SIZE", 3)
        gridLayout = findViewById(R.id.boardGrid)
        timerText = findViewById(R.id.timerText)
        buttonBackToMenu = findViewById(R.id.buttonBackToMenu)
        winnerText = findViewById(R.id.winnerText)

        gridLayout.columnCount = boardSize
        gridLayout.rowCount = boardSize

        generateBoard()
        startTimer()

        buttonBackToMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun generateBoard() {
        board = Array(boardSize) { row ->
            Array(boardSize) { col ->
                val button = Button(this).apply {
                    layoutParams = ViewGroup.LayoutParams(160, 160)
                    textSize = 24f
                    setOnClickListener { onCellClicked(this, row, col) }
                }
                gridLayout.addView(button)
                button
            }
        }
    }

    private fun onCellClicked(button: Button, row: Int, col: Int) {
        if (button.text.isNotEmpty() || gameOver) return

        button.text = if (isPlayerOneTurn) "X" else "O"

        if (checkWin(row, col)) {
            gameOver = true
            moveTimer?.cancel()
            showWinOnBoard(if (isPlayerOneTurn) "X" else "O")

            winnerText.text = "Переміг гравець ${if (isPlayerOneTurn) "X" else "O"}"
            winnerText.visibility = TextView.VISIBLE

            Toast.makeText(this, "Гравець ${if (isPlayerOneTurn) "1 (X)" else "2 (O)"} переміг!", Toast.LENGTH_LONG).show()
            buttonBackToMenu.visibility = Button.VISIBLE
            return
        }

        isPlayerOneTurn = !isPlayerOneTurn
        moveTimer?.cancel()
        startTimer()
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        val symbol = board[row][col].text.toString()
        if (symbol.isEmpty()) return false

        fun countInDirection(dx: Int, dy: Int): Int {
            var count = 1  // рахуємо поточну клітинку

            // Рахуємо в позитивному напрямку
            var x = row + dx
            var y = col + dy
            while (x in 0 until boardSize && y in 0 until boardSize && board[x][y].text == symbol) {
                count++
                x += dx
                y += dy
            }

            // Рахуємо в негативному напрямку
            x = row - dx
            y = col - dy
            while (x in 0 until boardSize && y in 0 until boardSize && board[x][y].text == symbol) {
                count++
                x -= dx
                y -= dy
            }

            return count
        }

        // Перевіряємо всі 4 напрямки
        return (countInDirection(1, 0) >= boardSize) ||  // вертикаль
                (countInDirection(0, 1) >= boardSize) ||  // горизонталь
                (countInDirection(1, 1) >= boardSize) ||  // головна діагональ
                (countInDirection(1, -1) >= boardSize)    // побічна діагональ
    }

    private fun showWinOnBoard(winnerSymbol: String) {
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                board[row][col].text = "Переміг $winnerSymbol"
                board[row][col].isEnabled = false
            }
        }
    }

    private fun startTimer() {
        if (gameOver) {
            timerText.text = "Гра завершена"
            return
        }
        moveTimer = object : CountDownTimer(MOVE_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Час: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                Toast.makeText(this@GameActivity, "Час вичерпано! Хід передано.", Toast.LENGTH_SHORT).show()
                isPlayerOneTurn = !isPlayerOneTurn
                startTimer()
            }
        }.start()
    }
}
