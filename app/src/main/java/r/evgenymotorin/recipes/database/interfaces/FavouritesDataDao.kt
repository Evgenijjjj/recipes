package r.evgenymotorin.recipes.database.interfaces

import android.arch.persistence.room.*
import r.evgenymotorin.recipes.database.tables.FavouritesData

@Dao
interface FavouritesDataDao {
    @Query("SELECT * from FavouritesData")
    fun getAll(): List<FavouritesData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favouritesData: FavouritesData)

    @Query("DELETE from FavouritesData")
    fun deleteAll()

    @Update
    fun updateFavouritesData(favouritesData: FavouritesData)

    @Query("SELECT * from FavouritesData WHERE id = (SELECT MAX(id) from FavouritesData)")
    fun getLastFavouritesData(): FavouritesData

    /*@Query("SELECT * from FavouritesData WHERE id = :ID LIMIT 1")
    fun getFavouritesDataWithId(ID: Int): FavouritesData?

    @Query("DELETE FROM FavouritesData WHERE id = :ID")
    fun deletFavouritesDataWithId(ID: Int)*/

    @Query("SELECT * from FavouritesData WHERE recipeUrl = :URL LIMIT 1")
    fun getFavouritesDataWithUrl(URL: String): FavouritesData?

    @Query("DELETE FROM FavouritesData WHERE recipeUrl = :URL")
    fun deleteFavouritesDataWithUrl(URL: String)
}