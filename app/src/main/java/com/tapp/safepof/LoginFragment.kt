package com.tapp.safepof

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.tapp.safepof.databinding.LoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding : LoginBinding
    private lateinit var viewModel : LoginViewModel
    private lateinit var viewModelFactory: LoginViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil
            .inflate(
                inflater, R.layout.login,
                container, false
            )
        // view model initialization
        viewModelFactory = LoginViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        // view model binding
        binding.loginViewModel = viewModel

        viewModel.isUserInfoCorrect.observe(this, Observer { userInfo ->
            if (userInfo == 0) {
                binding.passwordIncorrectText.setText(R.string.either_username_or_password_incorrect)
                clearEdittext()
            }
            else if (userInfo == 1) {
                binding.loginButton
                    .findNavController()
                    .navigate(
                        LoginFragmentDirections
                            .actionLoginFragmentToFinanceTrackerFragment(
                                firstName(binding.nameEdittext.text.toString())
                            )
                    )
            }
            else if (userInfo == 2) {
                binding.passwordIncorrectText.setText(R.string.password_incorrect)
                clearEdittext()
            }
            else if (userInfo == 3) {
                binding.passwordIncorrectText.setText(R.string.user_name_not_found)
                clearEdittext()
            }
            else {
                binding.passwordIncorrectText.setText(R.string.check_internet)
                binding.passwordIncorrectText.visibility = View.VISIBLE
            }
        })

        binding.loginButton.setOnClickListener {
            binding.passwordIncorrectText.visibility = View.INVISIBLE
            viewModel.checkUserInfo(
                binding.nameEdittext.text.toString(),
                binding.passwordEdittext.text.toString()
            )
        }

        binding.loginConstraintView.setOnClickListener {view: View ->
            hideKeyboard(view)
        }

        binding.forgetPasswordText.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }

        binding.createAccountText.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }

        return binding.root
    }

    private fun hideKeyboard(view: View) {
        // clear focus
        binding.nameEdittext.clearFocus()
        binding.passwordEdittext.clearFocus()
        // hide the keyboard
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun clearEdittext() {
        binding.passwordIncorrectText.visibility = View.VISIBLE
        binding.nameEdittext.setText("")
        binding.passwordEdittext.setText("")
    }

    private fun firstName(name: String): String {
        var res = ""
        for (i: Int in 0..name.length-1) {
            if (name[i] == ' ') break
            else { res += name[i] }
        }
        return res
    }

}