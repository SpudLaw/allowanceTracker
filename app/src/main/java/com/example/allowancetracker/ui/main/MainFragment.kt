package com.example.allowancetracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.databinding.MainFragmentBinding
import java.util.*

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var purchasesAdapter: PurchasesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        purchasesAdapter = PurchasesAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.balance.observe(viewLifecycleOwner, { allowance ->
            binding.balanceValue.text = String.format("$%.2f", allowance)
        })

        viewModel.purchases.observe(viewLifecycleOwner, { purchases ->
            purchases.let { purchasesAdapter.setPurchases(it) }
        })

        binding.balanceValue.text = viewModel.getBalanceString()
        binding.purchasesRecyclerView.adapter = purchasesAdapter
        binding.purchasesRecyclerView.layoutManager = LinearLayoutManager(context)


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

}