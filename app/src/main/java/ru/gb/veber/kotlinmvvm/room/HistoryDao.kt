package ru.gb.veber.kotlinmvvm.room

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("Select * from HistorySelect")
    fun all(): List<HistorySelect>

    @Query("Select * from HistorySelect where city Like :cityName ")
    fun getDataByCityName(cityName: String): List<HistorySelect>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistorySelect)

    @Update
    fun update(entity: HistorySelect)

    @Delete
    fun delete(entity: HistorySelect)
}