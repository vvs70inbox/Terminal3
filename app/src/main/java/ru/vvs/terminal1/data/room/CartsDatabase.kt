package ru.vvs.terminal1.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.vvs.terminal1.data.room.dao.CartsDao
import ru.vvs.terminal1.model.CartItem

@Database(entities = [CartItem::class], version = 1)
abstract class CartsDatabase: RoomDatabase() {

    abstract fun getCartsDao(): CartsDao

    companion object {
        @Volatile
        private var database: CartsDatabase ?= null

        fun getInstance(context: Context): CartsDatabase {
            return  if (database == null) {
                database = Room
                    .databaseBuilder(context, CartsDatabase::class.java, "db")
                    .build()
                database as CartsDatabase
            } else {
                database as CartsDatabase
            }
        }
    }
}