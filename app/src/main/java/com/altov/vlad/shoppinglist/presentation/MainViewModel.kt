package com.altov.vlad.shoppinglist.presentation


import androidx.lifecycle.ViewModel
import com.altov.vlad.shoppinglist.data.ShopListRepositoryImpl
import com.altov.vlad.shoppinglist.domain.DeleteShopItemUseCase
import com.altov.vlad.shoppinglist.domain.EditShopItemUseCase
import com.altov.vlad.shoppinglist.domain.GetShopListUseCase
import com.altov.vlad.shoppinglist.domain.ShopItem

class MainViewModel:ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)

    }
    fun changeEnableState(shopItem: ShopItem){
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)

    }

}