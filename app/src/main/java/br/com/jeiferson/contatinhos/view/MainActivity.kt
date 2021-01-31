package br.com.jeiferson.contatinhos.view

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.jeiferson.contatinhos.R
import br.com.jeiferson.contatinhos.adapter.ContactsAdapter
import br.com.jeiferson.contatinhos.adapter.OnContactClickListener
import br.com.jeiferson.contatinhos.dao.ContactDAO
import br.com.jeiferson.contatinhos.databinding.ActivityMainBinding
import br.com.jeiferson.contatinhos.model.Contact
import br.com.jeiferson.contatinhos.service.ContactFirebase
import br.com.jeiferson.contatinhos.view.MainActivity.Extras.EXTRA_CONTACT
import br.com.jeiferson.contatinhos.view.MainActivity.Extras.VIEW_CONTACT_ACTION
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), OnContactClickListener {

    private lateinit var activityMainBinding: ActivityMainBinding

    private lateinit var contactDAO: ContactDAO
    private lateinit var contactList: MutableList<Contact>

    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var contactsLayoutManager: LinearLayoutManager

    private val NEW_CONTACT_REQUEST_CODE = 0
    private val EDIT_CONTACT_REQUEST_CODE = 1
    object Extras {
        val EXTRA_CONTACT = "EXTRA_CONTACT"
        val VIEW_CONTACT_ACTION = "VIEW_CONTACT_ACTION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        contactDAO = ContactFirebase()

        contactList = mutableListOf()

        contactsLayoutManager = LinearLayoutManager(this)

        contactsAdapter = ContactsAdapter(contactList, this)

        activityMainBinding.rvContacts.adapter = contactsAdapter
        activityMainBinding.rvContacts.layoutManager = contactsLayoutManager

        GetContactsTask(this).execute()
    }

    private class GetContactsTask(context: MainActivity): AsyncTask<Void, Void, List<Contact>>() {

        private val activityReference: WeakReference<MainActivity> = WeakReference(context)

        override fun onPreExecute() {
            super.onPreExecute()

            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return

            activity.pbLoadingContacts.visibility = View.VISIBLE
            activity.rvContacts.visibility = View.GONE
        }

        override fun doInBackground(vararg p0: Void?): List<Contact> {
            Thread.sleep(5000)

            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return mutableListOf()

            return activity.contactDAO.getAll()
        }

        override fun onPostExecute(result: List<Contact>?) {
            super.onPostExecute(result)

            val activity = activityReference.get()
            if (activity == null || activity.isFinishing) return

            activity.pbLoadingContacts.visibility = View.GONE
            activity.rvContacts.visibility = View.VISIBLE

            if (result != null) {
                activity.contactList.clear()
                activity.contactList.addAll(result)
                activity.contactsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miNewContact) {
            val newContactIntent = Intent(this, ContactActivity::class.java)
            startActivityForResult(newContactIntent, NEW_CONTACT_REQUEST_CODE)
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == NEW_CONTACT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val newContact = data.getParcelableExtra<Contact>(EXTRA_CONTACT)
            if (newContact != null) {
                contactDAO.save(newContact)

                contactList.add(newContact)
                contactsAdapter.notifyDataSetChanged()
            }
        } else {
            if (requestCode == EDIT_CONTACT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                val editedContact: Contact? = data.getParcelableExtra(EXTRA_CONTACT)
                if (editedContact != null) {
                    contactDAO.update(editedContact)

                    contactList[contactList.indexOfFirst { it.name.equals(editedContact.name) }] = editedContact
                    contactsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onContact(position: Int) {
        val contact: Contact = contactList[position]

        val viewContactIntent = Intent(this, ContactActivity::class.java)
        viewContactIntent.putExtra(EXTRA_CONTACT, contact)
        viewContactIntent.action = VIEW_CONTACT_ACTION

        startActivity(viewContactIntent)
    }

    override fun onEditMenuItem(position: Int) {
        val selectedContact: Contact = contactList[position]

        val editContactIntent = Intent(this, ContactActivity::class.java)
        editContactIntent.putExtra(EXTRA_CONTACT, selectedContact)
        startActivityForResult(editContactIntent, EDIT_CONTACT_REQUEST_CODE)
    }

    override fun onRemoveMenuItem(position: Int) {
        val selectedContact: Contact = contactList[position]

        if (position != -1) {
            contactDAO.delete(selectedContact.name)

            contactList.removeAt(position)
            contactsAdapter.notifyDataSetChanged()
        }
    }

}