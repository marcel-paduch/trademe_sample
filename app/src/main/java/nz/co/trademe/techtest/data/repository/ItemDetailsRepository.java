package nz.co.trademe.techtest.data.repository;

import nz.co.trademe.techtest.data.repository.base.LruCacheRepository;
import nz.co.trademe.wrapper.TradeMeApiService;
import nz.co.trademe.wrapper.models.ListedItemDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsRepository extends LruCacheRepository<ListedItemDetail> {
    private static ItemDetailsRepository INSTANCE = null;
    private ItemDetailsRepository(TradeMeApiService tradeMeApi) {
        this.tradeMeApi = tradeMeApi;
    }

    public static ItemDetailsRepository getInstance(TradeMeApiService tradeMeApi) {
        if (INSTANCE == null)
            INSTANCE = new ItemDetailsRepository(tradeMeApi);

        return INSTANCE;
    }

    @Override
    protected void refreshItems(String id) {
        tradeMeApi.getListing(Long.valueOf(id)).enqueue(new Callback<ListedItemDetail>() {
            @Override
            public void onResponse(Call<ListedItemDetail> call, Response<ListedItemDetail> response) {
                ListedItemDetail itemDetail = response.body();
                cache.put(id, itemDetail);
                emitFromCache(id);
            }

            @Override
            public void onFailure(Call<ListedItemDetail> call, Throwable t) {

            }
        });
    }
}
