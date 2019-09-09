package com.queen.kartish.queen.AccountDetails;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.queen.kartish.queen.ProductModel;
import com.queen.kartish.queen.R;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

    String userid;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser fuser;
    FirebaseAuth myfba;
    List<ProductModel> wishlist=new ArrayList<>();
    List<String> listwish=new ArrayList<>();
    RecyclerView recycle;
    LinearLayout empty;
    LinearLayout back;
    WishlistAdapter wishlistAdapter;
    public  static View layoutsec;
    public  static Button done;
    public static LinearLayout s,m,l,xl,xxl,size,popup;
    public static TextView selectsize,sizechart,stext,mtext,ltext,xltext,xxltext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myfba= FirebaseAuth.getInstance();
        fuser = myfba.getCurrentUser();
        recycle =findViewById(R.id.recycle);
        back=findViewById(R.id.backbt);
        empty=findViewById(R.id.empty);
        userid=myfba.getCurrentUser().getUid();
        selectsize=findViewById(R.id.selectsize);
        sizechart=findViewById(R.id.sizechart);
        stext=findViewById(R.id.stext);
        mtext=findViewById(R.id.mtext);
        ltext=findViewById(R.id.ltext);
        xltext=findViewById(R.id.xltext);
        xxltext=findViewById(R.id.xxltext);
        s=findViewById(R.id.s);
        m=findViewById(R.id.m);
        l=findViewById(R.id.l);
        xl=findViewById(R.id.xl);
        xxl=findViewById(R.id.xxl);
        size=findViewById(R.id.size);
        popup=findViewById(R.id.popup);
        layoutsec = findViewById(R.id.my_view);
        done=findViewById(R.id.done);

        layoutsec.setVisibility(View.INVISIBLE);
        popup.setVisibility(View.GONE);

        recycle.setNestedScrollingEnabled(false);

        wishlistAdapter = new WishlistAdapter(wishlist,WishlistActivity.this);
        RecyclerView.LayoutManager recyceAll = new GridLayoutManager(WishlistActivity.this,2);
        recycle.setLayoutManager(recyceAll);
        recyceAll.setAutoMeasureEnabled(false);
        recycle.setItemAnimator( new DefaultItemAnimator());
        recycle.setAdapter(wishlistAdapter);

        empty.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userwish();

    }

    public void addproductad(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                wishlist.clear();

                for(int i=0;i<listwish.size();i++) {

                    String product_no=listwish.get(i);

                    String price = dataSnapshot.child(product_no).child("price").getValue(String.class);
                    String image1 = dataSnapshot.child(product_no).child("image1").getValue(String.class);

                    String adtitle = dataSnapshot.child(product_no).child("name").getValue(String.class);

                    String brand = dataSnapshot.child(product_no).child("brand").getValue(String.class);

                    price = "₹" + price;

                    ProductModel fire = new ProductModel();

                    fire.setPrice(price);
                    fire.setTitle(adtitle);
                    fire.setBrand(brand);
                    fire.setProductno(product_no);
                    fire.setUrl1(image1);

                    wishlist.add(fire);

                    wishlistAdapter.notifyItemInserted(wishlist.size() - 1);

                }

                wishlistAdapter.notifyDataSetChanged();

                if(listwish.isEmpty())
                    empty.setVisibility(View.VISIBLE);
                else
                    empty.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void userwish(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("wish");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listwish.clear();

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    String product_no=dataSnapshot1.getValue(String.class);
                    listwish.add(product_no);
                }

                addproductad();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
