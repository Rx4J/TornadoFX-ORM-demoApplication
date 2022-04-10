package com.lanik.kursach.model
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

/**
 * Class Phone, является entity объектом, на основе которого hibernate строит
 * таблицу базы данных автоматически. В основном используется как модель данных
 * @param productName - инициализатор для строкового свойства productNameProperty
 */
class Phone(productName: String? = null)
{
    // Создаем простое числовое свойства
    val idProperty = SimpleIntegerProperty()
    var id by idProperty
    var shop: Shop? = null
    val countProperty = SimpleIntegerProperty()
    var count by countProperty
    // Создаем сткровое числовое свойства
    val companyProperty = SimpleObjectProperty<Company>()
    var company by companyProperty
    val osProperty = SimpleObjectProperty<Os>()
    var os by osProperty
    val productNameProperty = SimpleStringProperty(productName)
    var productName: String by productNameProperty
    // Создаем числовое свойство с плавающей точкой
    val diagonalProperty = SimpleFloatProperty()
    var diagonal by diagonalProperty
    val priceProperty = SimpleFloatProperty()
    var price by priceProperty
}

/**
 * Class PhoneModel наследующий ItemViewModel с инициализатором Phone,
 * является интерпритацией модели данных Phone, но только специализированым
 * для framework TornadoFX
 * @param phone - является экземпляром класса Phone который затем разбирается на данные для PhoneModel
 */
class PhoneModel(phone: Phone? = null): ItemViewModel<Phone>(phone) {
    // Создаем элементы связывающие данные в интерфейсе и таблицу и SQL
    var id = bind(Phone::idProperty)
    var count = bind(Phone::countProperty)
    val company = bind(Phone::companyProperty)
    val os = bind(Phone::osProperty)
    val productName = bind(Phone::productNameProperty)
    val diagonal = bind(Phone::diagonalProperty)
    val price = bind(Phone::priceProperty)
}