package com.example.nasaimage.room


import android.content.Context
import androidx.room.*
import com.example.nasaimage.NasaDataModel


@Database(entities = [NasaDataModel::class], version = 1)
abstract class NasaDatabase : RoomDatabase() {
    abstract val dbDao : dbDao
    companion object
    {
        @Volatile
        private var INSTANCE : NasaDatabase? = null
        fun getInstance(context: Context): NasaDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NasaDatabase::class.java,
                        "apod_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}
