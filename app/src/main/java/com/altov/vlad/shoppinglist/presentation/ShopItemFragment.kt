package com.altov.vlad.shoppinglist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.altov.vlad.shoppinglist.R
import com.altov.vlad.shoppinglist.databinding.FragmentShopItemBinding
import com.altov.vlad.shoppinglist.domain.ShopItem

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var binding:FragmentShopItemBinding
    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopItemBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ShopItemFragment","onCreate")
        super.onCreate(savedInstanceState)
        parsParams()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*initViews(view)*/
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }



    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
           binding.etName.error = message

        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            binding.etCount.error = message
        }


        viewModel.activityReadyToClose.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()

        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            binding.etName.setText(it.name)
            binding.etCount.setText(it.count.toString())
        }
        binding.saveButton.setOnClickListener {
            viewModel.editShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )


        }

    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            viewModel.addShopItem(binding.etName.text?.toString(), binding.etCount.text?.toString())


        }
    }


    private fun parsParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID,ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }

        }
    }
}