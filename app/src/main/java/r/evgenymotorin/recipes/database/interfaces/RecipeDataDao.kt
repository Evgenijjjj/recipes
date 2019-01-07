package r.evgenymotorin.recipes.database.interfaces

import android.arch.persistence.room.*
import r.evgenymotorin.recipes.db.tables.RecipeData

@Dao
interface RecipeDataDao {
    @Query("SELECT * from RecipeData")
    fun getAll(): List<RecipeData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(topicData: RecipeData)

    @Query("DELETE from RecipeData")
    fun deleteAll()

    @Update
    fun updateRecipe(recipeData: RecipeData)

    @Query("SELECT * from RecipeData WHERE id = (SELECT MAX(id) from RecipeData)")
    fun getLastRecipe(): RecipeData

    @Query("SELECT * from RecipeData WHERE id = :ID LIMIT 1")
    fun getRecipeWithId(ID: Int): RecipeData

    @Query("SELECT * from RecipeData WHERE recipeUrl = :URL LIMIT 1")
    fun getrecipeWithUrl(URL: String): RecipeData

    @Query("DELETE FROM RecipeData WHERE recipeUrl = :URL")
    fun deleteRecipeWithUrl(URL: String)

    @Query("DELETE FROM RecipeData WHERE id = :ID")
    fun deleteRecipeWithId(ID: Int)
}