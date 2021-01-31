package br.com.jeiferson.contatinhos.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.jeiferson.contatinhos.auth.FirebaseAuth
import br.com.jeiferson.contatinhos.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var activitySignUpBinding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(activitySignUpBinding.root)
    }

    fun onSignUp(view: View) {
        val email = activitySignUpBinding.etEmailAddress.text.toString()
        val password = activitySignUpBinding.etPassword.text.toString()
        val repeatPassword = activitySignUpBinding.etRepeatPassword.text.toString()

        if (!checkFields(email, password, repeatPassword)) return

        if (password != repeatPassword) {
            Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_SHORT).show()
            activitySignUpBinding.etPassword.requestFocus()
            return
        }

        FirebaseAuth.firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Erro na criação do usuário.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun checkFields(email: String, password: String, repeatPassword: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(this, "O campo email não pode ser vazio!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "O campo password não pode ser vazio!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}