package com.example.allowancetracker.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allowancetracker.R
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.databinding.MainFragmentBinding

class MainFragment : Fragment(R.layout.main_fragment) {

    private var _binding: MainFragmentBinding? = null

    private val binding: MainFragmentBinding
        get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MainFragmentBinding.bind(view)

        viewModel.balance.observe(viewLifecycleOwner, { allowance ->
            binding.balanceValue.text = String.format("$%.2f", allowance)
        })

        viewModel.purchases.observe(viewLifecycleOwner) {
            (binding.purchasesRecyclerView.adapter as PurchasesAdapter).submitList(it.asReversed())
        }

        binding.balanceValue.text = viewModel.getBalanceString()
        binding.purchasesRecyclerView.adapter = PurchasesAdapter()


        binding.addPurchaseButton.setOnClickListener {
            with(binding.addPurchaseDialog) {

                costEditText.text?.clear()
                descriptionEditText.text?.clear()

                dialogPositiveButton.setOnClickListener {
                    toggleDialogVisibility(false)

                    if (costEditText.text?.isNotBlank() == true && descriptionEditText.text?.isNotBlank() == true) {
                        val cost: Double = costEditText.text?.toString()?.toDouble() ?: 0.0
                        val description: String = descriptionEditText.text.toString()
                        val purchase = Purchase(0, cost, null, description)

                        viewModel.add(purchase)

                        binding.addPurchaseButton.isVisible = true

                    } else {
                        Toast.makeText(
                            context,
                            "Please enter cost and description",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                    binding.addPurchaseButton.isVisible = true
                }

                dialogNegativeButton.setOnClickListener {
                    toggleDialogVisibility(false)
                    binding.addPurchaseButton.isVisible = true
                }
            }

            toggleDialogVisibility(true)

            binding.addPurchaseButton.isVisible = false
        }

        binding.addToBalanceButton.setOnClickListener {
            with(binding.allowanceDialog) {
                balanceTextEdit.text?.clear()

                dialogPositiveButton.setOnClickListener {
                    toggleDialogVisibility(visible = false, purchase = false)

                    if (balanceTextEdit.text?.isNotBlank() == true) {
                        val amountToAdd: Double = balanceTextEdit.text.toString().toDouble()
                        viewModel.setBalance(amountToAdd)

                    } else {
                        Toast.makeText(
                            context,
                            "Please enter allowance",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                dialogNegativeButton.setOnClickListener {
                    toggleDialogVisibility(visible = false, purchase = false)
                }
            }
            toggleDialogVisibility(visible = true, purchase = false)
        }
    }

    private fun toggleDialogVisibility(visible: Boolean, purchase: Boolean = true) {
        if (purchase) {
            binding.addPurchaseDialog.root.isVisible = visible
        } else {
            binding.allowanceDialog.root.isVisible = visible
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear our the reference so our view can be garbage collected
        _binding = null
    }
}
