package com.attinder.weatherdemoapp.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.attinder.weatherdemoapp.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private String[] mDataset;

    // Provide a reference to the com.attinder.weatherdemoapp.views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the com.attinder.weatherdemoapp.views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
         CardView cv;
        TextView title;

         MyViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.cardView);
            title = v.findViewById(R.id.title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new com.attinder.weatherdemoapp.views (invoked by the layout manager)
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
