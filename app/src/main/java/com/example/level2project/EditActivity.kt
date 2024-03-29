package com.example.level2project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.level2project.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    //Companion object - это аналог модификатора static
    //В java static - это переменная, которая является общей для всех объектов класса,
    //т.е. при создании объектов, не содаются копии статических переменных (в отличие от обычных)
    //Статические переменные являются глобальными
    companion object {
        fun makeIntent(context: Context, item: WorkerModel): Intent =
            Intent(context, EditActivity::class.java).putExtra("item", item)
    }

    private lateinit var binding: ActivityEditBinding
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
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val item = intent.getSerializableExtra("item") as WorkerModel
        initButtons(item)
        val nameBuilder = item.surname+" "+item.name[0].uppercase()+". "+item.patronymic[0].uppercase()+"."
        val posBuilder = item.dept+", "+item.position
        binding.apply {
            imageView.setImageResource(imageList[item.imageId])
            nameEV.setText(nameBuilder)
            positionEV.setText(posBuilder)
        }
    }

    private fun initButtons(item: WorkerModel) = with(binding) {
        deleteBtn.setOnClickListener {
            //intent - это намерения. Способ передачи данных между activity
            val addIntent = Intent().apply {
                putExtra("delItem", item)
            }
            setResult(RESULT_OK, addIntent)
            finish()
        }
        doneBtn.setOnClickListener {
            finish()
        }
    }
}