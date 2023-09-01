package com.altov.vlad.shoppinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.altov.vlad.shoppinglist.data.ShopListRepositoryImpl
import com.altov.vlad.shoppinglist.domain.AddShopItemUseCase
import com.altov.vlad.shoppinglist.domain.EditShopItemUseCase
import com.altov.vlad.shoppinglist.domain.GetShopItemUseCase
import com.altov.vlad.shoppinglist.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    private val _errorInputCount = MutableLiveData<Boolean>()
    private val _shopItem = MutableLiveData<ShopItem>()
    private val _activityReadyToClose = MutableLiveData<Unit>()
    val errorInputName:LiveData<Boolean>
        get() {
            return _errorInputName
        }
    val errorInputCount:LiveData<Boolean>
        get() {
            return _errorInputCount
        }
    val shopItem:LiveData<ShopItem>
        get(){
            return _shopItem
        }
    val activityReadyToClose:LiveData<Unit>
        get(){
            return _activityReadyToClose
        }

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item

    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parsName(inputName)
        val count = parsCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parsName(inputName)
        val count = parsCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopItem.value?.let {
                val item = it.copy(name = name,count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork() }

        }
    }


    private fun parsName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parsCount(inputCount: String?): Int {
        return try {
            val count = inputCount?.trim()?.toInt() ?: 0
            count
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0.0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }
    fun resetErrorInputName(){
        _errorInputName.value = false
    }
    fun resetErrorInputCount(){
        _errorInputCount.value = false
    }
    private fun finishWork(){
        _activityReadyToClose.value = Unit
    }
}