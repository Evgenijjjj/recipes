package r.evgenymotorin.recipes.database.interfaces

import android.arch.persistence.room.*
import r.evgenymotorin.recipes.database.tables.AdaptiveIngredientData

@Dao
interface AdaptiveIngredientDataDao {
    @Query("SELECT * from AdaptiveIngredientData")
    fun getAll(): List<AdaptiveIngredientData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(adaptiveIngredientData: AdaptiveIngredientData)

    @Query("DELETE from AdaptiveIngredientData")
    fun deleteAll()

    @Update
    fun updateAdaptiveIngredientData(adaptiveIngredientData: AdaptiveIngredientData)

    @Query("SELECT * from AdaptiveIngredientData WHERE id = (SELECT MAX(id) from AdaptiveIngredientData)")
    fun getLastAdaptiveIngredientData(): AdaptiveIngredientData

    @Query("SELECT * from AdaptiveIngredientData WHERE id = :ID LIMIT 1")
    fun getAdaptiveIngredientDataWithId(ID: Int): AdaptiveIngredientData

    @Query("DELETE FROM AdaptiveIngredientData WHERE id = :ID")
    fun deleteAdaptiveIngredientDataWithId(ID: Int)
}