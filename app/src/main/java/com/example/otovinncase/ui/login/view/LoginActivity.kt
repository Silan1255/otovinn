package com.example.otovinncase.ui.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.otovinncase.MainActivity
import com.example.otovinncase.R
import com.example.otovinncase.data.model.LoginRequestModel
import com.example.otovinncase.data.model.LoginResponseModel
import com.example.otovinncase.databinding.ActivityLoginBinding
import com.example.otovinncase.ui.login.viewModel.LoginVM
import com.example.otovinncase.utils.cache.ApplicationCache
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    //region variables
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginVM: LoginVM
    private lateinit var loginRequestModel: LoginRequestModel

    @Inject
    lateinit var cache: ApplicationCache
    //endregion

    //region lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentContext = applicationContext
        loginVM = ViewModelProvider(this)[LoginVM::class.java]
        addListeners()
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    override fun onStop() {
        super.onStop()
        removeObservers()
    }
    //endregion

    //region tools
    private fun login(loginRequestModel: LoginRequestModel) {
        loginVM.login(loginRequestModel)
    }

    private fun validation(): Boolean {
        binding.apply {
            edtUsername.text.toString().let { username ->
                if (username.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.wrong_empty_username),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

            }
            edtPassword.text.toString().let { password ->
                if (password.contains(" ")) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.wrong_password_has_spaces),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                if (password.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.wrong_empty_password),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                if (password.length < 6) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.wrong_short_password),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
            return true
        }
    }

    //region listeners
    private fun setObservers() {
        loginVM.loginData.observe(this, loginDataObserver)
    }

    private fun removeObservers() {
        loginVM.loginData.removeObserver(loginDataObserver)
    }

    private fun addListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                loginRequestModel = LoginRequestModel(
                    binding.edtUsername.text.toString(), binding.edtPassword.text.toString()
                )
                if (validation()) login(loginRequestModel)
            }
        }
    }
    //endregion

    //region apply data
    private val loginDataObserver = Observer<LoginResponseModel> { response ->
        response.let { responseBody ->
            when (responseBody.code) {
                100 -> {
                    Toast.makeText(
                        this, getString(R.string.success_login), Toast.LENGTH_SHORT
                    ).show()
                    cache.setUserToken(responseBody.token)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                101 -> Toast.makeText(
                    this, getString(R.string.wrong_login), Toast.LENGTH_SHORT
                ).show()

                else -> Toast.makeText(
                    this, getString(R.string.wrong_login), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    //endregion
}

@SuppressLint("StaticFieldLeak")
lateinit var currentContext: Context