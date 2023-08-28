package com.altov.vlad.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.altov.vlad.shoppinglist.data.ShopListRepositoryImpl
import com.altov.vlad.shoppinglist.domain.AddShopItemUseCase
import com.altov.vlad.shoppinglist.domain.EditShopItemUseCase
import com.altov.vlad.shoppinglist.domain.GetShopItemUseCase
import com.altov.vlad.shoppinglist.domain.ShopItem

class ShopItemViewModel:ViewModel() {

    private  val repository = ShopListRepositoryImpl

   private val getShopItemUseCase = GetShopItemUseCase(repository)
   private val addShopItemUseCase = AddShopItemUseCase(repository)
   private val editShopItemUseCase = EditShopItemUseCase(repository)

    fun getShopItem(shopItemId:Int){
        val item = getShopItemUseCase.getShopItem(shopItemId)
    }
    fun addShopItem(inputName:String?,inputCount:String?){
        val name = parsName(inputName)
        val count = parsCount(inputCount)
        val fieldsValid = validateInput(name,count)
        if(fieldsValid){
            val shopItem = ShopItem(name,count,true)
            addShopItemUseCase.addShopItem(shopItem)
        }

    }
    fun editShopItem(inputName:String?,inputCount:String?){
        val name = parsName(inputName)
        val count = parsCount(inputCount)
        val fieldsValid = validateInput(name,count)
        if(fieldsValid){
            val shopItem = ShopItem(name,count,true)
            editShopItemUseCase.editShopItem(shopItem)
        }
    }
    private fun parsName(inputName:String?):String{
        return inputName?.trim() ?: ""
    }
    private fun parsCount(inputCount: String?):Double{
        return try {
            val count = inputCount?.trim()?.toDouble() ?: 0.0
            count
        } catch (e:Exception){
            0.0
        }
    }
    private fun validateInput(name:String,count:Double):Boolean {
        var result = true
        if(name.isBlank()){
            //TODO:Show error input name
            result = false
        }
        if(count<=0.0){
            //TODO:Show error input count
            result = false
        }
        return result
    }
}