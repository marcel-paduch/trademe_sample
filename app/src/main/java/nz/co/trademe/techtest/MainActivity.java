package nz.co.trademe.techtest;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import nz.co.trademe.techtest.view.CategoryFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new CategoryFragment()).commit();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
