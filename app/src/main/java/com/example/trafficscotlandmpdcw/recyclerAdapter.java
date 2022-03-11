package com.example.trafficscotlandmpdcw;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private static final String TAG = "recyclerAdapter";

    private ArrayList<Item> itemList = new ArrayList<Item>();

    public recyclerAdapter(ArrayList<Item> itemList){
        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView description;
        private TextView link;
        private TextView georss;
        private TextView pubDate;
        private TextView itemType;


        public MyViewHolder(final View view){
            super(view);
            title = view.findViewById(R.id.feed_item_title);
            description = view.findViewById(R.id.feed_item_description);
            pubDate = view.findViewById(R.id.tvpubDate);
            itemType = view.findViewById(R.id.tvitemtype);

            title.setOnClickListener( new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Please Confirm your order: ");
                    Toast.makeText(view.getContext(), "Order has not been submitted!", Toast.LENGTH_SHORT).show();

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });


        }

    }


    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String itemTitle = itemList.get(position).getTitle().toString();
        Log.d(TAG, "onBindViewHolder: "+ itemTitle);
        holder.title.setText(itemTitle);

        String itemDescription = itemList.get(position).getDescription().toString();
        holder.description.setText(itemDescription);
        if (itemList.get(position).getPubDate() != null){
            String itemPubDate = itemList.get(position).getPubDate();
            Log.d(TAG, "onBindViewHolder: " + itemPubDate.toString());
            holder.pubDate.setText(itemPubDate.toString());
        }
        if (itemList.get(position).getItemType() != null){
            String itemTypestr = itemList.get(position).getItemType();
            Log.d(TAG, "onBindViewHolder: " + itemTypestr.toString());
            holder.itemType.setText(itemTypestr.toString());
        }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
