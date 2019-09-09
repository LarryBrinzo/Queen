package com.queen.kartish.queen.AccountDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.queen.kartish.queen.ProductModel;
import com.queen.kartish.queen.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BagActivity extends AppCompatActivity {

    String userid;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser fuser;
    FirebaseAuth myfba;
    List<ProductModel> orderlist=new ArrayList<>();
    List<Pair<String,Pair<String,String>>> listorder=new ArrayList<>();
    RecyclerView recycle;
    LinearLayout empty,placeorder,details;
    LinearLayout back;
    BagAdapter bagAdapter;
    TextView item,tprice,totprice,discount,tax,pricedetails,detailprice;
    int totalprice=0,discounted_price=0;
    public static LinearLayout popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myfba= FirebaseAuth.getInstance();
        fuser = myfba.getCurrentUser();
        recycle =findViewById(R.id.recycle);
        back=findViewById(R.id.backbt);
        empty=findViewById(R.id.empty);
        userid=myfba.getCurrentUser().getUid();
        item=findViewById(R.id.item);
        tprice=findViewById(R.id.tprice);
        totprice=findViewById(R.id.totprice);
        discount=findViewById(R.id.discount);
        tax=findViewById(R.id.tax);
        popup=findViewById(R.id.popup);
        details=findViewById(R.id.details);
        placeorder=findViewById(R.id.placeorder);
        pricedetails=findViewById(R.id.pricedetails);
        detailprice=findViewById(R.id.detailprice);

        recycle.setNestedScrollingEnabled(false);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation shake;
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                pricedetails.startAnimation(shake);
            }
        });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm:ss");
                String strDate =mdformat.format(calendar.getTime());

                if (calendar.get(Calendar.AM_PM) == 0) {
                    strDate+=" AM";
                } else {
                    strDate+=" PM";
                }

                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd:MM:yyyy");
                String date = df.format(Calendar.getInstance().getTime());

                date+=" ";
                date+=strDate;

                final String key= FirebaseAuth.getInstance().getCurrentUser().getUid()+" "+date;

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                            String product_no=dataSnapshot1.child("product_no").getValue(String.class);
                            String qty=dataSnapshot1.child("quantity").getValue(String.class);
                            String size=dataSnapshot1.child("size").getValue(String.class);

                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Orders").child(key).child(product_no+" "+size);

                            ref.child("product_no").setValue(product_no);
                            ref.child("quantity").setValue(qty);
                            ref.child("size").setValue(size);

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                Intent intent=new Intent(getApplicationContext(),OrderConfirmationActivity.class);
                startActivity(intent);
            }
        });

        bagAdapter = new BagAdapter(orderlist,BagActivity.this);
        RecyclerView.LayoutManager recyceAll = new GridLayoutManager(BagActivity.this,1);
        recycle.setLayoutManager(recyceAll);
        recyceAll.setAutoMeasureEnabled(false);
        recycle.setItemAnimator( new DefaultItemAnimator());
        recycle.setAdapter(bagAdapter);

        empty.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userorder();

    }

    public void addproductad(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderlist.clear();

                for(int i=0;i<listorder.size();i++) {

                    String product_no=listorder.get(i).first;

                    if(product_no==null)
                        continue;

                    String price = dataSnapshot.child(product_no).child("price").getValue(String.class);
                    String image1 = dataSnapshot.child(product_no).child("image1").getValue(String.class);

                    String adtitle = dataSnapshot.child(product_no).child("name").getValue(String.class);

                    String brand = dataSnapshot.child(product_no).child("brand").getValue(String.class);

                    String quantity=listorder.get(i).second.first;

                    totalprice=totalprice+Integer.parseInt(quantity)*Integer.parseInt(price);
                    price = "₹" + price;

                    discounted_price=discounted_price+Integer.parseInt(quantity)*Integer.parseInt(dataSnapshot.child(product_no).child("discounted_price").getValue(String.class));

                    ProductModel fire = new ProductModel();

                    fire.setPrice(price);
                    fire.setTitle(adtitle);
                    fire.setBrand(brand);
                    fire.setProductno(product_no);
                    fire.setUrl1(image1);
                    fire.setSize(listorder.get(i).second.second);
                    fire.setQuantity(listorder.get(i).second.first);
                    fire.setDiscount(dataSnapshot.child(product_no).child("discount").getValue(String.class));
                    fire.setDiscount_price(dataSnapshot.child(product_no).child("discounted_price").getValue(String.class));


                    orderlist.add(fire);

                    bagAdapter.notifyItemInserted(orderlist.size() - 1);

                }

                item.setText("ITEMS ("+orderlist.size()+")");
                tprice.setText("TOTAL: ₹"+ totalprice);
                totprice.setText("₹"+discounted_price);
                detailprice.setText("₹"+(discounted_price-totalprice));
                discount.setText("-₹"+(discounted_price-totalprice));
                bagAdapter.notifyDataSetChanged();

                if(orderlist.isEmpty())
                    empty.setVisibility(View.VISIBLE);
                else
                    empty.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    public void userorder(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listorder.clear();

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    String product_no=dataSnapshot1.child("product_no").getValue(String.class);
                    String qty=dataSnapshot1.child("quantity").getValue(String.class);
                    String size=dataSnapshot1.child("size").getValue(String.class);
                    listorder.add(new Pair <> (product_no, new Pair <> (qty,size)));
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
