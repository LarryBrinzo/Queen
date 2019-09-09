package com.queen.kartish.queen.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.queen.kartish.queen.ProductDescriptionActivity;
import com.queen.kartish.queen.ProductModel;
import com.queen.kartish.queen.R;

import java.util.List;

public class ProductAdAdapter extends RecyclerView.Adapter<ProductAdAdapter.MyHoder>{

    private List<ProductModel> list;
    private Context context;
    private FirebaseUser fuser;
    private DatabaseReference ref;
    private String userid;

    public ProductAdAdapter(List<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        fuser = firebaseAuth.getCurrentUser();

        if(fuser!=null)
            userid= firebaseAuth.getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public MyHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.vertical_adview_card,parent,false);
        return new MyHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHoder holder, @SuppressLint("RecyclerView") final int position) {
        final ProductModel mylist = list.get(position);
        holder.price.setText(mylist.getPrice());
        holder.adtitle.setText(mylist.getTitle());
        holder.brand.setText(mylist.getBrand());

        Glide.with(context).asBitmap().load(mylist.getUrl1()).into(holder.image1);

        if(list.get(position).getWish().equals("1"))
        {
            holder.wishr.setVisibility(View.VISIBLE);
            holder.wish.setVisibility(View.GONE);
        }

        else
        {
            holder.wish.setVisibility(View.VISIBLE);
            holder.wishr.setVisibility(View.GONE);
        }


        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, ProductDescriptionActivity.class);
                intent.putExtra("product_no", list.get(position).getProductno());
                context.startActivity(intent);
            }
        });

        holder.wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fuser==null)
                {
//                    Intent intent=new Intent(context,LoginSignupactivity.class);
//                    context.startActivity(intent);
                }

                else
                {
                    ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("wish").child(list.get(position).getProductno());

                    holder.wishr.setVisibility(View.VISIBLE);
                    holder.wish.setVisibility(View.GONE);
                    Toast.makeText(context, "Added to Wishlist", Toast.LENGTH_SHORT).show();

                    ref.setValue(list.get(position).getProductno());
                }

            }
        });

        holder.wishr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fuser==null)
                {
//                    Intent intent=new Intent(context,LoginSignupactivity.class);
//                    context.startActivity(intent);

                }

                else
                {
                    ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("wish").child(list.get(position).getProductno());

                    ref.setValue(null);
                    holder.wish.setVisibility(View.VISIBLE);
                    holder.wishr.setVisibility(View.GONE);

                }
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
        TextView price,adtitle,brand;//,sold;
        ImageView image1;
        LinearLayout card;
        ImageButton wish,wishr;


        MyHoder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            adtitle= itemView.findViewById(R.id.addetails);
            brand=itemView.findViewById(R.id.brand);
            image1 =itemView.findViewById(R.id.image);
            card=itemView.findViewById(R.id.card_view);
            wish=itemView.findViewById(R.id.wish);
            wishr=itemView.findViewById(R.id.wishr);
            // sold=itemView.findViewById(R.id.sold);
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


