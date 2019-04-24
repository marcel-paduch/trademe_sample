package nz.co.trademe.techtest.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nz.co.trademe.techtest.R;
import nz.co.trademe.techtest.data.offline.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler View adapter for {@link Category}
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private CategoryClickListener categoryClickListener;
    private List<Category> categoryList = new ArrayList<>();

    public void setCategoryClickListener(CategoryClickListener categoryClickListener) {
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(categoryList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    /**
     * Far from optimal, used to load new data
     */
    public void addItems(List<Category> items) {
        this.categoryList.clear();
        this.categoryList.addAll(items);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView textView;

        private ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.category_name_text);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(categoryClickListener != null && !categoryList.get(getAdapterPosition()).getLeaf()){
                categoryClickListener.onClick(categoryList.get(getAdapterPosition()).getId());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(categoryClickListener != null){
                categoryClickListener.onLongClick(categoryList.get(getAdapterPosition()).getId());
                return true;
            } else {
                return false;
            }

        }
    }

    /**
     * Helper interface for delegating click events
     */
    public interface CategoryClickListener{
        void onClick(String parentId);
        void onLongClick(String parentId);
    }
}
