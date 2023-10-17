package ru.vvs.terminal1.screens.ordersFragment.orderFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.ItemsRepository
import ru.vvs.terminal1.data.OrdersRepository
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.Order

class OrderViewModel(application: Application): AndroidViewModel(application) {

    private val repository: ItemsRepository

    private var _myItemsList: MutableLiveData<List<ItemsOrder>> = MutableLiveData()
    val myItemsList: LiveData<List<ItemsOrder>> = _myItemsList

    var itemOrder: MutableLiveData<ItemsOrder> = MutableLiveData()

    init {
        val itemsDao = CartsDatabase.getInstance(application).getAllItemsFromOrder()
        repository = ItemsRepository(itemsDao)
    }

    fun getItems(orderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _myItemsList.postValue(repository.getItems(orderId))
        }
    }

    fun getItemByBarcode(barcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            itemOrder.postValue(repository.getItemByBarcode(barcode))
        }

    }
}