package nz.co.trademe.techtest.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import nz.co.trademe.techtest.data.repository.ItemListRepository;
import nz.co.trademe.wrapper.models.SearchListing;

import java.util.List;

public class ItemListViewModel extends ViewModel {
    private ItemListRepository repository;
    private MutableLiveData<List<SearchListing>> items;

    public ItemListViewModel(ItemListRepository repository) {
        this.repository = repository;
    }

    public void init(String categoryId){
        if(items == null){
            items = repository.getData(categoryId);
        }
    }

    public MutableLiveData<List<SearchListing>> getItems() {
        return items;
    }
    public MutableLiveData<Boolean> getIsLoading(){
        return repository.getIsLoading();
    }

}
