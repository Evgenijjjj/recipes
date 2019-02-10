package r.evgenymotorin.recipes.database.interfaces

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import r.evgenymotorin.recipes.database.tables.SearchHistoryData

@Dao
interface SearchHistoryDataDao {
    @Query("SELECT * from SearchHistoryData")
    fun getAll(): List<SearchHistoryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchHistoryData: SearchHistoryData)

    @Query("DELETE from SearchHistoryData")
    fun deleteAll()

    @Query("SELECT * from SearchHistoryData WHERE request = :REQUEST LIMIT 1")
    fun getDataWithRequest(REQUEST: String): SearchHistoryData?
}