package com.queen.kartish.queen.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.queen.kartish.queen.Home.Searching.ProductDisplayActivity;
import com.queen.kartish.queen.R;

import java.util.List;

public class HorizontalDealsAdapter extends RecyclerView.Adapter<HorizontalDealsAdapter.MyHoder>{

    private List<Pair<String,String>> list;
    private Context context;

    HorizontalDealsAdapter(List<Pair<String,String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_view_cards2,parent,false);
        return new MyHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHoder holder, @SuppressLint("RecyclerView") final int position) {

        Glide.with(context).asBitmap().load(list.get(position).second).into(holder.image1);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, ProductDisplayActivity.class);
                intent.putExtra("searchstring", list.get(position).first);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try{
            if(list.size()==0){

                arr = 0;

            }
            else{

                arr=list.size();
            }



        }catch (Exception ignored){



        }

        return arr;

    }

    class MyHoder extends RecyclerView.ViewHolder{

        ImageView image1;
        LinearLayout card;

        MyHoder(View itemView) {
            super(itemView);
            image1 =itemView.findViewById(R.id.image);
            card=itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}


