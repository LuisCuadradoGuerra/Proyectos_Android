package com.example.tarotadivination

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tarotadivination.databinding.ResultsBinding
import kotlin.random.Random

class ResultsActivity : AppCompatActivity() {
    private lateinit var binding: ResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ResultsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainResults)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            var data = intent.extras?.getString("binary").toString()
            tvBinary.text = data.substring(0 ,9) + "\n" + data.substring(9, 18)

//            Tratamiento del string para poder procesarlo como número
            data = data.replace(".", "")
            data = data.trimStart('0')
            if (data.isEmpty()) data = "0"

            val lucky = data.toInt(2).mod(5)
            tvMod.text = luckyText(lucky)

            tvLuck.text = luckyPoints(lucky)

            btReset.setOnClickListener {
                reset()
            }
        }
    }

    private fun reset() {
        val myIntent = Intent(this@ResultsActivity, MainActivity::class.java)
        this.startActivity(myIntent)
    }

    private fun luckyText(luck : Int):String {
        var result = ""
        when (luck) {
            0 -> result = "Bendecido por el\nDios Mákina"
            1 -> result = "¡Compila a la primera! WOW"
            2 -> result = "Ekilibrio alcanzado"
            3 -> result = "Pichi picha"
            4 -> result = "Corrompido por el Succ"
        }
        return result
    }

    private fun luckyPoints(luck : Int):String {
        var result = ""
        when (luck) {
            0 -> {
                result = excellentLuck[Random.nextInt(0, 5)]
            }
            1 -> {
                result = realNiceLuck[Random.nextInt(0, 5)]
            }
            2 -> {
                result = wellLuck[Random.nextInt(0, 5)]
            }
            3 -> {
                result = regularLuck[Random.nextInt(0, 5)]
            }
            4 -> {
                result = badLuck[Random.nextInt(0, 5)]
            }
        }
        return result
    }
}