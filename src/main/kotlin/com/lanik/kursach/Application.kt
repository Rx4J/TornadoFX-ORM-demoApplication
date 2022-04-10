package com.lanik.kursach

import com.lanik.kursach.control.DBControl
import tornadofx.App
import tornadofx.launch
import com.lanik.kursach.view.MasterView


/**
 * Класс DB наследующий MasterView. Является главным классом-управления,
 * используемым для того чтобы вызывать графический интерфейс.
 */
class DB: App(MasterView::class) {
    private val dbCtrl : DBControl by inject()
    override fun stop() {
        dbCtrl.shutdown()
        super.stop()
    }
}


/**
 * main функция в которой происходит инициализация интерфейска и
 * последующие подключение к БД
 */
fun main() {
    launch<DB>()
}