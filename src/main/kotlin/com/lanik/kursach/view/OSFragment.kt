package com.lanik.kursach.view

import com.lanik.kursach.control.DBControl
import com.lanik.kursach.model.Os
import com.lanik.kursach.model.OsModel
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*

/**
 * Class OSFragment наследует класс Fragment, что позволяет создовать несколько копий данного класса в памяти,
 * и во время исполнения программы, динамически по надобности выводить этот Fragment на экран
 * В самом классе OSFragment реализован интерфейс редактирование таблицы OS который позволяет:
 * Удалять, добавлять, сортировать, редактировать поля и искать записи этой таблицы
 */
class OSFragment : Fragment("List of OS") {
    private val dbCtrl : DBControl by inject()
    private val selectedOs : OsModel by inject()
    private var os = SortedFilteredList(FXCollections.observableArrayList<Os>())
    // root фрагмент в которой происходит инициализация интерфейса
    override val root = borderpane {
        center {
            tableview(os) {
                column("Id", Os::idProperty)
                column("Name", Os::nameProperty).makeEditable()
                onEditCommit {
                    runAsync { dbCtrl.updateObject(it) } ui { onRefresh() }
                }
                smartResize()
                bindSelected(selectedOs)
            }
        }
        bottom {
            hbox(10) {
                button("New entry") { action { addEntry() } }
                button("Delete entry") {
                    enableWhen(selectedOs.empty.not())
                    action {
                        deleteEntry()
                    }
                }
                togglebutton("Search") {
                    isSelected = false
                    action { if(isSelected) searchEntry() else os.predicate = {true} }
                }
                button("Refresh entry").action { onRefresh() }
            }
        }
    }

    /**
     * Процедура deleteEntry используется для удаления записей из таблицы, при удаление выводится окно потверждения.
     */
    private fun deleteEntry() {
        confirm("Confirm delete", "Do you want to delete ${selectedOs.item.name}?") {
            runAsync { dbCtrl.deleteObject(selectedOs.item) } ui { onRefresh() }
        }
    }

    /**
     * Процедура addEntry используется для добавления записей в таблицу, при добавлении выводится форма-заполнения
     * в которой пользователь может задать все данные которые ему нужны.
     */
    private fun addEntry() {
        val newOs = OsModel(Os())
        dialog("Add OS") {
            form {
                field("Name") { textfield(newOs.name).required() }
                button("Add") {
                    isDefaultButton = true
                    action {
                        newOs.commit {
                            runAsync {
                                dbCtrl.saveObject(newOs.item)
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
        dialog("Search OS") {
            form {
                field("Search") { textfield(ret) }
                button("Search") {
                    action {
                        runAsync {
                            os.predicate = {it.name == ret.value}
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
        os.asyncItems { dbCtrl.listOS() }
    }
}
