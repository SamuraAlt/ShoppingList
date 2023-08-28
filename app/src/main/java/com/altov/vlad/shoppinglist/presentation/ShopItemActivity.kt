package com.altov.vlad.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.altov.vlad.shoppinglist.databinding.ActivityShopItemBinding

class ShopItemActivity : AppCompatActivity() {
    private lateinit var binding:ActivityShopItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}