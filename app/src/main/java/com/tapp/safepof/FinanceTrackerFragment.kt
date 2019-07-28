package com.tapp.safepof

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.tapp.safepof.databinding.FinanceTrackerBinding


class FinanceTrackerFragment : Fragment() {

    private lateinit var binding: FinanceTrackerBinding
    private lateinit var viewModel: FinanceTrackerViewModel
    private lateinit var viewModelFactory: FinanceTrackerViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // layout inflation with data-binding
        binding = DataBindingUtil.inflate(inflater,
            R.layout.finance_tracker, container, false)

        // initialization for ViewModel
        viewModelFactory = FinanceTrackerViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FinanceTrackerViewModel::class.java)
        // data-binding for ViewModel
        binding.financeTrackerViewModel = viewModel

        // set currentBalance LiveData observer
        viewModel.currentBalance.observe(this, Observer {
            binding.depositTextview.setText(viewModel.currentBalanceString())
        })
        // set whichButton LiveData observer
        viewModel.whichButtonClicked.observe(this, Observer { buttonClicked ->
            when (buttonClicked) {
                0 -> binding.tapPrompt.setText("Deposit")
                1 -> binding.tapPrompt.setText("Withdraw")
            }
            resetTypeface()
            binding.doneImage.visibility = View.VISIBLE
            binding.transactionAmountEditText.visibility = View.VISIBLE
            binding.purposeEdittext.visibility = View.VISIBLE
            binding.purposeEdittext.requestFocus()
            // shows the keyboard
            showKeyboard()
        })

        binding.welcomeUserText.setText(
            "Welcome back, ${FinanceTrackerFragmentArgs.fromBundle(arguments!!).userName}"
        )

        // set click listener for the history button
        binding.logImage.setOnClickListener{ view: View ->
            // navigates to the history fragment
            view.findNavController()
                .navigate(FinanceTrackerFragmentDirections.actionFinanceTrackerFragmentToActivityHistoryFragment(
                    FinanceTrackerFragmentArgs.fromBundle(arguments!!).userName
                ))
        }
        // set click listener for the done button
        binding.doneImage.setOnClickListener { onClickDoneButton(viewModel.whichButtonClicked.value!!) }

        binding.financeTrackerConstraintView.setOnClickListener { view: View ->
            hideKeyboard(view)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard(binding.financeTrackerConstraintView)
        hideInputViews()
    }


    // click lister function for the done button
    private fun onClickDoneButton(whichButton: Int) {
        // deal with invalid inputs
        if (binding.transactionAmountEditText.text.toString() == "")
        {
            // reset text style
            resetTypeface()
            binding.tapPrompt.setText(R.string.tap_prompt_text)

            // hide keyboard
            hideKeyboard(binding.financeTrackerConstraintView)

            // hide unnecessary views
            hideInputViews()
        }
        else
        {
            viewModel.setAmount(binding.transactionAmountEditText.text.toString().toDouble())

            // deals with an invalid amount
            if (whichButton == 1 && viewModel.isEnteredAmountInvalid()) {
                binding.tapPrompt.setText("Amount exceeds the balance")
                binding.tapPrompt.setTypeface(Typeface.DEFAULT_BOLD)
                binding.tapPrompt.setTextColor(Color.parseColor("#E32636"))

                binding.transactionAmountEditText.setText("")
                binding.purposeEdittext.setText("")
            }
            else {
                binding.transactionAmountEditText.setText("")
                binding.purposeEdittext.setText("")
                hideInputViews()
                resetTypeface()
                binding.tapPrompt.setText(R.string.tap_prompt_text)

                hideKeyboard(binding.financeTrackerConstraintView)
                viewModel.updateCurrentBalance(whichButton, binding.purposeEdittext.text.toString())
                Toast.makeText(activity, viewModel.notifyModifyCompleted(), Toast.LENGTH_SHORT).show()
            }
        }

    }


    /*
     * a method to reset text style
     */
    private fun resetTypeface() {
        binding.tapPrompt.typeface = Typeface.DEFAULT
        binding.tapPrompt.setTextColor(Color.parseColor("#A9A9A9"))
    }

    /*
     * a method which hides keyboard and clear focus
     *
     * @param view: a View of window token
     */
    private fun hideKeyboard(view: View) {
        // clear focus
        binding.transactionAmountEditText.clearFocus()
        binding.purposeEdittext.clearFocus()
        // hide the keyboard
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /*
     * a method which shows keyboard
     */
    private fun showKeyboard() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.purposeEdittext, 0)
    }

    /*
     * a method which views for inputs
     */
    private fun hideInputViews() {
        binding.purposeEdittext.visibility = View.INVISIBLE
        binding.doneImage.visibility = View.INVISIBLE
        binding.transactionAmountEditText.visibility = View.INVISIBLE
    }

}
