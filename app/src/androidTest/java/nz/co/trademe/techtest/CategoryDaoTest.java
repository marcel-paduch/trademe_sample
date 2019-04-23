package nz.co.trademe.techtest;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import nz.co.trademe.techtest.data.offline.AppDatabase;
import nz.co.trademe.techtest.data.offline.dao.CategoryDao;
import nz.co.trademe.techtest.data.offline.model.Category;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CategoryDaoTest {
    private CategoryDao categoryDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        categoryDao = db.categoryDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void testInsertAndGetMainCategories() {
       insertList();
       List<Category> categories = categoryDao.getMainCategories().getValue();
       assert categories != null;
       assert(categories.size() == 3);
    }

    private void insertList(){
        List<Category> list = new ArrayList<>();
        list.add(createCategory("1", true));
        list.add(createCategory("2", true));
        list.add(createCategory("3", true));
        list.add(createCategory("4", false));
        list.add(createCategory("5", false));
        categoryDao.insertAll(list);
    }
    private Category createCategory(String id, Boolean isMainCategory){
        Category category = new Category();
        category.setId(id);
        category.setMainCategory(isMainCategory);
        return category;
    }
}
