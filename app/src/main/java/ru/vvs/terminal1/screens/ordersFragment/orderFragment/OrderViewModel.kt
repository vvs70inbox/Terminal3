package ru.vvs.terminal1.screens.ordersFragment.orderFragment

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ru.vvs.terminal1.MainActivity
import ru.vvs.terminal1.data.ItemsRepository
import ru.vvs.terminal1.data.retrofit.api.RetrofitInstance
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.mainActivity
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.model.Order1C

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

    fun createOrderIn1C() {
        val order1C: MutableList<Order1C> = mutableListOf()

        myItemsList.value!!.forEach {
            order1C.add(0,Order1C(it.Barcode, it.counts.toString()))
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.postOrder("VVS","999", order1C) //RetrofitClient.apiService.createUser(user)

                if (response.isSuccessful) {
                    Log.d("createOrderIn1C", "Order created")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(mainActivity, "Заказ передан в 1С", Toast.LENGTH_SHORT).show()
                    }
                    // Делаем что-то с созданным заказом
                } else {
                    Log.e("createOrderIn1C", "Failed to create order.")
                }
            } catch (e: Exception) {
                Log.e("createOrderIn1C", "Error occurred: ${e.message}")
            }
        }
    }
}