package com.example.level2project

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.level2project.databinding.ActivityAddBinding
import java.io.File
import java.io.FileInputStream

class AddActivity : AppCompatActivity() {
    //Companion object - это аналог модификатора static
    //В java static - это переменная, которая является общей для всех объектов класса,
    //т.е. при создании объектов, не содаются копии статических переменных (в отличие от обычных)
    //Статические переменные являются глобальными
    companion object {
    }

    lateinit var binding: ActivityAddBinding
    private var imageId = 0
    private val imageList = listOf(
        R.drawable.android,
        R.drawable.monkey_vah,
        R.drawable.ohota_krepkoe_enjoyer,
        R.drawable.oleg,
        R.drawable.blue_dog,
        R.drawable.pavel,
        R.drawable.short_dog
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.setImageResource(imageList[imageId])
        initButtons()
    }

    private fun initButtons() = with(binding){
        nextBtn.setOnClickListener{
            imageId++
            if (imageId > imageList.size - 1) {
                imageId = 0
            }
            imageView.setImageResource(imageList[imageId])
        }
        doneBtn.setOnClickListener{
            val worker = Worker (imageId, editTitle.text.toString(), editDetail.text.toString())
            //intent - это намерения. Способ передачи данных между activity
            val addIntent = Intent().apply{
                putExtra("worker", worker)
            }
            setResult(RESULT_OK, addIntent)
            finish()
        }
        cancelBtn.setOnClickListener{
            finish()
        }
    }
}