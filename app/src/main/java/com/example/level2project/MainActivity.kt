package com.example.level2project

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.level2project.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset

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
        when (item.itemId) {
            R.id.by_title -> adapter.sortBySurname()
            R.id.delete -> delete()
        }
        return true
    }

    private fun delete() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Are you sure?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            adapter.deleteSelectedElements()
            showDeleteItemMenu(false)

        }
        alertDialog.setNegativeButton("No") { _, _ -> }
        alertDialog.show()
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
        val nullWorker = WorkerModel(0, "Error", "Error","Error","Error","Error", false)
        val temp: MutableList<WorkerModel> = MutableList(1) { nullWorker }
        val nullWorkerList = Workers(temp)
        return try {
            val bReader = getJSONFromAssets()
//            val bReader: BufferedReader = File("workers.json").bufferedReader()
            val workers = Gson().fromJson(bReader, Workers::class.java)
//            bReader.close()
            WorkerAdapter(this, workers.workers, this) { show -> showDeleteItemMenu(show) }
        } catch (e:Exception) {
            e.printStackTrace()
            WorkerAdapter(this, nullWorkerList.workers, this) { show -> showDeleteItemMenu(show) }
        }
    }

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