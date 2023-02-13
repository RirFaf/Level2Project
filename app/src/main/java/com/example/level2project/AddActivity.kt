package com.example.level2project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.level2project.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    //Companion object - это аналог модификатора static
    //В java static - это переменная, которая является общей для всех объектов класса,
    //т.е. при создании объектов, не содаются копии статических переменных (в отличие от обычных)
    //Статические переменные являются глобальными
    companion object {
    }

    private lateinit var binding: ActivityAddBinding
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

    private fun initButtons() = with(binding) {
        nextBtn.setOnClickListener {
            imageId++
            if (imageId > imageList.size - 1) {
                imageId = 0
            }
            imageView.setImageResource(imageList[imageId])
        }
        doneBtn.setOnClickListener {
            if(editName.text.toString().isNotBlank() &&
                editPatronymic.text.toString().isNotBlank() &&
                editSurname.text.toString().isNotBlank()&&
                editPosition.text.toString().isNotBlank()&&
                editDept.text.toString().isNotBlank()) {
                val worker = WorkerModel(
                    imageId,
                    editName.text.toString().lowercase().replaceFirstChar { it.uppercase() },
                    editPatronymic.text.toString().lowercase().replaceFirstChar { it.uppercase() },
                    editSurname.text.toString().lowercase().replaceFirstChar { it.uppercase() },
                    editPosition.text.toString().lowercase().replaceFirstChar { it.uppercase() },
                    editDept.text.toString(),
                    false
                )
                //intent - это намерения. Способ передачи данных между activity
                val addIntent = Intent().apply {
                    putExtra("worker", worker)
                }
                setResult(RESULT_OK, addIntent)
                finish()
            } else {
                Toast.makeText(this@AddActivity,"Заполните все поля", Toast.LENGTH_LONG).show()
            }
        }
        cancelBtn.setOnClickListener {
            finish()
        }
    }
}