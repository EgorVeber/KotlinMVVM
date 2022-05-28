package ru.gb.veber.kotlinmvvm.room

import android.database.Cursor
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

    @Query("Delete from HistorySelect where id = :id")
    fun deleteById(id: Int)

    @Query("Delete from HistorySelect")
    fun deleteAll()


    @Query("SELECT id, city, temperature FROM HistorySelect")
    fun getHistoryCursor(): Cursor

    @Query("SELECT id, city, temperature FROM HistorySelect WHERE id = :id")
    fun getHistoryCursor(id: Long): Cursor
}