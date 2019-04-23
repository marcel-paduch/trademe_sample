package nz.co.trademe.techtest.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    private ProgressDialog progress;

    @Override
    public void onResume() {
        super.onResume();
        progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setCancelable(false);
    }

    protected void showLoading(){
        progress.show();
    }
    protected void dismissLoading(){
        progress.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissLoading();
    }

}
