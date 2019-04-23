package nz.co.trademe.techtest.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    private ProgressDialog progress;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setCancelable(false);
    }


    protected void showLoading() {
        if (progress != null) {
            progress.show();
        }

    }

    protected void dismissLoading() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissLoading();
    }

}
