package nz.co.trademe.techtest.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nz.co.trademe.techtest.R;
import nz.co.trademe.wrapper.models.SearchListing;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler View adapter for {@link SearchListing}
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder>{
    private ItemListAdapter.ItemClickListener itemClickListener;
    private List<SearchListing> items = new ArrayList<>();

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new ItemListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemListAdapter.ViewHolder holder, int position) {
        holder.textView.setText(items.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<SearchListing> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    public void clear(){
        this.items.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;

        private ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.category_name_text);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener != null ){
                itemClickListener.onClick(items.get(getAdapterPosition()).getListingId());
            }
        }


    }

    public interface ItemClickListener{
        void onClick(long parentId);
    }
}
