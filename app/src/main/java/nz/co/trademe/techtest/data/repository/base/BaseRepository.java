package nz.co.trademe.techtest.data.repository.base;

import androidx.lifecycle.MutableLiveData;

/**
 * Base repository class, exposes loading indicator, should also expose network response status
 * All repositories follow single source of truth principle
 * All repository classes should be singletons
 */
public abstract class BaseRepository {
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }


}
