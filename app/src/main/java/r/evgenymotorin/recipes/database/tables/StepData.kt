package r.evgenymotorin.recipes.database.tables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class StepData(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "stepDescription") var stepDescription: String?,
    @ColumnInfo(name = "stepImageUrl") var stepImageUrl: String?,
    @ColumnInfo(name = "nextStepDataPtr") var nextStepDataPtr: Int?
) {
    constructor() : this(null, null, null, null)
}