package br.com.jeiferson.contatinhos.adapter

interface OnContactClickListener {

    fun onContact(position: Int)

    fun onEditMenuItem(position: Int)
    fun onRemoveMenuItem(position: Int)

}