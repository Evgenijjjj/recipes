package r.evgenymotorin.recipes.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import dagger.Provides
import r.evgenymotorin.recipes.database.interfaces.*
import r.evgenymotorin.recipes.database.tables.AboutImageData
import r.evgenymotorin.recipes.database.tables.AdaptiveIngredientData
import r.evgenymotorin.recipes.database.tables.FavouritesData
import r.evgenymotorin.recipes.database.tables.StepData
import r.evgenymotorin.recipes.db.tables.RecipeData

@Database(entities = [RecipeData::class, StepData::class, AboutImageData::class, AdaptiveIngredientData::class, FavouritesData::class], version = 1)
abstract class RecipesDataBase: RoomDatabase() {

    abstract fun RecipeDataDao(): RecipeDataDao
    abstract fun StepDataDao(): StepDataDao
    abstract fun AboutImageDataDao(): AboutImageDataDao
    abstract fun AdaptiveIngredientDataDao(): AdaptiveIngredientDataDao
    abstract fun FavouritesDataDao(): FavouritesDataDao

    companion object {
        private var instance: RecipesDataBase? = null

        fun getInstance(ctx: Context): RecipesDataBase? {
            if (instance == null) {
                synchronized(RecipesDataBase::class) {
                    instance = Room.databaseBuilder(ctx.applicationContext, RecipesDataBase::class.java, "recipes.0.0.3")
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}