package com.lanik.kursach.view

import com.lanik.kursach.control.DBControl
import com.lanik.kursach.model.*
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*

/**
 * Class CompanyFragment наследует класс Fragment, что позволяет создовать несколько копий данного класса в памяти,
 * и во время исполнения программы, динамически по надобности выводить этот Fragment на экран
 * В самом классе CompanyFragment реализован интерфейс редактирование таблицы Company который позволяет:
 * Удалять, добавлять, сортировать, редактировать поля и искать записи этой таблицы
 */
class CompanyFragment : Fragment("List of Company") {
    private val dbCtrl : DBControl by inject()
    private val selectedCompany : CompanyModel by inject()
    private var company = SortedFilteredList(FXCollections.observableArrayList<Company>())
    // root фрагмент в которой происходит инициализация интерфейса
    override val root = borderpane {
        center {
            tableview(company) {
                column("Id", Company::idProperty)
                column("Name", Company::nameProperty).makeEditable()
                onEditCommit {
                    runAsync { dbCtrl.updateObject(it) } ui { onRefresh() }
                }
                smartResize()
                bindSelected(selectedCompany)
            }
        }
        bottom {
            hbox(10) {
                button("New entry") { action { addEntry() } }
                button("Delete entry") {
                    enableWhen(selectedCompany.empty.not())
                    action {
                        deleteEntry()
                    }
                }
                togglebutton("Search") {
                    isSelected = false
                    action { if(isSelected) searchEntry() else company.predicate = {true} }
                }
                button("Refresh entry").action { onRefresh() }
            }
        }
    }

    /**
     * Процедура deleteEntry используется для удаления записей из таблицы, при удаление выводится окно потверждения.
     */
    private fun deleteEntry() {
        confirm("Confirm delete", "Do you want to delete ${selectedCompany.item.name}?") {
            runAsync { dbCtrl.deleteObject(selectedCompany.item) } ui { onRefresh() }
        }
    }

    /**
     * Процедура addEntry используется для добавления записей в таблицу, при добавлении выводится форма-заполнения
     * в которой пользователь может задать все данные которые ему нужны.
     */
    private fun addEntry() {
        val newCompany = CompanyModel(Company())
        dialog("Add company") {
            form {
                field("Name") { textfield(newCompany.name).required() }
                button("Add") {
                    isDefaultButton = true
                    action {
                        newCompany.commit {
                            runAsync {
                                dbCtrl.saveObject(newCompany.item)
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
        dialog("Search company") {
            form {
                field("Search") { textfield(ret) }
                button("Search") {
                    action {
                        runAsync {
                            company.predicate = {it.name == ret.value}
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
        company.asyncItems { dbCtrl.listCompany() }
    }
}
