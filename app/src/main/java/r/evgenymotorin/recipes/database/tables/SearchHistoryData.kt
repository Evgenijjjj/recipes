package r.evgenymotorin.recipes.database.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "SearchHistoryData")
data class SearchHistoryData(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "request") var request: String
) {
    constructor() : this(null, "")
}