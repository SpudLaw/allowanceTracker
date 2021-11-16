package com.example.allowancetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.databinding.FragmentPurchaseDetailsBinding
import com.example.allowancetracker.ui.main.MainViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val PURCHASE_ARG = "purchase_arg"

/**
 * A simple [Fragment] subclass.
 * Use the [PurchaseDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PurchaseDetailsFragment : Fragment() {
    private var purchase: Purchase? = null
    private lateinit var binding: FragmentPurchaseDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            purchase = PurchaseDetailsFragmentArgs.fromBundle(it).purchase
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPurchaseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            costEditText.setText(purchase?.cost.toString())
            descriptionEditText.setText(purchase?.description)

            saveButton.setOnClickListener {
                if (costEditText.text?.isNotBlank() == true || descriptionEditText.text?.isNotBlank() == true) {
                    if (costEditText.text?.isNotBlank() == true) {
                        viewModel.purchases.value?.forEach lit@ {
                            if (it.id == purchase?.id) {
                                purchase?.cost = costEditText.text.toString().toDouble()
                                return@lit
                            }
                        }
                    }

                    if(descriptionEditText.text?.isNotBlank() == true) {
                        viewModel.purchases.value?.forEach lit@ {
                            if (it.id == purchase?.id) {
                                purchase?.description = descriptionEditText.text.toString()
                                return@lit
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please enter cost or description",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            deleteButton.setOnClickListener {
                viewModel.purchases.value?.forEach {
                    if (it.id == purchase?.id) {
                        viewModel.delete(purchase!!)
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param purchase Purchase
         * @return A new instance of fragment PurchaseDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(purchase: Purchase) =
            PurchaseDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PURCHASE_ARG, purchase)
                }
            }
    }
}