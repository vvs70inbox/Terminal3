package ru.vvs.terminal1.screens.ordersFragment.orderFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.ItemsRepository
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

    fun updateOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOrder(order)
        }
    }

    private fun getItemByBarcode(barcode: String): ItemsOrder? {
        return myItemsList.value!!.find { it.Barcode == barcode }
    }

    fun updateItem(barcode: String, orderId: Int) {
        val item = getItemByBarcode(barcode)
        if (item == null) {
            viewModelScope.launch(Dispatchers.IO) {
                itemOrder.postValue(repository.newItem(barcode, orderId))
                getItems(orderId)
                itemOrder = MutableLiveData()
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                itemOrder.postValue(repository.updateItem(barcode, orderId))
                getItems(orderId)
                itemOrder = MutableLiveData()
            }
        }
    }

    fun updateItemCount(itemsOrder: ItemsOrder, orderId: Int, counts: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _myItemsList.value!!.find{ it.Barcode == itemsOrder.Barcode }!!.counts = counts
            repository.updateItemCount(itemsOrder, orderId)
            getItems(orderId)
        }
    }

    fun swipeItem(position: Int, orderId: Int) {
        val itemsOrder = myItemsList.value!![position]
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(itemsOrder.Barcode, orderId)
            _myItemsList.postValue(_myItemsList.value!!.toMutableList().apply { removeAt(position)})
            getItems(orderId)
        }
    }
}