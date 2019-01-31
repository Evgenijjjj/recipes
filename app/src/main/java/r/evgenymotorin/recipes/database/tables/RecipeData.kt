package r.evgenymotorin.recipes.db.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "RecipeData")
data class RecipeData(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "recipeUrl") var recipeUrl: String,
    @ColumnInfo(name = "imageUrl") var imageUrl: String?,
    @ColumnInfo(name = "recipeName") var recipeName: String,
    @ColumnInfo(name = "numberOfServings") var numberOfServings: String,
    @ColumnInfo(name = "cookingTime") var cookingTime: String,
    @ColumnInfo(name = "ingredientsCount") var ingredientsCount: String,
    @ColumnInfo(name = "firstStepPtr") var firstStepPtr: Int?,
    @ColumnInfo(name = "aboutDescription") var aboutDescription: String?,
    @ColumnInfo(name = "firstImgAboutPtr") var firstImgAboutPtr: Int?,
    @ColumnInfo(name = "adaptiveIngredientFlag") var adaptiveIngredientFlag: Int?,
    @ColumnInfo(name = "adaptiveIngredientsTitle") var adaptiveIngredientsTitle: String?,
    @ColumnInfo(name = "notAdaptiveIngredientsText") var notAdaptiveIngredientsText: String?,
    @ColumnInfo(name = "firstAdaptiveIngredientPtr") var firstAdaptiveIngredientPtr: Int?


) {
   /* @Ignore
    constructor(recipeUrl: String, imageUrl: String?, recipeName: String, numberOfServings: String, cookingTime: String, ingredientsCount: String):
            this(null, recipeUrl, imageUrl, recipeName, numberOfServings, cookingTime, ingredientsCount,
                null, null, null)*/

    constructor() : this(null, "", null, "",
        "", "", "", null,
        null,null, null, null,
        null, null)
}