package com.example.level2project

import android.Manifest
import android.app.Notification.MessagingStyle.Message
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
import org.json.JSONException
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Paths

class MainActivity() : AppCompatActivity(), WorkerAdapter.Listener {

    private lateinit var binding: ActivityMainBinding
    private var addLauncher: ActivityResultLauncher<Intent>? = null
    private var editLauncher: ActivityResultLauncher<Intent>? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Присваивание значений из JSON - файла
        try {
            isStoragePermissionGranted()
//            val bufferedReader: BufferedReader =
//                File("file:///workers.json").bufferedReader()
//            val jsonString = bufferedReader.use { it.readText() }
            val jsonString = getJSONFromAssets()!!
            val workers = Gson().fromJson(jsonString, Workers::class.java)
            val adapter = WorkerAdapter(this, workers.workers, this)
            initRecyclerView(adapter)
            //Для получения данных из addActivity используем
            addLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        adapter.addWorkerToScreen(
                            it.data?.getSerializableExtra("worker") as WorkerModel
                        )
                    }
                }

            editLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        adapter.delWorkerFromScreen(it.data?.getSerializableExtra("delItem") as WorkerModel)
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

    //Надуваем выпадающее меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return true
    }

    //Здесь должны будут прописываться действия по нажатию на элементы меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    //Реализация данной функции происходит в main, т.к. данные хранятся именно здесь
    override fun onClick(worker: WorkerModel) {
        //EditActivity теперь запускается таким образом, т.к. ему надо обмениваться изменёнными объектами с main
        editLauncher?.launch(
            Intent(
                this@MainActivity,
                EditActivity::class.java
            ).apply {
                putExtra("item", worker)
            })
    }

    //Функция для инициализации JSON файла
    private fun getJSONFromAssets(): String? {
        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val workerJSONFile =
                assets.open("workers.json")
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

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("TAG", "Permission is granted")
                true
            } else {
                Log.e("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("TAG", "Permission is granted")
            true
        }
    }
}