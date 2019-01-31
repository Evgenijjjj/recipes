package r.evgenymotorin.recipes.database.interfaces

import android.arch.persistence.room.*
import r.evgenymotorin.recipes.database.tables.StepData

@Dao
interface StepDataDao {
    @Query("SELECT * from StepData")
    fun getAll(): List<StepData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stepData: StepData)

    @Query("DELETE from StepData")
    fun deleteAll()

    @Update
    fun updateStepData(stepData: StepData)

    @Query("SELECT * from StepData WHERE id = (SELECT MAX(id) from StepData)")
    fun getLastStepData(): StepData

    @Query("SELECT * from StepData WHERE id = :ID LIMIT 1")
    fun getStepDataWithId(ID: Int): StepData

    @Query("DELETE FROM StepData WHERE id = :ID")
    fun deleteStepDataWithId(ID: Int)
}