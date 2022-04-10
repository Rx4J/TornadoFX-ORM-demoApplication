package com.lanik.kursach.model

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

/**
 * Class Company, является entity объектом, на основе которого hibernate строит
 * таблицу базы данных автоматически. В основном используется как модель данных
 * @param name - инициализатор для строкового свойства nameProperty
 */
class Company(name: String? = null) {
    // Создаем простое числовое свойства
    val idProperty = SimpleIntegerProperty()
    var id by idProperty
    // Создаем сткровое числовое свойства
    val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty
}

/**
 * Class CompanyModel наследующий ItemViewModel с инициализатором Company,
 * является интерпритацией модели данных Company, но только специализированым
 * для framework TornadoFX
 * @param company - является экземпляром класса Company который затем разбирается на данные для CompanyModel
 */
class CompanyModel(company: Company? = null): ItemViewModel<Company>(company) {
    // Создаем элементы связывающие данные в интерфейсе и таблицу и SQL
    var id = bind(Company::idProperty)
    val name = bind(Company::nameProperty)
}