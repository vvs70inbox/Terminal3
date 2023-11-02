package ru.vvs.terminal1.screens.salesFragment.saleFragment

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vvs.terminal1.data.ItemsSaleRepository
import ru.vvs.terminal1.data.room.CartsDatabase
import ru.vvs.terminal1.mainActivity
import ru.vvs.terminal1.model.ItemsOrder
import ru.vvs.terminal1.model.ItemsSale

class SaleViewModel(application: Application): AndroidViewModel(application) {

    private val repository: ItemsSaleRepository

    private var _myItemsList: MutableLiveData<List<ItemsSale>> = MutableLiveData()
    val myItemsList: LiveData<List<ItemsSale>> = _myItemsList

    var itemSale: MutableLiveData<ItemsSale> = MutableLiveData()

    init {
        val itemsDao = CartsDatabase.getInstance(application).getAllItemsFromSale()
        repository = ItemsSaleRepository(itemsDao)
    }

    fun getItems(orderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _myItemsList.postValue(repository.getItems(orderId))
        }
    }

    fun updateItemCount(itemsSale: ItemsSale, orderId: Int, checks: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _myItemsList.value!!.find{ it.Barcode == itemsSale.Barcode }!!.checks = checks
            repository.updateItemCount(itemsSale, orderId)
            getItems(orderId)
        }
    }

    private fun getItemByBarcode(barcode: String): ItemsSale? {
        return myItemsList.value!!.find { it.Barcode == barcode }
    }
    fun updateItem(barcode: String, orderId: Int) {
        val item = getItemByBarcode(barcode)
        if (item == null) {
            Toast.makeText(mainActivity, "Товар по штрихкоду не найден в отгузке!!!", Toast.LENGTH_LONG).show()
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                itemSale.postValue(repository.updateItem(barcode, orderId))
                getItems(orderId)
                itemSale = MutableLiveData()
            }
        }
    }
}