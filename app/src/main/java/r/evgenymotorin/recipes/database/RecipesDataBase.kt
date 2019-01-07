package r.evgenymotorin.recipes.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import dagger.Provides
import r.evgenymotorin.recipes.database.interfaces.RecipeDataDao
import r.evgenymotorin.recipes.db.tables.RecipeData

@Database(entities = arrayOf(RecipeData::class), version = 1)
abstract class RecipesDataBase: RoomDatabase() {

    abstract fun RecipeDataDao(): RecipeDataDao

    companion object {
        private var instance: RecipesDataBase? = null

        fun getInstance(ctx: Context): RecipesDataBase? {
            if (instance == null) {
                synchronized(RecipesDataBase::class) {
                    instance = Room.databaseBuilder(ctx.applicationContext, RecipesDataBase::class.java, "recipes.0.0.1")
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