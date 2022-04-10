package com.lanik.kursach.control

import com.lanik.kursach.model.Company
import com.lanik.kursach.model.Os
import com.lanik.kursach.model.Phone
import com.lanik.kursach.model.Shop
import org.hibernate.Session
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import tornadofx.Controller
import tornadofx.asObservable
/**
 * Class PhoneControl наследует Controller, является JPA контроллером через который происходит все взаимодействие
 * с БД.
 */
class DBControl : Controller() {
    // Объект является формой регистрации сессии между БД и программой
    private val registry = StandardServiceRegistryBuilder().configure().build()
    // Объект является формой инициализацией сессии между БД и программой
    private val sessionFactory = MetadataSources(registry).buildMetadata().buildSessionFactory()

    /**
     * Процедура shutdown используется для остановки SQL сервера
     */
    fun shutdown() {
        sessionFactory.close()
    }

    /**
     * Функция listShop наследует сессию withTransaction и открывает I/O между БД и программой.
     * Далее происходит запрос всех объектов из БД, которые возвращаются в виде списка.
     * @return список объектов Shop
     */
    fun listShop() = withTransaction {
        createQuery("from Shop", Shop::class.java).list().asObservable()
    }

    /**
     * Функция listPhone наследует сессию withTransaction и открывает I/O между БД и программой.
     * Далее происходит запрос всех объектов из БД, которые возвращаются в виде списка.
     * @return список объектов Phone
     */
    fun listPhone() = withTransaction {
        createQuery("from Phone", Phone::class.java).list().asObservable()
    }

    /**
     * Функция listOS наследует сессию withTransaction и открывает I/O между БД и программой.
     * Далее происходит запрос всех объектов из БД, которые возвращаются в виде списка.
     * @return список объектов OS
     */
    fun listOS() = withTransaction {
        createQuery("from Os", Os::class.java).list().asObservable()
    }

    /**
     * Функция listCompany наследует сессию withTransaction и открывает I/O между БД и программой.
     * Далее происходит запрос всех объектов из БД, которые возвращаются в виде списка.
     * @return список объектов Company
     */
    fun listCompany() = withTransaction {
        createQuery("from Company", Company::class.java).list().asObservable()
    }

    /**
     * Функция saveObject наследует сессию withTransaction и открывает I/O между БД и программой.
     * Далее сохраняем экземпляр объекта в БД.
     * @param obj - это экземпляр объекта который мы хотим сохранить.
     */
    fun saveObject(obj: Any) = withTransaction {
        save(obj)
    }!!

    /**
     * Функция updateObject наследует сессию withTransaction и открывает I/O между БД и программой.
     * Далее обновляем экземпляр объекта в БД.
     * @param obj - это экземпляр объекта который требуется обновить.
     */
    fun updateObject(obj: Any) = withTransaction {
        update(obj)
    }

    /**
     * Функция deleteObject наследует сессию withTransaction и открывает I/O между БД и программой.
     * Далее удаляет экземпляр объекта Phone в БД.
     * @param obj - это экземпляр объекта который требуется удалить.
     */
    fun deleteObject(obj: Any) = withTransaction {
        delete(obj)
    }

    /**
     * Процедура-обобщение withTransaction создана для того чтобы вынести общую часть код из предыдущих функций.
     * Открываем сессию I/O между БД и программой, возвращаем сессию и если она больше не ипользуется, то закрываем её
     */
    private fun <T> withTransaction(fn: Session.() -> T): T {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        try {
            return fn(session)
        } finally {
            session.transaction.commit()
            session.close()
        }
    }
}