package nz.co.trademe.techtest.data.offline.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import nz.co.trademe.techtest.data.offline.model.Category;

import java.util.List;

/**
 * Data class used to access Category data stored in SQL database
 */
@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);

    /**
     *
     * @return Returns root categories
     */
    @Query("SELECT * FROM Category WHERE parentId = 0")
    LiveData<List<Category>> getMainCategories();

    /**
     * Every non root category is treated as subcategory
     */
    @Query("SELECT * FROM Category WHERE parentId = :parentId")
    LiveData<List<Category>> getSubcategoriesForId(String parentId);
}
