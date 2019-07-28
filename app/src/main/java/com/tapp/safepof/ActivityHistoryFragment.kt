package com.tapp.safepof


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.tapp.safepof.databinding.ActivityHistoryBinding


class ActivityHistoryFragment : Fragment() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: ActivityHistoryViewModel
    private lateinit var viewModelFactory: ActivityHistoryViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = inflate(inflater,
            R.layout.activity_history, container, false)

        viewModelFactory = ActivityHistoryViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ActivityHistoryViewModel::class.java)
        binding.activityHistoryViewModel = viewModel

        binding.backButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(ActivityHistoryFragmentDirections
                    .actionActivityHistoryFragmentToFinanceTrackerFragment(
                        ActivityHistoryFragmentArgs.fromBundle(arguments!!).userName
                    ))
        }

        val adapter = ActivityHistoryAdapter()
        binding.activityHistoryList.adapter = adapter
        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        return binding.root
    }


}
