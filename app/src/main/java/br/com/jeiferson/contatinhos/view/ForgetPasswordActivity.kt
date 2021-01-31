package br.com.jeiferson.contatinhos.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.jeiferson.contatinhos.auth.FirebaseAuth
import br.com.jeiferson.contatinhos.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var activityForgetPasswordBinding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityForgetPasswordBinding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(activityForgetPasswordBinding.root)
    }

    fun onForget(view: View) {
        val email = activityForgetPasswordBinding.etEmailAddress.text.toString()

        if (!checkFields(email)) return

        FirebaseAuth.firebaseAuth.sendPasswordResetEmail(email)
        Toast.makeText(this, "E-mail de recuperação de senha enviado!", Toast.LENGTH_SHORT).show()

        finish()
    }

    fun checkFields(email: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "O campo email não pode ser vazio!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}