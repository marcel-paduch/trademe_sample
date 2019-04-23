package nz.co.trademe.techtest.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import nz.co.trademe.techtest.data.repository.ItemDetailsRepository;
import nz.co.trademe.wrapper.models.ListedItemDetail;

/**
 * ViewModel for itemDetails
 */
public class ItemDetailsViewModel extends ViewModel {
    private ItemDetailsRepository itemDetailsRepository;
    private MutableLiveData<ListedItemDetail> itemDetail;

    public ItemDetailsViewModel(ItemDetailsRepository itemDetailsRepository) {
        this.itemDetailsRepository = itemDetailsRepository;
    }

    public void init(String listingId) {
        if (itemDetail == null) {
            itemDetail = itemDetailsRepository.getData(listingId);
        }
    }

    public MutableLiveData<ListedItemDetail> getItemDetail() {
        return itemDetail;
    }
}
