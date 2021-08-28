package com.noahliu.roomrelationshipsdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Master::class, Pet::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun dao(): Dao?

    companion object {
        private var INSTANCE: RoomDB? = null

        fun getAppDatabase(context: Context): RoomDB? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext, RoomDB::class.java, "MasterDB"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}