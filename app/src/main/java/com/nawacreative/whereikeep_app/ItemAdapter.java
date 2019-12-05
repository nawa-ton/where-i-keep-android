package com.nawacreative.whereikeep_app;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> implements Filterable {
    private ItemViewModel itemViewModel;

    private List<Item> items = new ArrayList<>();

    private OnItemClickListener listener;

    ItemAdapter(ItemViewModel itemViewModel){
        this.itemViewModel = itemViewModel;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ItemHolder(itemView);
    }

    //getting data from single Item of java object into the ItemHolder
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
        Item currentItem = items.get(position);
        itemHolder.textViewID.setText(String.format("ID %d", currentItem.getId()));
        itemHolder.textViewName.setText(currentItem.getItemName());
        itemHolder.textViewLocation.setText(currentItem.getStoragelocation());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public Item getItemAt(int position){
        return items.get(position);
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView textViewID;
        private TextView textViewName;
        private TextView textViewLocation;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.textView_id);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewLocation = itemView.findViewById(R.id.textView_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(items.get(position));
                    }

                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item> itemsFull = itemViewModel.fetchAllItems();
            List<Item> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(itemsFull);
            }else {
                String filterpattern = constraint.toString().toLowerCase().trim();

                for(Item item : itemsFull){
                    if(item.getItemName().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
