package nz.co.trademe.techtest.view;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import nz.co.trademe.techtest.R;
import nz.co.trademe.techtest.data.repository.ItemDetailsRepository;
import nz.co.trademe.techtest.viewmodel.ItemDetailsViewModel;
import nz.co.trademe.wrapper.TradeMeApi;

/**
 * This fragment shows detailed item listing
 */
public class ItemDetailsFragment extends BaseFragment {
    public static final String LISTING_ID_KEY = ItemDetailsFragment.class.getCanonicalName() + ".listng";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        TextView textView = view.findViewById(R.id.item_name_text);
        ImageView imageView = view.findViewById(R.id.imageView);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.start();
        ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            @SuppressWarnings("unchecked")
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
        viewModel.getIsLoadig().observe(getViewLifecycleOwner(), val -> {
            if(val){
                showLoading();
            } else {
                dismissLoading();
            }
        });
        viewModel.getItemDetail().observe(getViewLifecycleOwner(), item -> {
            textView.setText(item.getTitle());
            if(item.getPhotos() != null && item.getPhotos().size() > 0){
                Glide
                        .with(this)
                        .load(item.getPhotos().get(0).getValue().component2())
                        .placeholder(circularProgressDrawable)
                        .into(imageView);
            } else {
                showSnackbar(getString(R.string.no_data));
            }

        });
        return view;
    }

}
