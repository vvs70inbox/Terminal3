package ru.vvs.terminal1.data

import ru.vvs.terminal1.data.room.dao.SalesDao
import ru.vvs.terminal1.data.room.dao.SalesItemDao
import ru.vvs.terminal1.model.Sale

class SalesRepository(private val salesDao: SalesDao, private val salesItemDao: SalesItemDao) {

    suspend fun getSales(newList: Boolean): List<Sale> {
        return salesDao.getAllSales()
    }

    suspend fun newSale(sale: Sale) {
        salesDao.insert(sale)
    }

    suspend fun getSaleItems() {

    }

    suspend fun deleteSale(sale: Sale) {
        salesDao.delete(sale)
    }
}