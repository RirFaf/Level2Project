package com.example.level2project

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.level2project.databinding.CardViewBinding

//Адаптер служит для того, чтобы отрисовывать элементы Recycler View
//В самом классе прописывается логика одного элемента (примера),
// который в дальнейшем заполняется пользовательскими данными
class WorkerAdapter(
    val listener: Listener,
    val workerList: MutableList<WorkerModel>,
    context: Context
) :
    RecyclerView.Adapter<WorkerAdapter.ViewHolder>(), java.io.Serializable {

//    @RequiresApi(Build.VERSION_CODES.N)
//    private val path = "${context.cacheDir}/workers.json"

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = CardViewBinding.bind(item)

        //чтобы не прописывать binding перед каждой строкой чожно использовать with(binding)
        //по сути этот оператор указывает класс, из которого нужно брать элементы
        fun bind(worker: WorkerModel, listener: Listener) = with(binding) {
            val imageList = listOf(
                R.drawable.android,
                R.drawable.monkey_vah,
                R.drawable.ohota_krepkoe_enjoyer,
                R.drawable.oleg,
                R.drawable.blue_dog,
                R.drawable.pavel,
                R.drawable.short_dog
            )
            imageView.setImageResource(imageList[worker.imageId])
            titleView.text = worker.title
            detailView.text = worker.detail
            cardView.setOnClickListener {
                listener.onClick(worker)
            }
        }
    }

//    private val workerList = ArrayList<WorkerModel>()

    //Создание cardView и его передача
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return ViewHolder(view)
    }

    //Счётчик айтемов для того, чтобы класс знал, сколько раз запуститься, чтобы заполнить все элементы
    override fun getItemCount(): Int {
        return workerList.size
    }

    //Заполняет cardView данными
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workerList[position], listener)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addWorkerToScreen(worker: WorkerModel) {
        workerList.add(worker)
//        addDataToCacheFile(workerList)
        //Уведомление об изменении данных для адаптера
        //Обязательно для всех, чтобы он обновлялся во сремя работы с приложением
        notifyDataSetChanged()
    }

    fun delWorkerFromScreen(worker: WorkerModel) {
        workerList.remove(worker)
        notifyDataSetChanged()
    }

    fun sortByTitle() {
        workerList.sortBy { it.title }
        notifyDataSetChanged()
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun addDataToCacheFile(list: MutableList<WorkerModel>) {
//        val res = Gson().toJson(list)
////        val file=File("workers.json")
////        file.writeText(res)
//        if (!File(path).exists()) {
//            File(path).createNewFile()
//        }
//        val outputStream = File(path).outputStream()
//        try {
//            outputStream.bufferedWriter().use {
//                it.write("workers: $res}")
//                Log.d("MyTag", path)
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        } finally {
//            outputStream.close()
//        }
//        var json: String? = null
//        val charset: Charset = Charsets.UTF_8
//        val inputStream = File(path).inputStream()
//        val size = inputStream.available()
//        val buffer = ByteArray(size)
//        inputStream.read(buffer)
//        inputStream.close()
//        json = String(buffer, charset)
//        try {
//            Log.d("TagMy", path)
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        } finally {
//            inputStream.close()
//        }
//    }

    interface Listener {
        fun onClick(worker: WorkerModel)
    }
}