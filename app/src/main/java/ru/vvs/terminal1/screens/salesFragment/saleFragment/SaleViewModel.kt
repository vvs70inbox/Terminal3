package ru.vvs.terminal1.screens.salesFragment.saleFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.ItemsSaleRepository
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.ItemsSale

class SaleViewModel(application: Application): AndroidViewModel(application) {

    private val repository: ItemsSaleRepository

    private var _myItemsList: MutableLiveData<List<ItemsSale>> = MutableLiveData()
    val myItemsList: LiveData<List<ItemsSale>> = _myItemsList

    var itemOrder: MutableLiveData<ItemsOrder> = MutableLiveData()

    init {
        val itemsDao = CartsDatabase.getInstance(application).getAllItemsFromSale()
        repository = ItemsSaleRepository(itemsDao)
    }

    fun getItems(orderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _myItemsList.postValue(repository.getItems(orderId))
        }
    }
}