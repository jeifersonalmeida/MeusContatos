package br.com.jeiferson.contatinhos.dao

interface DAO<T> {

    fun save(t: T)
    fun get(name: String): T
    fun getAll(): MutableList<T>
    fun update(t: T)
    fun delete(name: String)

}