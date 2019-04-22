package nz.co.trademe.techtest.data.repository;

import nz.co.trademe.techtest.data.repository.base.LruCacheRepository;
import nz.co.trademe.wrapper.TradeMeApiService;
import nz.co.trademe.wrapper.models.SearchCollection;
import nz.co.trademe.wrapper.models.SearchListing;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;

/**
 * This class combines data from local and remote source.
 */
public class ItemListRepository extends LruCacheRepository<List<SearchListing>> {
    private static ItemListRepository INSTANCE = null;
    private static final String CATEGORY = "category";
    private static final String ROWS = "rows";
    private static final String MAX_RESULT = "20";

    private ItemListRepository(TradeMeApiService tradeMeApi) {
        this.tradeMeApi = tradeMeApi;
    }

    public static ItemListRepository getInstance(TradeMeApiService tradeMeApi) {
        if (INSTANCE == null)
            INSTANCE = new ItemListRepository(tradeMeApi);

        return INSTANCE;
    }


    @Override
    protected void refreshItems(String id) {
        HashMap<String, String> queryMap = new HashMap<>(1);
        queryMap.put(CATEGORY, id);
        queryMap.put(ROWS, MAX_RESULT);
        tradeMeApi.generalSearch(queryMap).enqueue(new Callback<SearchCollection>() {
            @Override
            public void onResponse(Call<SearchCollection> call, Response<SearchCollection> response) {
                SearchCollection searchCollection = response.body();
                if(searchCollection != null){
                    cache.put(id, searchCollection.getList());
                    emitFromCache(id);
                }
            }

            @Override
            public void onFailure(Call<SearchCollection> call, Throwable t) {

            }
        });
    }

}
