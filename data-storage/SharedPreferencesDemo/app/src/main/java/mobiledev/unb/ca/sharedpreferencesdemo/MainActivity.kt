package mobiledev.unb.ca.sharedpreferencesdemo

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var highScoreTextView: TextView
    private lateinit var gameScoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initSharedPreferences()

        highScoreTextView = findViewById(R.id.highScoreText)
        setHighScoreText(readHighScoreFromSharedPreferences())

        //Game Score
        gameScoreTextView = findViewById(R.id.gameScoreText)
        setGameScoreText(INITIAL_HIGH_SCORE)

        val playButton: Button = findViewById(R.id.playButton)
        playButton.setOnClickListener {
            handlePlayButton()
        }

        // Reset Button
        val resetButton: Button = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            handleResetButton()
        }
    }

    private fun setHighScoreText(highScore: Int) {
        highScoreTextView.text = getString(R.string.high_score, highScore)
    }

    private fun setGameScoreText(gameScore: Int) {
        gameScoreTextView.text = gameScore.toString()
    }

    private fun handlePlayButton() {
        val randomScore = Random.nextInt(1000)
        setGameScoreText(randomScore)

        val currHighScore = readHighScoreFromSharedPreferences()
        if (randomScore > currHighScore) {
            writeHighScoreToSharedPreferences(randomScore)
            setHighScoreText(randomScore)
        }
    }

    private fun handleResetButton() {
        // Reset the high score
        writeHighScoreToSharedPreferences(INITIAL_HIGH_SCORE)
        setGameScoreText(INITIAL_HIGH_SCORE)
        setHighScoreText(INITIAL_HIGH_SCORE)
    }

    // Shared Preferences Helper Functions
    private fun initSharedPreferences() {
        SharedPreferencesManager.init(applicationContext)
    }

    private fun writeHighScoreToSharedPreferences(score: Int) {
        SharedPreferencesManager.saveIntValue(HIGH_SCORE_KEY, score)
    }

    private fun readHighScoreFromSharedPreferences(): Int {
        return SharedPreferencesManager.getIntValue(HIGH_SCORE_KEY, 0)
    }

    companion object {
        private const val HIGH_SCORE_KEY = "HIGH_SCORE_KEY"
        private const val INITIAL_HIGH_SCORE = 0
    }
}