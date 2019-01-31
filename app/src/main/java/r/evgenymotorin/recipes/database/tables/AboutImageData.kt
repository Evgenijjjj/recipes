package r.evgenymotorin.recipes.database.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class AboutImageData(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "imageUrl") var imageUrl: String?,
    @ColumnInfo(name = "nextImageDataPtr") var nextImageDataPtr: Int?
) {
    constructor(): this(null, null, null)
}