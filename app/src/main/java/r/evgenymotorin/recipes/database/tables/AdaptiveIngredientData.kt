package r.evgenymotorin.recipes.database.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class AdaptiveIngredientData(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "ingredient") var ingredient: String,
    @ColumnInfo(name = "count") var count: String,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "nextAdaptiveIngredientPtr") var nextAdaptiveIngredientPtr: Int?
) {
    constructor(): this(null, "", "", null, null)
}