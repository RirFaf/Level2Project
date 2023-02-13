package com.example.level2project

import java.text.FieldPosition

//Данный дата-класс имплементирует интерфейс Serializable для возможности передачи его объектов между activity
//Дата-классы нужны для простого объявления, они могут содержать методы
//Являются финальными по умолчанию, т.е. не могут иметь потомков
data class WorkerModel(
    val imageId: Int,
    val name: String,
    val patronymic: String,
    val surname: String,
    val position: String,
    val dept: String,
    var selected: Boolean

) :
    java.io.Serializable

data class Workers(
    val workers: MutableList<WorkerModel>
)
