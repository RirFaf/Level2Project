package com.example.level2project

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.level2project.databinding.ActivityMainBinding

class MainActivity() : AppCompatActivity(), WorkerAdapter.Listener{
    private var adapter = WorkerAdapter(this)
    private lateinit var binding: ActivityMainBinding
    private var addLauncher: ActivityResultLauncher<Intent>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        //Для получения данных из addActivity используем
        addLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                adapter.addWorker(it.data?.getSerializableExtra("worker") as Worker)
            }
        }
    }

    //Инициализация recycler view
    private fun initRecyclerView() = with(binding) {
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
        addBtn.setOnClickListener {
            //Запуск других activity осущесвтляется через intent, где первый аргумент -
            //это класс ИЗ которого мы запускаем, а второй - это класс, который мы запускаем
            addLauncher?.launch(Intent(this@MainActivity, AddActivity::class.java))
        }
    }

    //Реализация данной функции происходит в main, т.к. данные хранятся именно здесь
    override fun onClick(worker: Worker) {
        startActivity(EditActivity.makeIntent(this@MainActivity, worker))
    }
}