package com.example.level2project

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.level2project.databinding.CardViewBinding

//Адаптер служит для того, чтобы отрисовывать элементы Recycler View
//В самом классе прописывается логика одного элемента (примера),
// который в дальнейшем заполняется пользовательскими данными
class WorkerAdapter(val listener: Listener, val workerList: ArrayList<WorkerModel>) :
    RecyclerView.Adapter<WorkerAdapter.ViewHolder>() {

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

    fun addWorkerToScreen(worker: WorkerModel) {
        workerList.add(worker)
        //Уведомление об изменении данных для адаптера
        //Обязательно для всех, чтобы он обновлялся во сремя работы с приложением
        notifyDataSetChanged()
    }

    fun delWorkerFromScreen(worker: WorkerModel) {
        workerList.remove(worker)
        //Уведомление об изменении данных для адаптера
        //Обязательно для всех, чтобы он обновлялся во сремя работы с приложением
        notifyDataSetChanged()
    }

    interface Listener {
        fun onClick(worker: WorkerModel)
    }
}