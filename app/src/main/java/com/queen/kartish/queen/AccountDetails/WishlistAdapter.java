package com.queen.kartish.queen.AccountDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
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

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyHoder>{

    private List<ProductModel> list;
    private Context context;
    private FirebaseUser fuser;
    private DatabaseReference ref;
    private String userid;
    String sizes;
    int sizesel=0;

    public WishlistAdapter(List<ProductModel> list, Context context) {

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

        View view = LayoutInflater.from(context).inflate(R.layout.wishlistcard,parent,false);
        return new MyHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHoder holder, @SuppressLint("RecyclerView") final int position) {
        final ProductModel mylist = list.get(position);
        holder.price.setText(mylist.getPrice());
        holder.adtitle.setText(mylist.getTitle());
        holder.brand.setText(mylist.getBrand());

        Glide.with(context).asBitmap().load(mylist.getUrl1()).into(holder.image1);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, ProductDescriptionActivity.class);
                intent.putExtra("product_no", list.get(position).getProductno());
                context.startActivity(intent);
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("wish").child(list.get(position).getProductno());
                ref.setValue(null);

                list.remove(position);
            }
        });

        holder.bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WishlistActivity.popup.setVisibility(View.VISIBLE);
                WishlistActivity.popup.setBackgroundColor(Color.parseColor("#80000000"));
                slideUp(WishlistActivity.layoutsec);

                sizeSelect();

            }
        });

        WishlistActivity.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistActivity.popup.setVisibility(View.GONE);
                slideDown(WishlistActivity.layoutsec);
                WishlistActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
            }
        });

        WishlistActivity.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sizesel==1){

                WishlistActivity.popup.setBackgroundColor(Color.parseColor("#80000000"));
                slideUp(WishlistActivity.layoutsec);

                sizeSelect();

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+sizes);
                ref.child("product_no").setValue(list.get(position).getProductno());
                ref.child("quantity").setValue("1");
                ref.child("size").setValue(sizes);

                ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("wish").child(list.get(position).getProductno());
                ref.setValue(null);

                list.remove(position);

                    WishlistActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    slideDown(WishlistActivity.layoutsec);

                }

                else {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    assert v != null;
                    v.vibrate(200);
                }
            }
        });

    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void sizeSelect(){

        WishlistActivity.s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistActivity.stext.setTextColor(Color.parseColor("#fc3c6c"));
                WishlistActivity.s.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder2));
                WishlistActivity.mtext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.m.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.ltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.l.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.xltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.xl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.xxltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.xxl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));

                WishlistActivity.selectsize.setText("SIZE: S");

                sizesel=1;
                sizes="S";
            }
        });

        WishlistActivity.m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistActivity.mtext.setTextColor(Color.parseColor("#fc3c6c"));
                WishlistActivity.m.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder2));
                WishlistActivity.stext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.s.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.ltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.l.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.xltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.xl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.xxltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.xxl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));

                WishlistActivity.selectsize.setText("SIZE: M");

                sizesel=1;
                sizes="S";
            }
        });

        WishlistActivity.l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistActivity.ltext.setTextColor(Color.parseColor("#fc3c6c"));
                WishlistActivity.l.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder2));
                WishlistActivity.mtext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.m.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.stext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.s.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.xltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.xl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.xxltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.xxl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));

                WishlistActivity.selectsize.setText("SIZE: L");

                sizesel=1;
                sizes="S";
            }
        });

        WishlistActivity.xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistActivity.xltext.setTextColor(Color.parseColor("#fc3c6c"));
                WishlistActivity.xl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder2));
                WishlistActivity.mtext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.m.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.ltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.l.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.stext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.s.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.xxltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.xxl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));

                WishlistActivity.selectsize.setText("SIZE: XL");

                sizesel=1;
                sizes="S";
            }
        });

        WishlistActivity.xxl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WishlistActivity.xxltext.setTextColor(Color.parseColor("#fc3c6c"));
                WishlistActivity.xxl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder2));
                WishlistActivity.mtext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.m.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.ltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.l.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.xltext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.xl.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));
                WishlistActivity.stext.setTextColor(Color.parseColor("#343438"));
                WishlistActivity.s.setBackground(ContextCompat.getDrawable(context, R.drawable.catborder));

                WishlistActivity.selectsize.setText("SIZE: XXL");

                sizesel=1;
                sizes="S";
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
        LinearLayout card,cancel,bag;


        MyHoder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            adtitle= itemView.findViewById(R.id.addetails);
            brand=itemView.findViewById(R.id.brand);
            image1 =itemView.findViewById(R.id.image);
            card=itemView.findViewById(R.id.card_view);
            cancel=itemView.findViewById(R.id.cancel);
            bag=itemView.findViewById(R.id.bag);
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


