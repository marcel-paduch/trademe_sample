package nz.co.trademe.techtest;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import nz.co.trademe.techtest.data.offline.dao.CategoryDao;
import nz.co.trademe.techtest.data.offline.model.Category;
import nz.co.trademe.techtest.data.repository.CategoryRepository;
import nz.co.trademe.wrapper.TradeMeApiService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.mock.Calls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CategoryRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private TradeMeApiService tradeMeApi;
    @Mock
    private CategoryDao categoryDao;
    private CategoryRepository repository;

    @Before
    public void up() {
        MockitoAnnotations.initMocks(this);
        repository = CategoryRepository.getInstance(categoryDao, tradeMeApi, Executors.newSingleThreadExecutor());
    }


    @Test
    public void getCategoriesTest() {
        List<nz.co.trademe.wrapper.models.Category> subcategories = new ArrayList<>();
        subcategories.add(createCategory("1", "asd", null));
        subcategories.add(createCategory("2", "ae", null));
        subcategories.add(createCategory("3", "b", null));
        when(tradeMeApi.getCategory(any())).thenReturn(Calls.response(createCategory("0", "ro0t", subcategories)));
        repository.getCategories(null);
        verify(categoryDao).insertAll(anyList());
        verify(categoryDao).getMainCategories();
        verify(tradeMeApi).getCategory("0");
        assert repository.getIsLoading().getValue();

    }

    private nz.co.trademe.wrapper.models.Category createCategory(String id, String name,
                                                                 List<nz.co.trademe.wrapper.models.Category> categories) {
        return new nz.co.trademe.wrapper.models.Category(id, name, true, categories);
    }
}
