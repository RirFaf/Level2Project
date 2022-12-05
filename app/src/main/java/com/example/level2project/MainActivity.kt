package com.example.level2project

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.level2project.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.json.JSONException
import java.io.IOException
import java.nio.charset.Charset

class MainActivity() : AppCompatActivity(), WorkerAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private var addLauncher: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Присваивание значений из JSON - файла
        try {
            val jsonString = getJSONFromAssets()!!
            val workers = Gson().fromJson(jsonString, Workers::class.java)
            val adapter = WorkerAdapter(this, workers.workers)
            initRecyclerView(adapter)
            //Для получения данных из addActivity используем
            addLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        adapter.addWorkerToScreen(it.data?.getSerializableExtra("worker") as WorkerModel)
                    }
                }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    //Инициализация recycler view
    private fun initRecyclerView(adapter: WorkerAdapter) = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter

        addBtn.setOnClickListener {
            //Запуск других activity осущесвтляется через intent, где первый аргумент -
            //это класс ИЗ которого мы запускаем, а второй - это класс, который мы запускаем
            addLauncher?.launch(Intent(this@MainActivity, AddActivity::class.java))
        }
    }

    //Реализация данной функции происходит в main, т.к. данные хранятся именно здесь
    override fun onClick(worker: WorkerModel) {
        startActivity(EditActivity.makeIntent(this@MainActivity, worker))
    }

    //Функция для инициализации JSON файла
    private fun getJSONFromAssets(): String? {
        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val workerJSONFile = assets.open("workers.json")
            val size = workerJSONFile.available()
            val buffer = ByteArray(size)
            workerJSONFile.read(buffer)
            workerJSONFile.close()
            json = String(buffer, charset)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return json
    }
}