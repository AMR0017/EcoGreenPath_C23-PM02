package com.example.ecogreenpath_c23_pm02.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.example.ecogreenpath_c23_pm02.R
import com.example.ecogreenpath_c23_pm02.data.response.Result
import com.example.ecogreenpath_c23_pm02.databinding.ActivityRegisterBinding
import com.example.ecogreenpath_c23_pm02.ui.login.LoginActivity
import com.example.ecogreenpath_c23_pm02.utility.ViewModelFactory
import com.example.ecogreenpath_c23_pm02.utility.formatDate
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        supportActionBar?.hide()

        binding.datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding.apply {
            registButton.setOnClickListener {
                val firstName = firstNameEditText.text.toString()
                val lastName = lastnameEditText.text.toString()
                val phoneNumber = phoneNumberEditText.text.toString()
                val birthDate = dateSelectionView.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                val name = "$firstName $lastName"

                viewModel.register(name, email, password).observe(this@RegisterActivity){ result ->
                    when (result){
                        is Result.Loading -> {
                            binding.registButton.isEnabled = false
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            binding.registButton.isEnabled = true

                            AlertDialog.Builder(this@RegisterActivity).apply {
                                setTitle("Yeay!")
                                setMessage("Register Success, Welcome to Homepage")
                                setPositiveButton("Continue") {_,_ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                            println("Name : $name")
                            println("phone number : $phoneNumber")
                            println("Birth Date : $birthDate")
                            println("email : $email")
                            println("password : $password")
                        }
                        is Result.Error -> {
                            binding.registButton.isEnabled = true
                            showLoading(false)
                            message(result.error)
                        }
                    }
                }
            }
            tvlogin.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }



    private fun message(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private var selectedYear: Int = 2023
    private var selectedMonth: Int = 11
    private var selectedDayOfMonth: Int = 9

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month
                selectedDayOfMonth = dayOfMonth
                val selectedDate = formatDate(selectedDayOfMonth, selectedMonth, selectedYear)
                binding.dateSelectionView.text = selectedDate // Update TextView with selected date
            },
            selectedYear,
            selectedMonth,
            selectedDayOfMonth
        )
        datePickerDialog.show()
    }

    private fun playAnimation(){
        val title = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val firstNameTextView = ObjectAnimator.ofFloat(binding.firstNameTextView, View.ALPHA, 1f).setDuration(500)
        val lastNameTextView = ObjectAnimator.ofFloat(binding.lastNameTextView, View.ALPHA, 1f).setDuration(500)
        val firstNameEditTextLayout = ObjectAnimator.ofFloat(binding.firstNameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val lastNameEditTextLayout = ObjectAnimator.ofFloat(binding.lastNameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val phoneTextView = ObjectAnimator.ofFloat(binding.phoneNumberTextView, View.ALPHA, 1f).setDuration(500)
        val phoneNumberEditTextLayout = ObjectAnimator.ofFloat(binding.phoneNumberInputLayout, View.ALPHA, 1f).setDuration(500)
        val birthDateTextView = ObjectAnimator.ofFloat(binding.birthDate, View.ALPHA, 1f).setDuration(500)
        val dateSelectionView = ObjectAnimator.ofFloat(binding.dateSelectionView, View.ALPHA, 1f).setDuration(500)
        val datePickerButton = ObjectAnimator.ofFloat(binding.datePicker, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.registButton, View.ALPHA, 1f).setDuration(500)
        val already = ObjectAnimator.ofFloat(binding.tvlogin, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                firstNameTextView,
                firstNameEditTextLayout,
                lastNameTextView,
                lastNameEditTextLayout,
                phoneTextView,
                phoneNumberEditTextLayout,
                birthDateTextView,
                dateSelectionView,
                datePickerButton,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup,
                already

            )
            startDelay = 500
        }.start()
    }
    private fun showLoading(isLoading: Boolean){
        binding.progressbar4.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}