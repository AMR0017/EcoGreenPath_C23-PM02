package com.example.ecogreenpath_c23_pm02.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isGone
import com.example.ecogreenpath_c23_pm02.MainActivity
import com.example.ecogreenpath_c23_pm02.R
import com.example.ecogreenpath_c23_pm02.data.pref.PreferenceDataSource
import com.example.ecogreenpath_c23_pm02.data.response.Result
import com.example.ecogreenpath_c23_pm02.databinding.ActivityLoginBinding
import com.example.ecogreenpath_c23_pm02.ui.signup.RegisterActivity
import com.example.ecogreenpath_c23_pm02.utility.ViewModelFactory
import com.example.ecogreenpath_c23_pm02.utility.gone
import com.example.ecogreenpath_c23_pm02.utility.show

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val pref by lazy {
        PreferenceDataSource.invoke(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        playAnimation()

        binding.apply {
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (password.length >= 8){
                    viewModel.login(email, password).observe(this@LoginActivity){ result ->
                        when(result){
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success ->{
                                showLoading(false)
                                binding.loginButton.isEnabled = true

                                result.data.let {
                                    if (!it.error){
                                        pref.saveAuthToken(it.loginResult.token)
                                        message(it.message)
                                        intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        message(it.message)
                                    }
                                }
                            }
                            is Result.Error -> {
                                showLoading(false)
                                message(result.error)
                            }
                        }

                    }
                }
            }
            register.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

    }


    private fun message(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading:Boolean){
        if (isLoading) binding.progressbar3.show() else binding.progressbar3.gone()
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val register= ObjectAnimator.ofFloat(binding.register, View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title,emailTextView,emailEditTextLayout,passwordTextView,passwordEditTextLayout,login,register)
            startDelay = 500
        }.start()
    }
}