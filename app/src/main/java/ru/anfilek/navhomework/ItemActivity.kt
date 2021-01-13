package ru.anfilek.navhomework

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class ItemActivity : AppCompatActivity() {

    private val userLogin: UserLogin by lazy { UserLogin(this) }
    private val EXTRA_MESSAGE = "EXTRA_MESSAGE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        findViewById<Button>(R.id.startAgainButton).setOnClickListener {
            startMeAgain()
        }

        findViewById<Button>(R.id.logout).setOnClickListener {
            logout()
        }
        renderItemId()

    }

    private fun renderItemId() {
        // get id from arguments and set it in the tvItemId

        val randomInt = intent.getIntExtra(EXTRA_MESSAGE, 0)

        findViewById<TextView>(R.id.tvItemId).apply {
            text = randomInt.toString()
        }

    }

    private fun startMeAgain() {
        // start the activity again.
        // For user it should look like activity is just updated
        // Do not forget to randomise new itemIt and put it as an argument.

        val randomInt: Int = Random.nextInt()
        val intent = Intent(this, ItemActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, randomInt)
        }
        startActivity(intent)
        finish()
    }

    private fun logout() {
        userLogin.setUserLoggedOut()

        // go to login screen
        // pay attention to backstack

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)

    }
}

