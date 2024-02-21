package ru.vvs.terminal1.screens.orderListScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.model.Order
import ru.vvs.terminal1.model.OrderDao

class OrdersViewModel(application: Application): AndroidViewModel(application) {

    private val orderDao: OrderDao //= RetrofitRepository()

    val orderList: MutableLiveData<List<Order>> = MutableLiveData()
    val isProgress = MutableLiveData<Boolean>(false)
    var selectedOrder: MutableLiveData<Order> = MutableLiveData()

    init {
        orderDao = CartsDatabase.getInstance(application).getOrderDao()
    }

    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            isProgress.postValue(true)
            orderList.postValue(orderDao.getAllOrders())
            isProgress.postValue(false)
        }
    }

    fun newOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            selectedOrder.postValue(Order.newOrder(orderDao))
            getOrders()
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            orderDao.delete(order)
            getOrders()
        }
    }

}