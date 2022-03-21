package com.example.trafficscotlandmpdcw;


import static java.time.temporal.ChronoUnit.DAYS;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trafficscotlandmpdcw.Fragments.HomeFragment;
import com.example.trafficscotlandmpdcw.Fragments.ItemFragment;

import java.time.LocalDate;
import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private static final String TAG = "recyclerAdapter";

    private Context context;
    private ArrayList<Item> itemList = new ArrayList<Item>();


    public recyclerAdapter(ArrayList<Item> itemList){

        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView StartEndDatetv;
        private TextView viewMoreBtn;
        private TextView description;
        private TextView link;
        private TextView georss;
        private TextView pubDate;
        private TextView itemType;
        private TextView trafficMantv;
        private TextView worktv;
        private TextView reasontv;
        private TextView duration;



        public MyViewHolder(final View view){
            super(view);
            context = view.getContext();
            title = view.findViewById(R.id.feed_item_title);
            StartEndDatetv = view.findViewById(R.id.StartEndDatetv);
            viewMoreBtn = view.findViewById(R.id.viewMoreBtn);
            itemType = view.findViewById(R.id.typeTV);
            trafficMantv = view.findViewById(R.id.trafficMantv);
            worktv = view.findViewById(R.id.worktv);
            reasontv = view.findViewById(R.id.reasontv);
            description = view.findViewById(R.id.currentDescription);

            duration = view.findViewById(R.id.durationtv);

        }

    }


    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (itemList.get(position).getItemType() == "planned"){
            if (itemList.get(position).getTitle() != null){
                String itemTitle = itemList.get(position).getTitle().toString();
                Log.d(TAG, "onBindViewHolder: "+ itemTitle);
                holder.title.setText("Title: " + itemTitle);
            }

            holder.StartEndDatetv.setText( itemList.get(position).getStartDate().toString()+" - " + itemList.get(position).getEndDate().toString());

            LocalDate startDate = LocalDate.parse(itemList.get(position).getStartDate());
            LocalDate endDate = LocalDate.parse(itemList.get(position).getEndDate());

            holder.itemType.setText(itemList.get(position).getItemType());

            holder.worktv.setText("Work: " +itemList.get(position).getWork());
            holder.trafficMantv.setText("Traffic Management: " +itemList.get(position).getTrafficManagement());



            long days = DAYS.between(startDate, endDate);

            if (days >=1 && days <=2){
                holder.viewMoreBtn.setBackgroundColor(context.getResources().getColor(R.color.green));
            }else if (days >=3 && days <=5){
                holder.viewMoreBtn.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            }
            else if (days >=6){
                holder.viewMoreBtn.setBackgroundColor(context.getResources().getColor(R.color.red));
            }


            holder.viewMoreBtn.setText("View More");

            holder.viewMoreBtn.setOnClickListener( new View.OnClickListener()
            {
                public void onClick(View view)
                {

                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    ItemFragment itemFragment = new ItemFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("itemPos", position);
                    itemFragment.setArguments(bundle);

                    transaction.replace(MainActivity.pageFragment.getId(), itemFragment);
                    transaction.commit();

                }});

        }

        if (itemList.get(position).getItemType() == "current"){

            if (itemList.get(position).getTitle() != null){
                String itemTitle = itemList.get(position).getTitle().toString();
                Log.d(TAG, "onBindViewHolder: "+ itemTitle);
                holder.title.setText("Title: " + itemTitle);
            }

            if (itemList.get(position).getDescription() != null){
                String itemDescription = itemList.get(position).getDescription().toString();
                Log.d(TAG, "onBindViewHolder: "+ itemDescription);
                holder.description.setText("Description: " + itemDescription);
            }


        }

        if (itemList.get(position).getItemType() == "roadworks"){

            if (itemList.get(position).getTitle() != null){
                String itemTitle = itemList.get(position).getTitle().toString();
                Log.d(TAG, "onBindViewHolder: "+ itemTitle);
                holder.title.setText("Title: " + itemTitle);
            }

            if (itemList.get(position).getStartDate() != null && itemList.get(position).getEndDate() != null){
                String itemStartDate = itemList.get(position).getStartDate().toString();
                String itemEndDate = itemList.get(position).getEndDate().toString();

                holder.duration.setText("" +itemStartDate + " - " + itemEndDate );

            }

            holder.viewMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();

                    ItemFragment itemFragment = new ItemFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("itemPos", position);
                    itemFragment.setArguments(bundle);

                    transaction.replace(MainActivity.pageFragment.getId(), itemFragment);
                    transaction.commit();
                }
            });


        }









    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(final int position) {

        if (itemList.get(position).getItemType() == "planned"){
            return R.layout.feed_item;
        }else if (itemList.get(position).getItemType() == "current"){
            return R.layout.current_item;
        }
        else if (itemList.get(position).getItemType() == "roadworks"){
           return R.layout.roadworks_item;
        }else{
            return R.layout.feed_item;
        }


    }


}
