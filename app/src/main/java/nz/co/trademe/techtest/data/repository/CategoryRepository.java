package nz.co.trademe.techtest.data.repository;

import androidx.lifecycle.LiveData;
import nz.co.trademe.techtest.data.offline.model.Category;
import nz.co.trademe.techtest.data.offline.dao.CategoryDao;
import nz.co.trademe.techtest.data.repository.base.BaseRepository;
import nz.co.trademe.wrapper.TradeMeApiService;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class CategoryRepository extends BaseRepository {
    private static CategoryRepository INSTANCE = null;
    private CategoryDao categoryDao;
    private TradeMeApiService tradeMeApi;
    private Executor executor;

    private CategoryRepository(CategoryDao categoryDao, TradeMeApiService tradeMeApi, Executor executor) {
        this.categoryDao = categoryDao;
        this.tradeMeApi = tradeMeApi;
        this.executor = executor;
    }

    public static CategoryRepository getInstance(CategoryDao categoryDao, TradeMeApiService tradeMeApi, Executor executor){
        if(INSTANCE == null){
            INSTANCE = new CategoryRepository(categoryDao,tradeMeApi,executor);
        }
        return INSTANCE;
    }



    public LiveData<List<Category>> getCategories(String parentId) {
        getIsLoading().setValue(true);
        refreshCategoryTree(parentId);
        return parentId == null ? categoryDao.getMainCategories() : categoryDao.getSubcategoriesForId(parentId);
    }

    private void refreshCategoryTree(String parentId) {
        String id = parentId == null ? "0" : parentId;
        executor.execute(() -> {
            try {
                Response<nz.co.trademe.wrapper.models.Category> response = tradeMeApi.getCategory(id).execute();
                if(response.body() == null){
                    return;
                    //should show error
                }
                List<nz.co.trademe.wrapper.models.Category> subCategories = response.body().getSubcategories();
                if(subCategories == null){
                    return;
                }
                List<Category> localCategories = new ArrayList<>();
                for(nz.co.trademe.wrapper.models.Category responseCategory : subCategories){
                    Category category = new Category();
                    category.setId(responseCategory.getId());
                    category.setName(responseCategory.getName());
                    category.setLeaf(responseCategory.isLeaf());
                    category.setParentId(id);
                    localCategories.add(category);
                }
                categoryDao.insertAll(localCategories);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}

