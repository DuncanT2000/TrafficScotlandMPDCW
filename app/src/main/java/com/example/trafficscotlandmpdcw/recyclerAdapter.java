package com.example.trafficscotlandmpdcw;


import static android.content.ContentValues.TAG;
import static java.time.temporal.ChronoUnit.DAYS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

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
        private TextView reasontv;


        public MyViewHolder(final View view){
            super(view);
            context = view.getContext();
            title = view.findViewById(R.id.feed_item_title);
            StartEndDatetv = view.findViewById(R.id.StartEndDatetv);
            viewMoreBtn = view.findViewById(R.id.viewMoreBtn);
            itemType = view.findViewById(R.id.typeTV);
            reasontv = view.findViewById(R.id.reasontv);

        }

    }


    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item,parent,false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {


        if (itemList.get(position).getTitle() != null){
            String itemTitle = itemList.get(position).getTitle().toString();
            Log.d(TAG, "onBindViewHolder: "+ itemTitle);
            holder.title.setText(itemTitle);
        }

        holder.StartEndDatetv.setText( itemList.get(position).getStartDate().toString()+" - " + itemList.get(position).getEndDate().toString());

        LocalDate startDate = LocalDate.parse(itemList.get(position).getStartDate());
        LocalDate endDate = LocalDate.parse(itemList.get(position).getEndDate());

        holder.itemType.setText(itemList.get(position).getItemType());

        holder.reasontv.setText(itemList.get(position).getReason());


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

                showMoreInfoFragment();


            }
            public void showMoreInfoFragment(){

                Intent intent = new Intent(context, ItemActivity.class);


                context.startActivity(intent);



            }

        });




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
