package com.example.nasaimage.room



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nasaimage.NasaDataModel

@Dao
interface dbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apiEntity: NasaDataModel)

    @Query("select * from NasaDataModel")
    fun dataAvailable() : NasaDataModel?
    // You can add more queries here as needed
}
