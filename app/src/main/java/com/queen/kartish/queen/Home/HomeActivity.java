package com.queen.kartish.queen.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.queen.kartish.queen.AccountDetails.BagActivity;
import com.queen.kartish.queen.AccountDetails.WishlistActivity;
import com.queen.kartish.queen.Home.Searching.SearchActivity;
import com.queen.kartish.queen.LoginRegister.LoginRegisterActivity;
import com.queen.kartish.queen.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView brandrecycle,justinrecycle,editorrecycle,themerecycle,horizontaldeals,category;
    HorizontalCategoryAdapter brandAdapter,justinAdapter,editorAdapter,themeAdapter;
    HorizontalDealsAdapter horizontalDealsAdapter;
    CategoryAdapter categoryadapter;
    List<Pair<String,String>> listbrand=new ArrayList<>();
    List<Pair<String,String>> listjustin=new ArrayList<>();
    List<Pair<String,String>> listeditor=new ArrayList<>();
    List<Pair<String,String>> listtheme=new ArrayList<>();
    List<Pair<String,String>> listdeals=new ArrayList<>();
    List<Pair<String,String>> listcategory=new ArrayList<>();
    ImageView image1,image2,image3,image4,image5;
    public static List<String> listwish=new ArrayList<>();
    LinearLayout search,wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);

        brandrecycle=findViewById(R.id.brandrecycle);
        justinrecycle=findViewById(R.id.justinrecycle);
        editorrecycle=findViewById(R.id.editorrecycle);
        themerecycle=findViewById(R.id.themerecycle);
        horizontaldeals=findViewById(R.id.horzontaldeals);
        category=findViewById(R.id.categories);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        image4=findViewById(R.id.image4);
        search=findViewById(R.id.search);
        image5=findViewById(R.id.image5);
        wishlist=findViewById(R.id.wishlist);

        BottomNavigationView navigation=findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), WishlistActivity.class);
                startActivity(intent);
            }
        });

        imageLoading();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        horizontalDealsAdapter = new HorizontalDealsAdapter(listdeals, HomeActivity.this);
        RecyclerView.LayoutManager recycet = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontaldeals.setLayoutManager(recycet);
        horizontaldeals.setLayoutManager(recycet);
        horizontaldeals.setAdapter(horizontalDealsAdapter);

        categoryadapter = new CategoryAdapter(listcategory, HomeActivity.this);
        RecyclerView.LayoutManager recycer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        category.setLayoutManager(recycer);
        category.setLayoutManager(recycer);
        category.setAdapter(categoryadapter);

        brandAdapter = new HorizontalCategoryAdapter(listbrand, HomeActivity.this);
        RecyclerView.LayoutManager recycebooks = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        brandrecycle.setLayoutManager(recycebooks);
        brandrecycle.setLayoutManager(recycebooks);
        brandrecycle.setAdapter(brandAdapter);

        justinAdapter = new HorizontalCategoryAdapter(listjustin, HomeActivity.this);
        RecyclerView.LayoutManager recyce = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        justinrecycle.setLayoutManager(recyce);
        justinrecycle.setLayoutManager(recyce);
        justinrecycle.setAdapter(justinAdapter);

        editorAdapter = new HorizontalCategoryAdapter(listeditor, HomeActivity.this);
        RecyclerView.LayoutManager recyce2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        editorrecycle.setLayoutManager(recyce2);
        editorrecycle.setLayoutManager(recyce2);
        editorrecycle.setAdapter(editorAdapter);

        themeAdapter = new HorizontalCategoryAdapter(listtheme, HomeActivity.this);
        RecyclerView.LayoutManager recyce3 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        themerecycle.setLayoutManager(recyce3);
        themerecycle.setLayoutManager(recyce3);
        themerecycle.setAdapter(themeAdapter);

        categoryMethod();
        horizontalDealsMethod();
        brandMethod();
        justinMethod();
        editorMethod();
        themeMethod();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent2=new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent2);
                    return true;

                case R.id.navigation_cat:
                    return true;

                case R.id.navigation_bag:
                    Intent intent3=new Intent(getApplicationContext(), BagActivity.class);
                    startActivity(intent3);
                    return true;

                case R.id.navigation_profile:
                    Intent intent=new Intent(getApplicationContext(), LoginRegisterActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };


    public void categoryMethod(){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("category_images");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {

                    String brandName = dataSnapshot1.child("Name").getValue(String.class);
                    String imageUrl = dataSnapshot1.child("ImageUrl").getValue(String.class);

                    listcategory.add(new Pair <> (brandName,imageUrl));

                    categoryadapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void horizontalDealsMethod(){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Hori_img");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {

                    String brandName = dataSnapshot1.child("Name").getValue(String.class);
                    String imageUrl = dataSnapshot1.child("ImageUrl").getValue(String.class);

                    listdeals.add(new Pair <> (brandName,imageUrl));

                    horizontalDealsAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void imageLoading(){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Images");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                Glide.with(getApplicationContext()).asBitmap().load(dataSnapshot.child("1").child("ImageUrl").getValue(String.class)).into(image1);
                Glide.with(getApplicationContext()).asBitmap().load(dataSnapshot.child("2").child("ImageUrl").getValue(String.class)).into(image2);
                Glide.with(getApplicationContext()).asBitmap().load(dataSnapshot.child("4").child("ImageUrl").getValue(String.class)).into(image3);
                Glide.with(getApplicationContext()).asBitmap().load(dataSnapshot.child("3").child("ImageUrl").getValue(String.class)).into(image4);
                Glide.with(getApplicationContext()).asBitmap().load(dataSnapshot.child("5").child("ImageUrl").getValue(String.class)).into(image5);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void brandMethod(){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Brand");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {

                    String brandName = dataSnapshot1.child("Name").getValue(String.class);
                    String imageUrl = dataSnapshot1.child("Image_URL").getValue(String.class);

                    listbrand.add(new Pair <> (brandName,imageUrl));

                    brandAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void justinMethod(){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Just_in");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {

                    String brandName = dataSnapshot1.child("Name").getValue(String.class);
                    String imageUrl = dataSnapshot1.child("ImageUrl").getValue(String.class);

                    listjustin.add(new Pair <> (brandName,imageUrl));

                    justinAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void editorMethod(){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Editor_picks");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {

                    String brandName = dataSnapshot1.child("Name").getValue(String.class);
                    String imageUrl = dataSnapshot1.child("ImageUrl").getValue(String.class);

                    listeditor.add(new Pair <> (brandName,imageUrl));

                    editorAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void themeMethod(){

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Theme");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {

                    String brandName = dataSnapshot1.child("Name").getValue(String.class);
                    String imageUrl = dataSnapshot1.child("ImageUrl").getValue(String.class);

                    listtheme.add(new Pair <> (brandName,imageUrl));

                    themeAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


}
