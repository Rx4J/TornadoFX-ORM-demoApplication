package com.lanik.kursach.view

import com.lanik.kursach.control.DBControl
import com.lanik.kursach.model.Shop
import com.lanik.kursach.model.ShopModel
import javafx.collections.FXCollections
import javafx.scene.control.Tab
import tornadofx.*
import javafx.scene.control.TabPane

/**
 * Class MasterView наследует класс View, что позволяет ему запускаться только один раз и всегда
 * иметь копию этого класса в памяти. Так же является главной формой через которую происходит
 * все взаимодействие с программой.
 * В самом классе MasterView реализован интерфейс, выводящий каждый объект Shop как вкладку, и в самой
 * вкладке выводится табличка позволяющая редактировать все Phone принадлежащих к данной копии Shop.
 * Так же реализован функционал добавления, удаления и обновления вкладок
 */
class MasterView : View("SQL Manager") {
    lateinit var tabPane: TabPane
    private val dbCtrl : DBControl by inject()
    private val shopes = FXCollections.observableArrayList<Shop>()
    // root фрагмент в которой происходит инициализация интерфейса
    override val root = borderpane() {
        minWidth = 1280.0
        minHeight = 720.0
        center {
            tabpane {
                tabPane = this
            }
        }
        top {
            menubar {
                menu("Action") {
                    item("New shop").action {
                        addShop()
                    }
                    item("OS Edit").action {
                        openInternalWindow(OSFragment())
                    }

                    item("Company Edit").action {
                        openInternalWindow(CompanyFragment())
                    }
                    item("Refresh").action {
                        onRefresh()
                    }
                }
            }
        }
    }

    /**
     * Процедура syncTab используется для синхронизации всех вкладок с экземплярома класса Shop
     */
    private fun syncTab() {
        tabPane.tabs.clear()
        shopes.forEach {
            val frag = PhoneFragment()
            frag.detachShop = it
            val tab = Tab(it.name)
            tab.content = frag.root
            tabPane.tabs.add(tab)
        }
    }

    /**
     * Процедура addShop используется для добавления записей в таблицу, при добавлении выводится форма-заполнения
     * в которой пользователь может задать все данные которые ему нужны.
     */
    private fun addShop() {
        val shop = ShopModel(Shop())
        dialog("Add new shop") {
            form {
                field("Name") { textfield(shop.name).required() }
                button("Add") {
                    action {
                        shop.commit {
                            runAsync {
                                dbCtrl.saveObject(shop.item)
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
     * Инициализатор класса, который сразу же после его создания обновляет таблицу
     */
    init {
        onRefresh()
    }

    /**
     * Перегрузка функции onRefresh, добавляющая к обновлению самой таблицы, синхранизацию данных из БД
     */
    override fun onRefresh() {
        shopes.asyncItems { dbCtrl.listShop() }
        syncTab()
    }
}


