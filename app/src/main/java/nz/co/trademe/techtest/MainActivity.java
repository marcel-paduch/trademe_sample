package nz.co.trademe.techtest;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import nz.co.trademe.techtest.view.CategoryFragment;

/**
 * Main entry point to the app, loads list of categories fragment
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment, new CategoryFragment(), CategoryFragment.class.getName())
                    .commit();
        }
    }

    /**
     * This method checks if the backstack is empty which means that we are at the category screen and if so
     * it will try and call {@link CategoryFragment} popCategoryStack() method to try and change category to previous
     * in other cases or if the popCategoryStack() returns true it will continue as normal
     */
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
           Fragment fragment =  getSupportFragmentManager().findFragmentByTag(CategoryFragment.class.getName());
           if( fragment instanceof CategoryFragment){
               if(((CategoryFragment) fragment).popCategoryStack()){
                   super.onBackPressed();
               } else {
                   return;
               }
           }

        }
        super.onBackPressed();
    }
}
