package com.example.allowancetracker.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.databinding.MainFragmentBinding

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.balance.observe(viewLifecycleOwner, Observer { allowance ->
            binding.balanceValue.text = viewModel.getBalanceString()
        })

        viewModel.purchases.observe(viewLifecycleOwner, Observer { purchases ->
            purchases.let { purchasesAdapter.setPurchases(it) }

            var currentAllowance: Double = 400.0

            purchases.forEach {
                currentAllowance -= it.cost
            }

            viewModel.balance.value = currentAllowance
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
                        val purchase = Purchase(0, cost, null, description)

                        viewModel.add(purchase)
                        viewModel.balance.value = viewModel.balance.value?.minus(purchase.cost)

                    } else {
                        Toast.makeText(
                            context,
                            "Please enter cost and description",
                            Toast.LENGTH_LONG
                        ).show()
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