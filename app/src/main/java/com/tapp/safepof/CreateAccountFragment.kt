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
import com.tapp.safepof.databinding.CreateAccountBinding

class CreateAccountFragment: Fragment() {

    private lateinit var binding : CreateAccountBinding
    private lateinit var viewModel : CreateAccountViewModel
    private lateinit var viewModelFactory: CreateAccountViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil
            .inflate(
                inflater, R.layout.create_account,
                container, false
            )
        // view model initialization
        viewModelFactory = CreateAccountViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateAccountViewModel::class.java)
        // view model binding
        binding.createAccountViewModel = viewModel

        viewModel.isPasswordMatch.observe(this, Observer { isMatch ->
            when (isMatch) {
                true -> binding.createAccountButton
                    .findNavController().navigate(R.id.action_createAccountFragment_to_loginFragment)
                else -> passwordNotMatchReaction()
            }
        })

        binding.createAccountButton.setOnClickListener {view: View ->
            hideKeyboard(view)
            binding.passwordNotMatchText.visibility = View.INVISIBLE

            viewModel.checkPassword(
                binding.userNameEdittext.text.toString(),
                binding.newPasswordEdittext.text.toString(),
                binding.enterAgainEdittext.text.toString()
            )
        }

        binding.resetPasswordConstraintView.setOnClickListener { view: View ->
            hideKeyboard(view)
        }

        return binding.root
    }

    private fun passwordNotMatchReaction() {
        binding.passwordNotMatchText.visibility = View.VISIBLE
        binding.passwordNotMatchText.setText(R.string.password_not_match)
        binding.newPasswordEdittext.setText("")
        binding.enterAgainEdittext.setText("")
    }

    private fun hideKeyboard(view: View) {
        // clear focus
        binding.newPasswordEdittext.clearFocus()
        binding.enterAgainEdittext.clearFocus()
        // hide the keyboard
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}