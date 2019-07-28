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
import com.tapp.safepof.databinding.ForgetPasswordBinding

class ForgetPasswordFragment: Fragment() {

    private lateinit var binding : ForgetPasswordBinding
    private lateinit var viewModel : ForgetPasswordViewModel
    private lateinit var viewModelFactory: ForgetPasswordViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil
            .inflate(
                inflater, R.layout.forget_password,
                container, false
            )
        // view model initialization
        viewModelFactory = ForgetPasswordViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ForgetPasswordViewModel::class.java)
        // view model binding
        binding.forgetPasswordViewModel = viewModel

        viewModel.isPasswordMatch.observe(this, Observer { isMatch ->
            when (isMatch) {
                true -> binding.changePasswordButton
                    .findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
                else -> passwordNotMatchReaction(0)
            }
        })

        viewModel.isNameFound.observe(this, Observer { nameFound ->
            if (!nameFound)
            {
                passwordNotMatchReaction(1)
            }
        })

        binding.changePasswordButton.setOnClickListener {view: View ->
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

    private fun passwordNotMatchReaction(whichTextToBeShown: Int) {
        binding.passwordNotMatchText.visibility = View.VISIBLE
        when (whichTextToBeShown) {
            0 -> binding.passwordNotMatchText.setText(R.string.password_not_match)
            1 -> binding.passwordNotMatchText.setText(R.string.user_name_not_found)
        }
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