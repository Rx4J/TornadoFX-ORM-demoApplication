package com.lanik.kursach.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

/**
 * Class Os, является entity объектом, на основе которого hibernate строит
 * таблицу базы данных автоматически. В основном используется как модель данных
 * @param name - инициализатор для строкового свойства nameProperty
 */
class Os(name: String? = null) {
    // Создаем простое числовое свойства
    val idProperty = SimpleIntegerProperty()
    var id by idProperty
    // Создаем сткровое числовое свойства
    val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty
}

/**
 * Class OsModel наследующий ItemViewModel с инициализатором Os,
 * является интерпритацией модели данных Os, но только специализированым
 * для framework TornadoFX
 * @param os - является экземпляром класса Company который затем разбирается на данные для OsModel
 */
class OsModel(os: Os? = null): ItemViewModel<Os>(os) {
    // Создаем элементы связывающие данные в интерфейсе и таблицу и SQL
    var id = bind(Os::idProperty)
    val name = bind(Os::nameProperty)
}