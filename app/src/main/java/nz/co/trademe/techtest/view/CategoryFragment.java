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
import nz.co.trademe.techtest.data.offline.AppDatabase;
import nz.co.trademe.techtest.data.repository.CategoryRepository;
import nz.co.trademe.techtest.ui.CategoryAdapter;
import nz.co.trademe.techtest.viewmodel.CategoryViewModel;
import nz.co.trademe.wrapper.TradeMeApi;

import java.util.concurrent.Executors;

/**
 * This fragment allows user to browse categories
 */
public class CategoryFragment extends BaseFragment {
    private CategoryViewModel viewModel;
    private RecyclerView recyclerView;
    private CategoryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_browser, container, false);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CategoryAdapter();
        mAdapter.setCategoryClickListener(new CategoryAdapter.CategoryClickListener() {
            @Override
            public void onClick(String parentId) {
                viewModel.getCategoryBackstack().push(parentId);
                viewModel.parentIdChanged(parentId);
            }

            @Override
            public void onLongClick(String parentId) {
                loadNewFragment(parentId);
            }
        });
        recyclerView.setAdapter(mAdapter);
        //Custom factory to handle arguments for viewmodel class
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            @SuppressWarnings("unchecked")
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CategoryViewModel(CategoryRepository.getInstance(AppDatabase.getInstance(getContext()).categoryDao(),
                        new TradeMeApi().get(), Executors.newSingleThreadExecutor()));
            }
        };
        viewModel = ViewModelProviders.of(this, factory).get(CategoryViewModel.class);
        viewModel.init();
        viewModel.getIsLoading().observe(this, val -> {
            if (val) {
                showLoading();
            } else {
                dismissLoading();
            }
        });
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            dismissLoading();
            if (categories != null && categories.size() > 0) {
                mAdapter.addItems(categories);
            }
        });
        return view;
    }

    private void loadNewFragment(String catId) {
        Bundle bundle = new Bundle();
        bundle.putString(ItemListFragment.CAT_ID_KEY, catId);
        ItemListFragment itemListFragment = new ItemListFragment();
        itemListFragment.setArguments(bundle);
        if (getFragmentManager() != null && getView() != null) {
            getFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), itemListFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    /**
     * This method will remove last category id from the stack and load next category from stack
     * @return true if backstack is empty, false otherwise
     */
    public boolean popCategoryStack(){
        viewModel.getCategoryBackstack().pop();
        viewModel.parentIdChanged(viewModel.getCategoryBackstack().peek());
        return viewModel.getCategoryBackstack().empty();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }
}
