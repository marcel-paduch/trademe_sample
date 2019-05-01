package nz.co.trademe.techtest;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.*;
import nz.co.trademe.techtest.data.offline.model.Category;
import nz.co.trademe.techtest.data.repository.CategoryRepository;
import nz.co.trademe.techtest.viewmodel.CategoryViewModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    CategoryRepository repository;
    @InjectMocks
    private CategoryViewModel viewModel;

    @Before
    public void up() {
        viewModel.init();
    }

    @Test
    public void testIsLoadingTrigger() {
        MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
        isLoading.postValue(true);
        when(repository.getIsLoading()).thenReturn(isLoading);
        assert viewModel.getIsLoading() != null;
        assert viewModel.getIsLoading().getValue();
        isLoading.postValue(false);
        assert !viewModel.getIsLoading().getValue();
    }

    @Test
    public void testLoadNewCategory() {
        List<Category> list = new ArrayList<>();
        viewModel.getCategories().observe(() -> {
            LifecycleRegistry lifecycle = new LifecycleRegistry(mock(LifecycleOwner.class));
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            return lifecycle;
        }, list::addAll);
        verify(repository).getCategories(null);
        String TEST_VAL = "asd";
        viewModel.parentIdChanged(TEST_VAL);
        verify(repository).getCategories(TEST_VAL);
    }


}
