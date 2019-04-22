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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nz.co.trademe.techtest.R;
import nz.co.trademe.techtest.data.repository.ItemListRepository;
import nz.co.trademe.techtest.ui.ItemListAdapter;
import nz.co.trademe.techtest.viewmodel.ItemListViewModel;
import nz.co.trademe.wrapper.TradeMeApi;

public class ItemListFragment extends Fragment {
    public static final String CAT_ID_KEY = CategoryFragment.class.getCanonicalName() + "catId";
    private ItemListViewModel viewModel;
    private RecyclerView recyclerView;
    private ItemListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_browser, container, false);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setCancelable(false);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemListAdapter();
        mAdapter.setItemClickListener(this::loadItemFragment);
        recyclerView.setAdapter(mAdapter);
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ItemListViewModel(ItemListRepository.getInstance(new TradeMeApi().get()));
            }
        };
        viewModel = ViewModelProviders.of(this, factory).get(ItemListViewModel.class);
        String catId = null;
        if(getArguments() != null){
            catId = getArguments().getString(CAT_ID_KEY);
        }
        viewModel.init(catId);
        viewModel.getItems().observe(this, mAdapter::addItems);
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
