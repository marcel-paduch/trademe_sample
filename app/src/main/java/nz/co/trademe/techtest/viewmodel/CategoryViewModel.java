package nz.co.trademe.techtest.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import nz.co.trademe.techtest.data.offline.model.Category;
import nz.co.trademe.techtest.data.repository.CategoryRepository;

import java.util.List;
import java.util.Stack;

/**
 * This class exposes repository data and triggers new category fetching
 */
public class CategoryViewModel extends ViewModel {
    private MutableLiveData<String> parentIdFilter = new MutableLiveData<>();
    private LiveData<List<Category>> categories;
    private CategoryRepository repository;
    private Stack<String> categoryBackstack = new Stack<>();

    public CategoryViewModel(CategoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Inits viewModel, switchmap will trigger getCategories with value from parentIdFilter
     * every time item is emitted
     */
    public void init() {
        if (categories == null) {
            categoryBackstack.push(null);
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

    public Stack<String> getCategoryBackstack() {
        return categoryBackstack;
    }

    /**
     * Emit new value to parentIdFilter
     * @param parentId parent category id
     */
    public void parentIdChanged(String parentId) {
        parentIdFilter.setValue(parentId);
    }
}
