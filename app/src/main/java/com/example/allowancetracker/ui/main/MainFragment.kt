package com.example.allowancetracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.allowancetracker.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.addPurchaseButton.setOnClickListener {
            with(binding.addPurchaseDialog) {
                dialogPositiveButton.setOnClickListener {
                    toggleDialogVisibility(false)

                    val cost: Double = costEditText.text.toString().toDouble()
                    val description: String = descriptionEditText.text.toString()
                    val purchase: Purchase = Purchase(cost, null, description)

                    viewModel.add(purchase)
                }

                dialogNegativeButton.setOnClickListener {
                    toggleDialogVisibility(false)
                }
            }

            toggleDialogVisibility(true)
        }
    }

    private fun toggleDialogVisibility(visible: Boolean) {
        binding.addPurchaseDialog.root.isVisible = visible
    }

}