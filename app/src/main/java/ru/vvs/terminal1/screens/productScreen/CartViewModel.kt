package ru.vvs.terminal1.screens.productScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.ProductRepository
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.model.CartItem

class CartViewModel(app: Application): AndroidViewModel(app) {

    val repository: ProductRepository

    private var _myItemsList: MutableLiveData<List<CartItem>> = MutableLiveData()
    val myItemsList: LiveData<List<CartItem>> = _myItemsList

    init {
        val cartDao = CartsDatabase.getInstance(app).getCartsDao()
        repository = ProductRepository(cartDao)
    }

    fun getItems(product: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _myItemsList.postValue(repository.getCartsByProduct(product))
        }
    }

}