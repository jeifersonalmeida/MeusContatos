package br.com.jeiferson.contatinhos.service

import br.com.jeiferson.contatinhos.dao.ContactDAO
import br.com.jeiferson.contatinhos.model.Contact
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ContactFirebase: ContactDAO {

    private val CONTACTS_LIST_REALTIME_DATABASE = "contactsList"
    private val contactsListRtDb = Firebase.database.getReference(CONTACTS_LIST_REALTIME_DATABASE)
    private var contactList: MutableList<Contact> = mutableListOf()

    init {
        contactsListRtDb.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newContact: Contact = snapshot.getValue<Contact>() ?: Contact()

                if (contactList.indexOfFirst { it.name.equals(newContact.name) } == -1) {
                    contactList.add(newContact)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val editedContact: Contact = snapshot.getValue<Contact>() ?: Contact()

                val index = contactList.indexOfFirst { it.name.equals(editedContact.name) }
                contactList[index] = editedContact
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val deletedContact: Contact = snapshot.getValue<Contact>() ?: Contact()
                contactList.remove(deletedContact)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
    }

    override fun save(contact: Contact) = createOrUpdateContact(contact)

    override fun get(name: String): Contact = contactList[contactList.indexOfFirst { it.name == name }]

    override fun getAll(): MutableList<Contact> = contactList

    override fun update(contato: Contact) = createOrUpdateContact(contato)

    override fun delete(name: String) {
        contactsListRtDb.child(name).removeValue()
    }

    private fun createOrUpdateContact(contact: Contact) {
        contactsListRtDb.child(contact.name).setValue(contact)
    }

}