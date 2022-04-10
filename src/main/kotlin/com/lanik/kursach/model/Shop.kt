package com.lanik.kursach.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

/**
 * Class Shop, является entity объектом, на основе которого hibernate строит
 * таблицу базы данных автоматически. В основном используется как модель данных
 * @param name - инициализатор для строкового свойства nameProperty
 */
class Shop(name: String? = null) {
    // Создаем простое числовое свойства
    val idProperty = SimpleIntegerProperty()
    var id by idProperty
    // Создаем сткровое числовое свойства
    val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty
    var phones = mutableSetOf<Phone>()
}

/**
 * Class ShopModel наследующий ItemViewModel с инициализатором Shop,
 * является интерпритацией модели данных Shop, но только специализированым
 * для framework TornadoFX
 * @param shop - является экземпляром класса Shop который затем разбирается на данные для ShopModel
 */
class ShopModel(shop: Shop? = null): ItemViewModel<Shop>(shop) {
    // Создаем элементы связывающие данные в интерфейсе и таблицу и SQL
    var id = bind(Shop::idProperty)
    val name = bind(Shop::nameProperty)
}