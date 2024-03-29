package com.example.level2project

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.level2project.databinding.CardViewBinding

//Адаптер служит для того, чтобы отрисовывать элементы Recycler View
//В самом классе прописывается логика одного элемента (примера),
// который в дальнейшем заполняется пользовательскими данными
class WorkerAdapter(
    private val listener: Listener,
    private val workerList: MutableList<WorkerModel>,
    private val context: Context,
    private val showDeleteMenu: (Boolean) -> Unit
) : RecyclerView.Adapter<WorkerAdapter.ViewHolder>(), java.io.Serializable {
    private var isEnabled = false
    private val selectedItemsList = mutableListOf<Int>()
//    @RequiresApi(Build.VERSION_CODES.N)
//    private val path = "${context.cacheDir}/workers.json"

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = CardViewBinding.bind(item)
    }

    private fun selectItem(holder: ViewHolder, item: WorkerModel, position: Int) {
        isEnabled = true
        selectedItemsList.add(position)
        item.selected = true
        holder.binding.cardView.setBackgroundColor(Color.parseColor("#676767"))
        showDeleteMenu(true)
        Log.d("LongClick", "TrueFun")
    }

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
        val worker = workerList[position]
        val nameBuilder = worker.surname+" "+worker.name[0].uppercase()+". "+worker.patronymic[0].uppercase()+"."
        val posBuilder = worker.dept+", "+worker.position
        val imageList = listOf(
            R.drawable.android,
            R.drawable.monkey_vah,
            R.drawable.ohota_krepkoe_enjoyer,
            R.drawable.oleg,
            R.drawable.blue_dog,
            R.drawable.pavel,
            R.drawable.short_dog
        )
        //чтобы не прописывать binding перед каждой строкой чожно использовать with(binding)
        //по сути эта конструкция указывает класс, из которого нужно брать элементы
        //Так же можно использовать следующую конструкцию
        holder.binding.apply {
            imageView.setImageResource(imageList[worker.imageId])
            nameTV.text = nameBuilder
            positionTV.text = posBuilder
            cardView.setBackgroundColor(Color.parseColor("#9E9E9E"))
            cardView.setOnClickListener {
                if (selectedItemsList.contains(position)) {
                    selectedItemsList.removeAt(position)
                    cardView.setBackgroundColor(Color.parseColor("#9E9E9E"))
                    worker.selected = false
                    if (selectedItemsList.isEmpty()) {
                        showDeleteMenu(false)
                        isEnabled = false
                    }
                } else if (isEnabled) {
                    selectItem(holder, worker, position)
                } else {
                    listener.onClick(worker)
                    cardView.setBackgroundColor(Color.parseColor("#9E9E9E"))
                }
            }
            cardView.setOnLongClickListener {
                selectItem(holder, worker, position)
                true
            }
        }
    }

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

    fun sortBySurname() {
        workerList.sortBy { it.surname}
        notifyDataSetChanged()
    }

    fun sortByDept() {
        workerList.sortBy { it.dept}
        notifyDataSetChanged()
    }

    fun deleteSelectedElements() {
        if (selectedItemsList.isNotEmpty()) {
            workerList.removeAll { worker -> worker.selected }
            isEnabled = false
            selectedItemsList.clear()
        }
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