package nz.co.trademe.techtest.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import nz.co.trademe.techtest.R;
import nz.co.trademe.techtest.data.repository.ItemDetailsRepository;
import nz.co.trademe.techtest.viewmodel.ItemDetailsViewModel;
import nz.co.trademe.wrapper.TradeMeApi;


public class ItemDetailsFragment extends Fragment {
    public static final String LISTING_ID_KEY = ItemDetailsFragment.class.getCanonicalName() + ".listng";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        TextView textView = view.findViewById(R.id.item_name_text);
        ImageView imageView = view.findViewById(R.id.imageView);
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ItemDetailsViewModel(ItemDetailsRepository.getInstance(new TradeMeApi().get()));
            }
        };
        ItemDetailsViewModel viewModel = ViewModelProviders.of(this, factory).get(ItemDetailsViewModel.class);
        String listingId = null;
        if(getArguments() != null){
            listingId = getArguments().getString(LISTING_ID_KEY);
        }
        if(savedInstanceState != null){
            listingId = savedInstanceState.getString(LISTING_ID_KEY);

        }
        viewModel.init(listingId);
        viewModel.getItemDetail().observe(getViewLifecycleOwner(), item -> {
            textView.setText(item.getTitle());
            if(item.getPhotos() != null && item.getPhotos().size() > 0){
                Glide.with(this).load(item.getPhotos().get(0).getValue().component2()).into(imageView);
            } else {
                if(getActivity() != null){
                    getActivity().onBackPressed();
                }
            }

        });
        return view;
    }

}
