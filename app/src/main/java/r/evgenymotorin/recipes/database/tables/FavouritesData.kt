package r.evgenymotorin.recipes.database.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class FavouritesData(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "recipeId") var recipeId: Int?,
    @ColumnInfo(name = "recipeUrl") var recipeUrl: String
) {
    constructor(): this(null, null, "")
}