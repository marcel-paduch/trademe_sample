package nz.co.trademe.techtest.data.repository.base;

import android.util.Log;
import android.util.LruCache;
import androidx.lifecycle.MutableLiveData;
import nz.co.trademe.techtest.data.repository.ItemListRepository;
import nz.co.trademe.wrapper.TradeMeApiService;

/**
 * Generic repository that has built in LRU cache of size 1MiB
 * It will show cached data if available before doing network requests
 * @param <T>
 */
public abstract class LruCacheRepository<T> extends BaseRepository {
    private static int CACHE_SIZE =  100;
    protected TradeMeApiService tradeMeApi;
    protected LruCache<String, T> cache = new LruCache<>(CACHE_SIZE);
    protected MutableLiveData<T> data = new MutableLiveData<>();


    /**
     * This method sets loading indicator to true, emit data from LRU cache and triggers network operation
     * @return liveData
     */
    public MutableLiveData<T> getData(String id){
        getIsLoading().setValue(true);
        emitFromCache(id);
        refreshItems(id);
        return data;
    }

    /**
     * This method will post cached items to liveData stream if available, otherwise doesn't do anything
     */
    protected void emitFromCache(String id) {
        if(cache.get(id) != null){
            Log.d(ItemListRepository.class.getName(), "Fetching data from cache for id="+id);
            data.postValue(cache.get(id));
        }
    }

    /**
     * This abstract method should perform network request.
     * It is important to manually save new data to cache and call method emitFromCache(id)
     */
     abstract protected void refreshItems(String id);
}
