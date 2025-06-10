package com.example.tarotadivination

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tarotadivination.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var usedWords: ArrayList<String> = arrayListOf()
    private var usedPositions: ArrayList<Int> = arrayListOf()
    private var binaryDestination: Array<String?> = arrayOfNulls(16)
    private var pressCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            tvWords.text = words[Random.nextInt(words.size - 1)]
            usedWords.add(tvWords.text.toString())

            btCero.setOnClickListener {
                updatePressCount()
                draw(false)
                trackBinaryDestination(false)
                updateWords()
            }

            btOne.setOnClickListener {
                updatePressCount()
                draw(true)
                trackBinaryDestination(true)
                updateWords()
            }

        }
    }

    private fun updatePressCount() {
        pressCount = Random.nextInt(1, 17)

        while (usedPositions.contains(pressCount)) {
            pressCount = Random.nextInt(1, 17)
        }

        usedPositions.add(pressCount)
    }

    fun draw(value: Boolean) {
//        Creación de una variable String con el id del elemento en la vista
        val ivTarget = "imageView$pressCount"
//        identificador de (elemento, tipo de identificador, nombre del paquete en el que está)
        val resId = resources.getIdentifier(ivTarget, "id", packageName)
//        Ahora ya se puede obtener un elemento image view (iv) de la vista con ese id pasado a valor numérico por
//        el identificador
        val iv = findViewById<ImageView>(resId)

        if (value) {
            iv.setImageResource(R.drawable.uno_removebg_preview)
            iv.contentDescription = "1"
        }
        else {
            iv.setImageResource(R.drawable.cerosindonfo_removebg_preview)
            iv.contentDescription = "0"
        }
    }

    fun trackBinaryDestination(value: Boolean) {
        if (value) binaryDestination[pressCount - 1] = "1"
        else binaryDestination[pressCount - 1] = "0"
    }

    fun updateWords() {
        val tvWord = findViewById<TextView>(R.id.tvWords)
        if (usedWords.size < 16) {
            var nextWord = words[Random.nextInt(words.size - 1)]

            while (usedWords.contains(nextWord)) {
                nextWord = words[Random.nextInt(words.size - 1)]
            }

            tvWord.text = nextWord
            usedWords.add(nextWord)
        } else {
            findViewById<Button>(R.id.btCero).isClickable = false
            findViewById<Button>(R.id.btOne).isClickable = false
            tvWord.text = "Decide tu Destino"
            tvWord.textSize = 34.0F
        }
    }

//    Evento relacionado al elemento image view presionado (ivPressed) utilizando la propiedad onClick dichos
//    elementos. Para poder asignar la función al elemento debe recibir la vista.
    fun onClickImageView(view: View) {
//        Comprobación para evitar que se pulse en las imágenes antes de completar el número
        if (!binaryDestination.contains(null)) {
            val ivPressed = findViewById<ImageView>(view.id)

//            Se utiliza la descripción del elemento como valor para determinar la imagen dentro del mismo
//            y el atributo tooltiptext para el orden numérico en el binario generado
            if (ivPressed.contentDescription.equals("1")) {
                ivPressed.setImageResource(R.drawable.cerosindonfo_removebg_preview)
                binaryDestination[ivPressed.tooltipText.toString().toInt() - 1] = "0"
            }
            else {
                ivPressed.setImageResource(R.drawable.uno_removebg_preview)
                binaryDestination[ivPressed.tooltipText.toString().toInt() - 1] = "1"
            }

//            Traducción del numero binario a un formato mejor para su visualización
            var data = ""
            var count = 0
            var end = 0
            for (i in binaryDestination) {
                data += i
                count++
                end++
                if (end == 8) {
                    count = 0
                } else if (count == 4 && end != 16) {
                    data+= "."
                    count = 0
                }
            }

            val myIntent = Intent(this@MainActivity, ResultsActivity::class.java)
            myIntent.putExtra("binary", data)

            this.startActivity(myIntent)
        }
    }
}