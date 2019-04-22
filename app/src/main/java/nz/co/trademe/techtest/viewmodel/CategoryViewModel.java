package nz.co.trademe.techtest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import nz.co.trademe.techtest.data.offline.model.Category;
import nz.co.trademe.techtest.data.repository.CategoryRepository;

import java.util.List;

/**
 * This class exposes repository data and trigger's
 */
public class CategoryViewModel extends ViewModel {
    private MutableLiveData<String> parentIdFilter = new MutableLiveData<>();
    private LiveData<List<Category>> categories;
    private CategoryRepository repository;

    public CategoryViewModel(CategoryRepository repository) {
        this.repository = repository;
    }

    public void init() {
        if (categories == null) {
            parentIdFilter.setValue(null);
            categories = Transformations.switchMap(parentIdFilter, repository::getCategories);
        }

    }

    public MutableLiveData<Boolean> getIsLoading(){
        return repository.getIsLoading();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void parentIdChanged(String parentId) {
        parentIdFilter.setValue(parentId);
    }
}
