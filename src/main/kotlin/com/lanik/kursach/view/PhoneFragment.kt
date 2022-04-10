package com.lanik.kursach.view

import com.lanik.kursach.control.DBControl
import com.lanik.kursach.model.*
import javafx.beans.property.Property
import javafx.collections.FXCollections
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

/**
 * Class PhoneFragment наследует класс Fragment, что позволяет создовать несколько копий данного класса в памяти,
 * и во время исполнения программы, динамически по надобности выводить этот Fragment на экран
 * В самом классе PhoneFragment реализован интерфейс редактирование таблицы Phone который позволяет:
 * Удалять, добавлять, сортировать, редактировать поля и искать записи этой таблицы
 */
class PhoneFragment : Fragment() {
    var detachShop: Shop? = null
    private val dbCtrl : DBControl by inject()
    private val phones = FXCollections.observableArrayList<Phone>()
    private val selectedPhone : PhoneModel by inject()
    private var phonesView = SortedFilteredList(phones)
    // root фрагмент в которой происходит инициализация интерфейса
    override val root = borderpane {
        center {
            tableview(phonesView) {
                column("Id", Phone::idProperty)
                column("Comp", Phone::companyProperty).cellFormat { text = it.name }
                column("Name", Phone::productNameProperty).makeEditable()
                column("Os", Phone::osProperty).cellFormat { text = it.name }
                column("Diagonal", Phone::diagonalProperty).makeEditable()
                column("Price", Phone::priceProperty).makeEditable()
                column("Count", Phone::countProperty).makeEditable()
                onEditCommit { phone ->
                    runAsync { dbCtrl.updateObject(phone) } ui { onRefresh() }
                }
                smartResize()
                bindSelected(selectedPhone)
            }
        }
        bottom {
            hbox(10) {
                button("New entry") { action { addEntry() } }
                button("Delete entry") {
                    enableWhen(selectedPhone.empty.not())
                    action {
                        deleteEntry()
                    }
                }
                togglebutton("Search") {
                    isSelected = false
                    action { if(isSelected) searchEntry() else phonesView.predicate = {true} }
                }
                button("Refresh entry").action { onRefresh() }
            }
        }
    }

    /**
     * Процедура deleteEntry используется для удаления записей из таблицы, при удаление выводится окно потверждения.
     */
    private fun deleteEntry() {
        confirm("Confirm delete", "Do you want to delete ${selectedPhone.item.productName}?") {
            detachShop?.phones?.remove(selectedPhone.item)
            runAsync {
                dbCtrl.deleteObject(selectedPhone.item)
            } ui { onRefresh() }
        }
    }

    /**
     * Процедура addEntry используется для добавления записей в таблицу, при добавлении выводится форма-заполнения
     * в которой пользователь может задать все данные которые ему нужны.
     */
    private fun addEntry() {
        val phone = PhoneModel(Phone())
        val selectedOs: Property<Os> = objectProperty()
        val selectedCompany: Property<Company> = objectProperty()
        dialog("Add phone") {
            form {
                field("Name") { textfield(phone.productName).required() }
                field("Diagonal") { textfield(phone.diagonal) }
                field("Price") { textfield(phone.price) }
                field("Count") { textfield(phone.count) }
                combobox(selectedOs, dbCtrl.listOS()) {
                    cellFormat {
                        text = it.name
                    }
                }
                phone.item.os = selectedOs.value
                combobox(selectedCompany, dbCtrl.listCompany()) {
                    cellFormat {
                        text = it.name
                    }
                }
                phone.item.company = selectedCompany.value
                phone.item.shop = detachShop
                detachShop?.phones?.add(phone.item)
                button("Add") {
                    isDefaultButton = true
                    action {
                        phone.commit {
                            runAsync {
                                dbCtrl.saveObject(phone.item)
                                detachShop?.let { dbCtrl.updateObject(it) }
                            } ui {
                                onRefresh()
                                close()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Процедура searchEntry используется для поиска записей в таблице, при поиске выводится окно где
     * пользователь должен ввести имя искомого объекта
     */
    private fun searchEntry() {
       val ret = SimpleStringProperty()
        dialog("Search phone") {
            form {
                field("Search") { textfield(ret) }
                button("Search") {
                    action {
                        runAsync {
                            phonesView.predicate = {it.productName == ret.value && it.shop?.name == detachShop?.name}
                        } ui {
                            onRefresh()
                            close()
                        }
                    }
                }
            }
        }
    }

    /**
     * Инициализатор класса, который сразу же после его создания обновляет таблицу
     */
    init {
        onRefresh()
    }

    /**
     * Перегрузка функции onRefresh, добавляющая к обновлению самой таблицы, синхранизацию данных из БД
     */
    override fun onRefresh() {
        phones.asyncItems { dbCtrl.listPhone() }
        phonesView.predicate = { it.shop?.name == detachShop?.name }
    }
}
