package com.example.level2project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.level2project.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.IOException

class MainActivity() : AppCompatActivity(), WorkerAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private var addLauncher: ActivityResultLauncher<Intent>? = null
    private var editLauncher: ActivityResultLauncher<Intent>? = null
    private val adapter = getAdapter()
    private var sortMenu: Menu? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Присваивание значений из JSON - файла
        isStoragePermissionGranted()
//            val bufferedReader: BufferedReader =
//                File("file:///workers.json").bufferedReader()
//            val jsonString = bufferedReader.use { it.readText() }
//            val jsonString = getJSONFromAssets()!!
//            val workers = Gson().fromJson(jsonString, Workers::class.java)
//            val adapter = WorkerAdapter(this, workers.workers, this)
        initRecyclerView(adapter)

        //Для получения данных из addActivity используем
        addLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                adapter.addWorkerToScreen(
                    it.data?.getSerializableExtra("worker") as WorkerModel
                )
            }
        }

        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                adapter.delWorkerFromScreen(it.data?.getSerializableExtra("delItem") as WorkerModel)
            }
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

    //Надуваем выпадающее меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        sortMenu = menu
        menuInflater.inflate(R.menu.sort_menu, menu)
        showDeleteItemMenu(false)
        return true
    }

    private fun showDeleteItemMenu(show: Boolean) {
        sortMenu?.findItem(R.id.delete)?.isVisible = show
    }

    //Здесь должны будут прописываться действия по нажатию на элементы меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        binding.recyclerView.adapter = WorkerAdapter
        when (item.itemId) {
            R.id.by_title -> adapter.sortByTitle()
            R.id.delete -> deleteSelectedItems()
        }
        return true
    }

    private fun deleteSelectedItems() {

    }

    //Реализация данной функции происходит в main, т.к. данные хранятся именно здесь
    override fun onClick(worker: WorkerModel) {
        //EditActivity теперь запускается таким образом, т.к. ему надо обмениваться изменёнными объектами с main
        editLauncher?.launch(Intent(
            this@MainActivity, EditActivity::class.java
        ).apply {
            putExtra("item", worker)
        })
    }

    //Функция для инициализации JSON файла
    private fun getAdapter(): WorkerAdapter {
        val nullWorker = WorkerModel(0, "0", "0", false)
        val temp: MutableList<WorkerModel> = MutableList(1) { nullWorker }
        val nullWorkerList = Workers(temp)
        return try {
            val bReader: BufferedReader = File("workers.json").bufferedReader()
            val workers = Gson().fromJson(bReader.readText(), Workers::class.java)
            WorkerAdapter(this, workers.workers, this) { show -> showDeleteItemMenu(show) }
        } catch (e: IOException) {
            e.printStackTrace()
            WorkerAdapter(this, nullWorkerList.workers, this) { show -> showDeleteItemMenu(show) }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "Permission is granted")
                true
            } else {
                Log.e("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("TAG", "Permission is granted")
            true
        }
    }
}