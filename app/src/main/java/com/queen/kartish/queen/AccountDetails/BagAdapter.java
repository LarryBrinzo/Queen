package com.queen.kartish.queen.AccountDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class BagAdapter extends RecyclerView.Adapter<BagAdapter.MyHoder>{

    private List<ProductModel> list;
    private Context context;
    private FirebaseUser fuser;
    private DatabaseReference ref;
    private String userid;
    private PopupWindow mPopupWindow;

    public BagAdapter(List<ProductModel> list, Context context) {
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

        View view = LayoutInflater.from(context).inflate(R.layout.bagcard,parent,false);
        return new MyHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHoder holder, @SuppressLint("RecyclerView") final int position) {
        final ProductModel mylist = list.get(position);
        holder.price.setText(mylist.getPrice());
        holder.title.setText(mylist.getTitle());
        holder.brand.setText(mylist.getBrand());
        holder.discount.setText(mylist.getDiscount());
        holder.discounted_price.setText(mylist.getDiscount_price());
        holder.sizetext.setText("Size: "+mylist.getSize());
        holder.quantitytext.setText("Qty: "+mylist.getQuantity());

        holder.discounted_price.setPaintFlags(holder.discounted_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(context).asBitmap().load(mylist.getUrl1()).into(holder.image1);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, ProductDescriptionActivity.class);
                intent.putExtra("product_no", list.get(position).getProductno());
                context.startActivity(intent);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                ref.setValue(null);

                list.remove(position);
            }
        });

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("wish").child(list.get(position).getProductno());
                ref.setValue(list.get(position).getProductno());

                ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                ref.setValue(null);

                list.remove(position);
            }
        });

        holder.quantitytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BagActivity.popup.setBackgroundColor(Color.parseColor("#80000000"));
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                View customView = inflater.inflate(R.layout.quantity_layout,null);

                BagActivity.popup.setVisibility(View.VISIBLE);

                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                LinearLayout qty1= customView.findViewById(R.id.qty1);
                LinearLayout qty2= customView.findViewById(R.id.qty2);
                LinearLayout qty3= customView.findViewById(R.id.qty3);

                final TextView qty1text=customView.findViewById(R.id.qty1text);
                final TextView qty2text= customView.findViewById(R.id.qty2text);
                final TextView qty3text= customView.findViewById(R.id.qty3text);

                final ImageView qty1img= customView.findViewById(R.id.qty1img);
                final ImageView qty2img= customView.findViewById(R.id.qty2img);
                final ImageView qty3img= customView.findViewById(R.id.qty3img);

                qty1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qty1text.setTypeface(null, Typeface.BOLD);
                        qty1img.setVisibility(View.VISIBLE);

                        qty2text.setTypeface(null, Typeface.NORMAL);
                        qty2img.setVisibility(View.GONE);
                        qty3text.setTypeface(null, Typeface.NORMAL);
                        qty3img.setVisibility(View.GONE);

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                        ref.child("quantity").setValue("1");
                        mPopupWindow.dismiss();

                        BagActivity.popup.setVisibility(View.GONE);

                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });

                qty2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qty2text.setTypeface(null, Typeface.BOLD);
                        qty2img.setVisibility(View.VISIBLE);

                        qty1text.setTypeface(null, Typeface.NORMAL);
                        qty1img.setVisibility(View.GONE);
                        qty3text.setTypeface(null, Typeface.NORMAL);
                        qty3img.setVisibility(View.GONE);

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                        ref.child("quantity").setValue("2");
                        mPopupWindow.dismiss();

                        BagActivity.popup.setVisibility(View.GONE);

                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });

                qty3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qty3text.setTypeface(null, Typeface.BOLD);
                        qty3img.setVisibility(View.VISIBLE);

                        qty2text.setTypeface(null, Typeface.NORMAL);
                        qty2img.setVisibility(View.GONE);
                        qty1text.setTypeface(null, Typeface.NORMAL);
                        qty1img.setVisibility(View.GONE);

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                        ref.child("quantity").setValue("3");
                        mPopupWindow.dismiss();

                        BagActivity.popup.setVisibility(View.GONE);

                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });

                BagActivity.popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BagActivity.popup.setVisibility(View.GONE);
                        mPopupWindow.dismiss();
                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });

                mPopupWindow.showAtLocation(BagActivity.popup, Gravity.CENTER,0,0);
            }
        });

        holder.sizetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BagActivity.popup.setBackgroundColor(Color.parseColor("#80000000"));
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                View customView = inflater.inflate(R.layout.size_layout,null);

                BagActivity.popup.setVisibility(View.VISIBLE);

                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                LinearLayout s= customView.findViewById(R.id.s);
                LinearLayout m= customView.findViewById(R.id.m);
                LinearLayout l= customView.findViewById(R.id.l);
                LinearLayout xl= customView.findViewById(R.id.xl);
                LinearLayout xxl= customView.findViewById(R.id.xxl);

                final TextView stext=customView.findViewById(R.id.stext);
                final TextView mtext= customView.findViewById(R.id.mtext);
                final TextView ltext= customView.findViewById(R.id.ltext);
                final TextView xltext= customView.findViewById(R.id.xltext);
                final TextView xxltext= customView.findViewById(R.id.xxltext);

                final ImageView simg= customView.findViewById(R.id.simg);
                final ImageView mimg= customView.findViewById(R.id.mimg);
                final ImageView limg= customView.findViewById(R.id.limg);
                final ImageView xlimg= customView.findViewById(R.id.xlimg);
                final ImageView xxlimg= customView.findViewById(R.id.xxlimg);


                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stext.setTypeface(null, Typeface.BOLD);
                        simg.setVisibility(View.VISIBLE);

                        mtext.setTypeface(null, Typeface.NORMAL);
                        mimg.setVisibility(View.GONE);
                        ltext.setTypeface(null, Typeface.NORMAL);
                        limg.setVisibility(View.GONE);
                        xltext.setTypeface(null, Typeface.NORMAL);
                        xlimg.setVisibility(View.GONE);
                        xxltext.setTypeface(null, Typeface.NORMAL);
                        xxlimg.setVisibility(View.GONE);

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                        ref.child("size").setValue("S");
                        mPopupWindow.dismiss();

                        BagActivity.popup.setVisibility(View.GONE);

                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });
                m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mtext.setTypeface(null, Typeface.BOLD);
                        mimg.setVisibility(View.VISIBLE);

                        stext.setTypeface(null, Typeface.NORMAL);
                        simg.setVisibility(View.GONE);
                        ltext.setTypeface(null, Typeface.NORMAL);
                        limg.setVisibility(View.GONE);
                        xltext.setTypeface(null, Typeface.NORMAL);
                        xlimg.setVisibility(View.GONE);
                        xxltext.setTypeface(null, Typeface.NORMAL);
                        xxlimg.setVisibility(View.GONE);

                        BagActivity.popup.setVisibility(View.GONE);

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                        ref.child("size").setValue("M");
                        mPopupWindow.dismiss();
                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });
                l.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ltext.setTypeface(null, Typeface.BOLD);
                        limg.setVisibility(View.VISIBLE);

                        mtext.setTypeface(null, Typeface.NORMAL);
                        mimg.setVisibility(View.GONE);
                        stext.setTypeface(null, Typeface.NORMAL);
                        simg.setVisibility(View.GONE);
                        xltext.setTypeface(null, Typeface.NORMAL);
                        xlimg.setVisibility(View.GONE);
                        xxltext.setTypeface(null, Typeface.NORMAL);
                        xxlimg.setVisibility(View.GONE);

                        BagActivity.popup.setVisibility(View.GONE);

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                        ref.child("size").setValue("L");
                        mPopupWindow.dismiss();
                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });
                xl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        xltext.setTypeface(null, Typeface.BOLD);
                        xlimg.setVisibility(View.VISIBLE);

                        mtext.setTypeface(null, Typeface.NORMAL);
                        mimg.setVisibility(View.GONE);
                        ltext.setTypeface(null, Typeface.NORMAL);
                        limg.setVisibility(View.GONE);
                        stext.setTypeface(null, Typeface.NORMAL);
                        simg.setVisibility(View.GONE);
                        xxltext.setTypeface(null, Typeface.NORMAL);
                        xxlimg.setVisibility(View.GONE);

                        BagActivity.popup.setVisibility(View.GONE);

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                        ref.child("size").setValue("XL");
                        mPopupWindow.dismiss();
                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });
                xxl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        xxltext.setTypeface(null, Typeface.BOLD);
                        xxlimg.setVisibility(View.VISIBLE);

                        mtext.setTypeface(null, Typeface.NORMAL);
                        mimg.setVisibility(View.GONE);
                        ltext.setTypeface(null, Typeface.NORMAL);
                        limg.setVisibility(View.GONE);
                        xltext.setTypeface(null, Typeface.NORMAL);
                        xlimg.setVisibility(View.GONE);
                        stext.setTypeface(null, Typeface.NORMAL);
                        simg.setVisibility(View.GONE);

                        BagActivity.popup.setVisibility(View.GONE);

                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(list.get(position).getProductno()+" "+list.get(position).getSize());
                        ref.child("size").setValue("XXL");
                        mPopupWindow.dismiss();
                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });

                BagActivity.popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BagActivity.popup.setVisibility(View.GONE);
                        mPopupWindow.dismiss();
                        BagActivity.popup.setBackgroundColor(Color.parseColor("#00ffffff"));
                    }
                });

                mPopupWindow.showAtLocation(BagActivity.popup, Gravity.CENTER,0,0);

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
        TextView price,title,brand,remove,wishlist,discount,discounted_price,sizetext,quantitytext;
        ImageView image1;
        LinearLayout card,quantity,size;


        MyHoder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            title= itemView.findViewById(R.id.title);
            brand=itemView.findViewById(R.id.brand);
            remove=itemView.findViewById(R.id.remove);
            wishlist=itemView.findViewById(R.id.wishlist);
            discount=itemView.findViewById(R.id.discount);
            discounted_price=itemView.findViewById(R.id.discountedprice);
            quantity=itemView.findViewById(R.id.quantity);
            quantitytext=itemView.findViewById(R.id.quantitytext);
            size=itemView.findViewById(R.id.size);
            sizetext=itemView.findViewById(R.id.sizetext);
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



