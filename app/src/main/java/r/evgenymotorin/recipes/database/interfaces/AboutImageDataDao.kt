package r.evgenymotorin.recipes.database.interfaces

import android.arch.persistence.room.*
import r.evgenymotorin.recipes.database.tables.AboutImageData

@Dao
interface AboutImageDataDao {
    @Query("SELECT * from AboutImageData")
    fun getAll(): List<AboutImageData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(aboutImageData: AboutImageData)

    @Query("DELETE from AboutImageData")
    fun deleteAll()

    @Update
    fun updateAboutImageData(aboutImageData: AboutImageData)

    @Query("SELECT * from AboutImageData WHERE id = (SELECT MAX(id) from AboutImageData)")
    fun getLastAboutImageData(): AboutImageData

    @Query("SELECT * from AboutImageData WHERE id = :ID LIMIT 1")
    fun getAboutImageDataWithId(ID: Int): AboutImageData

    @Query("DELETE FROM AboutImageData WHERE id = :ID")
    fun deleteAboutImageDataWithId(ID: Int)
}