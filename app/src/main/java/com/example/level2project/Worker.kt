package com.example.level2project

//Данный дата-класс имплементирует интерфейс Serializable для возможности передачи его объектов между activity
//Дата-классы нужны для простого объявления, они могут содержать методы
//Являются финальными по умолчанию, т.е. не могут иметь потомков
data class WorkerModel(
    val imageId: Int,
    val title: String,
    val detail: String,
    var selected: Boolean

) :
    java.io.Serializable

data class Workers(
    val workers: MutableList<WorkerModel>
)
