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
 * This class combines item list data from local and remote source.
 * Shows first 20 items for specified category.
 * Uses in memory cache, due to dynamic nature of the data.
 */
public class ItemListRepository extends LruCacheRepository<List<SearchListing>> {
    private static ItemListRepository INSTANCE = null;
    //Query parameters for limiting search results only to first 20 rows of specific category
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


    /**
     * This method gets first 20 items from the given category and store these items in cache
     * No error handling
     * @param id category id
     */
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
                } else {
                    getIsLoading().postValue(false);
                }
            }

            @Override
            public void onFailure(Call<SearchCollection> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
