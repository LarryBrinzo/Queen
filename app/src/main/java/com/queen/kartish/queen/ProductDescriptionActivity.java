package com.queen.kartish.queen;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductDescriptionActivity extends AppCompatActivity {

    TextView adtitle,price,discounted_price,discount,selectsize,sizechart,stext,mtext,ltext,xltext,xxltext,pdetails,pmaterial,psize;
    LinearLayout s,m,l,xl,xxl,size,addtobag,wishlist;
    String product_no,sizes;
    int sizesel=0;
    ViewPager mDemoSlider;
    FirebaseUser fuser;
    MyCustomPagerAdapter myCustomPagerAdapter;
    List<String> images=new ArrayList<>();
    FirebaseAuth firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        adtitle=findViewById(R.id.adtitle);
        price=findViewById(R.id.price);
        discount=findViewById(R.id.discount);
        discounted_price=findViewById(R.id.discountedprice);
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
        addtobag=findViewById(R.id.addtobag);
        wishlist=findViewById(R.id.wishlist);
        pdetails=findViewById(R.id.pdeatils);
        pmaterial=findViewById(R.id.pmaterial);
        psize=findViewById(R.id.psize);
        mDemoSlider =findViewById(R.id.slider);

        firebaseAuth = FirebaseAuth.getInstance();
        fuser = firebaseAuth.getCurrentUser();

        sizeSelect();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            product_no=bundle.getString("product_no");
            ImagesfromDatabase();
            addetailsset(product_no);
        }

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_container);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);

        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(adtitle.getText().toString());
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void sizeSelect(){

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stext.setTextColor(Color.parseColor("#fc3c6c"));
                s.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder2));
                mtext.setTextColor(Color.parseColor("#343438"));
                m.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                ltext.setTextColor(Color.parseColor("#343438"));
                l.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                xltext.setTextColor(Color.parseColor("#343438"));
                xl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                xxltext.setTextColor(Color.parseColor("#343438"));
                xxl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));

                selectsize.setText("SIZE: S");

                sizesel=1;
                sizes="S";
            }
        });

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtext.setTextColor(Color.parseColor("#fc3c6c"));
                m.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder2));
                stext.setTextColor(Color.parseColor("#343438"));
                s.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                ltext.setTextColor(Color.parseColor("#343438"));
                l.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                xltext.setTextColor(Color.parseColor("#343438"));
                xl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                xxltext.setTextColor(Color.parseColor("#343438"));
                xxl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));

                selectsize.setText("SIZE: M");

                sizesel=1;
                sizes="M";
            }
        });

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ltext.setTextColor(Color.parseColor("#fc3c6c"));
                l.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder2));
                mtext.setTextColor(Color.parseColor("#343438"));
                m.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                stext.setTextColor(Color.parseColor("#343438"));
                s.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                xltext.setTextColor(Color.parseColor("#343438"));
                xl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                xxltext.setTextColor(Color.parseColor("#343438"));
                xxl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));

                selectsize.setText("SIZE: L");

                sizesel=1;
                sizes="L";
            }
        });

        xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xltext.setTextColor(Color.parseColor("#fc3c6c"));
                xl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder2));
                mtext.setTextColor(Color.parseColor("#343438"));
                m.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                ltext.setTextColor(Color.parseColor("#343438"));
                l.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                stext.setTextColor(Color.parseColor("#343438"));
                s.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                xxltext.setTextColor(Color.parseColor("#343438"));
                xxl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));

                selectsize.setText("SIZE: XL");

                sizesel=1;
                sizes="XL";
            }
        });

        xxl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xxltext.setTextColor(Color.parseColor("#fc3c6c"));
                xxl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder2));
                mtext.setTextColor(Color.parseColor("#343438"));
                m.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                ltext.setTextColor(Color.parseColor("#343438"));
                l.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                xltext.setTextColor(Color.parseColor("#343438"));
                xl.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));
                stext.setTextColor(Color.parseColor("#343438"));
                s.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.catborder));

                selectsize.setText("SIZE: XXL");

                sizesel=1;
                sizes="XXL";
            }
        });

        addtobag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sizesel==0){
                    Animation shake;
                    shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                    size.startAnimation(shake);
                }

                else if(fuser==null){

                }

                else {
                    String userid= firebaseAuth.getCurrentUser().getUid();

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("bag").child(product_no+" "+sizes);
                    ref.child("product_no").setValue(product_no);
                    ref.child("quantity").setValue("1");
                    ref.child("size").setValue(sizes);
                }

            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fuser!=null){
                    String userid= firebaseAuth.getCurrentUser().getUid();

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("wish").child(product_no);
                    ref.setValue(product_no);

                }

                else {

                }
            }
        });

    }

    public void addetailsset(String product_no) {

        DatabaseReference refer;
        refer=FirebaseDatabase.getInstance().getReference().child("Products").child(product_no);
        refer.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String brand=(dataSnapshot.child("brand").getValue(String.class));
                String name=(dataSnapshot.child("name").getValue(String.class));

                SpannableString ss1=  new SpannableString(brand);
                ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);

                ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                Spannable wordTwo = new SpannableString(name);

                wordTwo.setSpan(new ForegroundColorSpan(Color.LTGRAY), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                adtitle.append(ss1);
                adtitle.append(" ");
                adtitle.append(wordTwo);

                price.setText("₹"+dataSnapshot.child("price").getValue(String.class));
                discount.setText(dataSnapshot.child("discount").getValue(String.class));
                discounted_price.setText("₹"+dataSnapshot.child("discounted_price").getValue(String.class));
                discounted_price.setPaintFlags(discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                pdetails.setText(dataSnapshot.child("details").getValue(String.class));
                pmaterial.setText(dataSnapshot.child("material").getValue(String.class));
                psize.setText(dataSnapshot.child("size").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void ImagesfromDatabase(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products").child(product_no).child("images");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    images.add(dataSnapshot1.getValue(String.class));
                }

                myCustomPagerAdapter = new MyCustomPagerAdapter(ProductDescriptionActivity.this, images);
                mDemoSlider.setAdapter(myCustomPagerAdapter);
                TabLayout tabLayout = findViewById(R.id.tab_layout);
                tabLayout.setupWithViewPager(mDemoSlider, true);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

