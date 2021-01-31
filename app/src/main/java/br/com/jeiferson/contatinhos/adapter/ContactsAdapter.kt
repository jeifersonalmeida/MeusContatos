package br.com.jeiferson.contatinhos.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.jeiferson.contatinhos.R
import br.com.jeiferson.contatinhos.model.Contact

class ContactsAdapter(
    private val contactList: MutableList<Contact>,
    private val onContactClickListener: OnContactClickListener,
): RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(layoutContactView: View): RecyclerView.ViewHolder(layoutContactView),
        View.OnCreateContextMenuListener {
        val tvName: TextView = layoutContactView.findViewById(R.id.tvName)
        val tvPhoneNumber: TextView = layoutContactView.findViewById(R.id.tvPhoneNumber)
        val tvEmail: TextView = layoutContactView.findViewById(R.id.tvEmail)

        init {
            layoutContactView.setOnCreateContextMenuListener(this)
        }

        private val INVALID_POSITION = -1
        var index: Int = INVALID_POSITION

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.add("Editar")?.setOnMenuItemClickListener {
                if (index != INVALID_POSITION) {
                    onContactClickListener.onEditMenuItem(index)
                    true
                }
                false
            }
            menu?.add("Remover")?.setOnMenuItemClickListener {
                if (index != INVALID_POSITION) {
                    onContactClickListener.onRemoveMenuItem(index)
                    true
                }
                false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutContactView: View = LayoutInflater.from(parent.context).inflate(R.layout.contact_item_layout, parent, false)

        return ContactViewHolder(layoutContactView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]

        holder.tvName.text = contact.name
        holder.tvPhoneNumber.text = contact.phoneNumber
        holder.tvEmail.text = contact.email

        holder.itemView.setOnClickListener{
            onContactClickListener.onContact(position)
        }
        holder.index = position
    }

    override fun getItemCount(): Int = contactList.size

}