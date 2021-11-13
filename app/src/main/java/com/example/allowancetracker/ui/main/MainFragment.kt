package com.example.allowancetracker.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.allowanceValue.text = "$" + viewModel.allowance.toString()

        binding.addPurchaseButton.setOnClickListener {
            with(binding.addPurchaseDialog) {

                costEditText.text?.clear()
                descriptionEditText.text?.clear()

                dialogPositiveButton.setOnClickListener {
                    toggleDialogVisibility(false)

                    if (costEditText.text?.isNotBlank() == true && descriptionEditText.text?.isNotBlank() == true) {
                        val cost: Double = costEditText.text?.toString()?.toDouble() ?: 0.0
                        val description: String = descriptionEditText.text.toString()
                        val purchase = Purchase(cost, null, description)

                        viewModel.add(purchase)
                    } else {
                        Toast.makeText(context, "Please enter cost and description", Toast.LENGTH_LONG).show()
                    }
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