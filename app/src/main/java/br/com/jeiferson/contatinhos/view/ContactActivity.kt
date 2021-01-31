package br.com.jeiferson.contatinhos.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.jeiferson.contatinhos.databinding.ActivityContactBinding
import br.com.jeiferson.contatinhos.model.Contact

class ContactActivity : AppCompatActivity() {

    private lateinit var activityContactBinding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityContactBinding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(activityContactBinding.root)

        val contact: Contact? = intent.getParcelableExtra(MainActivity.Extras.EXTRA_CONTACT)
        if (contact != null) {
            activityContactBinding.etContactName.setText(contact.name)
            activityContactBinding.etContactName.isEnabled = false
            activityContactBinding.etPhoneNumber.setText(contact.phoneNumber)
            activityContactBinding.etEmailAddress.setText(contact.email)
            if (intent.action == MainActivity.Extras.VIEW_CONTACT_ACTION) {
                activityContactBinding.etPhoneNumber.isEnabled = false
                activityContactBinding.etEmailAddress.isEnabled = false
                activityContactBinding.btSave.visibility = View.GONE
            }
        }

        activityContactBinding.btSave.setOnClickListener {
            val newContact = Contact(
                activityContactBinding.etContactName.text.toString(),
                activityContactBinding.etPhoneNumber.text.toString(),
                activityContactBinding.etEmailAddress.text.toString()
            )

            val returnIntent = Intent()
            returnIntent.putExtra(MainActivity.Extras.EXTRA_CONTACT, newContact)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

}