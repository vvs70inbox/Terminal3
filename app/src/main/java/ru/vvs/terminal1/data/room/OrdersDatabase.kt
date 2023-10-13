package ru.vvs.terminal1.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import ru.vvs.terminal1.data.room.dao.OrdersDao
import ru.vvs.terminal1.model.Order

@Database(entities = [Order::class], version = 1)
abstract class OrdersDatabase: RoomDatabase() {

    abstract fun getOrdersDao(): OrdersDao

    companion object {
        @Volatile
        private var database: OrdersDatabase?= null

        fun getInstance(context: Context): OrdersDatabase {
            return  if (database == null) {
                database = Room
                    .databaseBuilder(context, OrdersDatabase::class.java, "db_orders")
                    .build()
                database as OrdersDatabase
            } else {
                database as OrdersDatabase
            }
        }
    }
}