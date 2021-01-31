package br.com.jeiferson.contatinhos.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.jeiferson.contatinhos.auth.FirebaseAuth
import br.com.jeiferson.contatinhos.databinding.ActivitySignInBinding

class SingInActivity : AppCompatActivity() {

    private lateinit var activitySingInBinding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySingInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(activitySingInBinding.root)
    }

    fun onSignIn(view: View) {
        val email = activitySingInBinding.etEmailAddress.text.toString()
        val password = activitySingInBinding.etPassword.text.toString()

        if (!checkFields(email, password)) return

        FirebaseAuth.firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this, "Usuário autenticado com sucesso!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Falha na autenticação do usuário!", Toast.LENGTH_SHORT).show()
            }
    }

    fun onForget(view: View) {
        startActivity(Intent(this, ForgetPasswordActivity::class.java))
    }

    fun onSignUp(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    fun checkFields(email: String, password: String): Boolean {
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
        return true
    }

}