package nz.co.trademe.techtest;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import nz.co.trademe.techtest.data.repository.CategoryRepository;
import nz.co.trademe.techtest.viewmodel.CategoryViewModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    CategoryRepository repository;
    @InjectMocks
    private CategoryViewModel viewModel;

    @Before
    public void up(){
        viewModel.init();
    }

    @Test
    public void testIsLoadingTrigger(){
        MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
        isLoading.postValue(true);
        when(repository.getIsLoading()).thenReturn(isLoading);
        assert viewModel.getIsLoading() != null;
        assert  viewModel.getIsLoading().getValue();
        isLoading.postValue(false);
        assert  !viewModel.getIsLoading().getValue();
    }



}
