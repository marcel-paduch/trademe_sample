package nz.co.trademe.techtest.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nz.co.trademe.techtest.R;
import nz.co.trademe.techtest.data.repository.ItemListRepository;
import nz.co.trademe.techtest.ui.ItemListAdapter;
import nz.co.trademe.techtest.viewmodel.ItemListViewModel;
import nz.co.trademe.wrapper.TradeMeApi;

/**
 * This fragment shows list of items in a choosen category
 */
public class ItemListFragment extends BaseFragment {
    public static final String CAT_ID_KEY = CategoryFragment.class.getCanonicalName() + "catId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_browser, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        ItemListAdapter mAdapter = new ItemListAdapter();
        mAdapter.setItemClickListener(this::loadItemFragment);
        recyclerView.setAdapter(mAdapter);
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            @SuppressWarnings("unchecked")
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ItemListViewModel(ItemListRepository.getInstance(new TradeMeApi().get()));
            }
        };
        ItemListViewModel viewModel = ViewModelProviders.of(this, factory).get(ItemListViewModel.class);
        String catId = null;
        if(getArguments() != null){
            catId = getArguments().getString(CAT_ID_KEY);
        }
        viewModel.init(catId);
        viewModel.getIsLoading().observe(this, val -> {
            if (val) {
                showLoading();
            } else {
                dismissLoading();
            }
        });
        viewModel.getItems().observe(this, items -> {
            if(items == null || items.size() == 0){
                showSnackbar(getString(R.string.no_data));
                return;
            }
            mAdapter.addItems(items);

        });
        return view;
    }

    private void loadItemFragment(long listingId){
        Bundle bundle = new Bundle();
        bundle.putString(ItemDetailsFragment.LISTING_ID_KEY, String.valueOf(listingId));
        ItemDetailsFragment itemDetailsFragment = new ItemDetailsFragment();
        itemDetailsFragment.setArguments(bundle);
        if(getFragmentManager() != null && getView() != null) {
            getFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), itemDetailsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

}
