package com.queen.kartish.queen.Home.Searching;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.queen.kartish.queen.Home.HomeActivity;
import com.queen.kartish.queen.Home.ProductAdAdapter;
import com.queen.kartish.queen.ProductModel;
import com.queen.kartish.queen.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.StrictMath.max;

public class ProductDisplayActivity extends AppCompatActivity implements Comparator<Pair<String,Integer>> {

    SearchView search;
    RecyclerView productrecycle,suggestionrecycle;
    List<ProductModel> searchlist=new ArrayList<>();
    ProductAdAdapter recyclerAdapterSearch;
    TextView prod;
    NestedScrollView scrollView;
    List<Pair<String,Integer>> sugglist=new ArrayList<>();
    long adstart,adend,itm=0,endstart=1;
    public static long adcount=0;
    int sclrct=0,notify=1,scroll=1;
    ImageView searchbt;
    List<Pair<String,Integer>> list=new ArrayList<>();
    SuggestionAdapter suggestionAdapter;
    LinearLayout filter,bbtn,bbtn2,toolbar1,toolbar2;
    String suggstring=null;
    ProgressBar progbar;
    int rsearchinsert=0;
    String sstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_display);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = findViewById(R.id.search);
        assert searchManager != null;
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setQueryRefinementEnabled(true);

        productrecycle=findViewById(R.id.productrecycle);
        prod=findViewById(R.id.prod);
        suggestionrecycle =findViewById(R.id.suggestion);
        progbar=findViewById(R.id.prog);
        scrollView=findViewById(R.id.scrollView2);
        filter=findViewById(R.id.filter);
        bbtn=findViewById(R.id.backbt);
        bbtn2=findViewById(R.id.backbt2);
        searchbt=findViewById(R.id.searchbt);
        toolbar1=findViewById(R.id.toolbar1);
        toolbar2=findViewById(R.id.toolbar2);

        recyclerAdapterSearch = new ProductAdAdapter(searchlist, ProductDisplayActivity.this);
        RecyclerView.LayoutManager recyceAll = new GridLayoutManager(ProductDisplayActivity.this,2);
        productrecycle.setLayoutManager(recyceAll);
        recyceAll.setAutoMeasureEnabled(false);
        productrecycle.setItemAnimator( new DefaultItemAnimator());
        productrecycle.setAdapter(recyclerAdapterSearch);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                adcount=dataSnapshot.getChildrenCount();
                adstart=adcount-1;
                adend= Math.max(adstart-29,1);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String s = bundle.getString("searchstring");
            prod.setText(s);

            if (bundle.containsKey("click")){
                searchlist.clear();
                productrecycle.removeAllViewsInLayout();
                recyclerAdapterSearch.notifyDataSetChanged();
                Adtraversal(s);
            }

            if (bundle.containsKey("offers"))
                rsearchinsert=1;

            if(rsearchinsert==0){
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor searchhint = pref.edit();

                int serachnumber=pref.getInt("serachnumber", 0)+1;
                String srchitm="recent"+(serachnumber);
                searchhint.putString(srchitm,s);

                String srchimg="recentimg"+(serachnumber);
                searchhint.putString(srchimg,"1");

                searchhint.putInt("serachnumber",serachnumber);
                searchhint.apply();

                sstring=s;
            }

            if (bundle.containsKey("sugghstring")){
                searchlist.clear();
                productrecycle.removeAllViewsInLayout();
                recyclerAdapterSearch.notifyDataSetChanged();
                suggstring=bundle.getString("sugghstring");
                Adtraversal(s);
            }

            else if(s != null) {
                searchlist.clear();
                productrecycle.removeAllViewsInLayout();
                recyclerAdapterSearch.notifyDataSetChanged();
                Adtraversal(s);
            }
        }


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                InputMethodManager imm = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                if (scrollY > oldScrollY) {
                    assert imm != null;
                    if (!imm.isAcceptingText()){
                        sugglist.clear();
                        suggestionrecycle.removeAllViewsInLayout();
                        suggestionAdapter.notifyDataSetChanged();}
                }
                else if (scrollY < oldScrollY) {
                    assert imm != null;
                    if (!imm.isAcceptingText()){
                        sugglist.clear();
                        suggestionrecycle.removeAllViewsInLayout();
                        suggestionAdapter.notifyDataSetChanged();}
                }

                if (scrollY == 0) {
                }

                if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                }
            }
        });

        suggestionrecycle.setNestedScrollingEnabled(false);

        suggestionAdapter = new SuggestionAdapter(sugglist, ProductDisplayActivity.this);
        RecyclerView.LayoutManager recyceSugg = new GridLayoutManager(ProductDisplayActivity.this,1);
        suggestionrecycle.setLayoutManager(recyceSugg);
        recyceSugg.setAutoMeasureEnabled(false);
        suggestionrecycle.setItemAnimator( new DefaultItemAnimator());
        suggestionrecycle.setAdapter(suggestionAdapter);


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                newText=newText.trim();
                newText=newText.toLowerCase();

                list.clear();
                suggestionrecycle.removeAllViewsInLayout();
                suggestionAdapter.notifyDataSetChanged();

                if(newText.length()==0){
                    suggestionrecycle.setVisibility(View.GONE);

                    sugglist.clear();
                    suggestionrecycle.removeAllViewsInLayout();
                    suggestionAdapter.notifyDataSetChanged();
                }

                if(newText.length()>1) {

                   // searchprog.setVisibility(View.VISIBLE);

                    DatabaseReference ref;
                    ref= FirebaseDatabase.getInstance().getReference().child("Products");

                    final String finalNewText = newText;
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            int ct=0;

                            for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                                String actualproductname=dataSnapshot1.child("name").getValue(String.class);
                                String actualproductbrand=dataSnapshot1.child("brand").getValue(String.class);

                                String name= actualproductbrand.toLowerCase();

                                if(name.length()> finalNewText.length() && (name.startsWith(finalNewText))){

                                    if(ct==0){
                                        ct=1;

                                        sugglist.clear();
                                        suggestionrecycle.removeAllViewsInLayout();
                                        suggestionAdapter.notifyDataSetChanged();
                                    }

                                    if(sugglist.size()==0)
                                        sugglist.add(new Pair(name,finalNewText.length()));

                                    else
                                        sugglist.add(new Pair(name,finalNewText.length()));

                                    name= actualproductname.toLowerCase();
                                }

                                else if(name.length()> finalNewText.length() && (name.contains(finalNewText))){

                                    if(ct==0){
                                        ct=1;

                                        sugglist.clear();
                                        suggestionrecycle.removeAllViewsInLayout();
                                        suggestionAdapter.notifyDataSetChanged();
                                    }

                                    sugglist.add(new Pair(name,0));

                                }

                                else {

                                    String[] splited = finalNewText.split(" ");

                                    for (String aSplited : splited) {

                                        if (name.length() > aSplited.length() && name.contains(aSplited)){

                                            if(ct==0){
                                                ct=1;

                                                sugglist.clear();
                                                suggestionrecycle.removeAllViewsInLayout();
                                                suggestionAdapter.notifyDataSetChanged();
                                            }

                                            sugglist.add(new Pair(name,0));
                                        }
                                    }
                                }


                                name= actualproductname.toLowerCase();

                                if(name.length()> finalNewText.length() && (name.startsWith(finalNewText))){

                                    if(ct==0){
                                        ct=1;

                                        sugglist.clear();
                                        suggestionrecycle.removeAllViewsInLayout();
                                        suggestionAdapter.notifyDataSetChanged();
                                    }

                                    if(sugglist.size()==0)
                                        sugglist.add(new Pair(name,finalNewText.length()));

                                    else
                                        sugglist.add(new Pair(name,finalNewText.length()));
                                }

                                else if(name.length()> finalNewText.length() && (name.contains(finalNewText))){

                                    if(ct==0){
                                        ct=1;

                                        sugglist.clear();
                                        suggestionrecycle.removeAllViewsInLayout();
                                        suggestionAdapter.notifyDataSetChanged();
                                    }

                                    sugglist.add(new Pair(name,0));

                                }


                                else {

                                    String[] splited = finalNewText.split(" ");

                                    for (String aSplited : splited) {

                                        if (name.length() > aSplited.length() && name.contains(aSplited)){

                                            if(ct==0){
                                                ct=1;

                                                sugglist.clear();
                                                suggestionrecycle.removeAllViewsInLayout();
                                                suggestionAdapter.notifyDataSetChanged();
                                            }

                                            sugglist.add(new Pair(name,0));
                                        }
                                    }
                                }
                            }

                            //searchprog.setVisibility(View.GONE);
                            //cancel.setVisibility(View.VISIBLE);
                            suggestionrecycle.setVisibility(View.VISIBLE);

                            if(sugglist.size()==0){
//                                noresult.setVisibility(View.VISIBLE);
//                                noresult.setText("Sorry, we couldn't find result matching "+"\""+search.getText()+"\"");
//                                recview.setVisibility(View.GONE);
                            }

                            else {
//                                noresult.setVisibility(View.GONE);
//                                recview.setVisibility(View.VISIBLE);
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    suggestionAdapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                String s=query;

                if(!s.isEmpty())
                {
                    s=s.toLowerCase();

                    if(sugglist.size()>0 && list.size()==0)
                        suggstring=sugglist.get(0).first;

                    searchlist.clear();
                    productrecycle.removeAllViewsInLayout();
                    recyclerAdapterSearch.notifyDataSetChanged();
                    Adtraversal(s);

                    return true;
                }

                return false;
            }

        });


        searchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbar1.setVisibility(View.GONE);
                toolbar2.setVisibility(View.VISIBLE);

                search.requestFocus();
            }
        });

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showInputMethod(view.findFocus());
                }
            }
        });

        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }


    public void searchevent(final String searchitem,final long start){

        DatabaseReference databaseReference;

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(long i=start;i>=adend;i--) {

                    if(i<=endstart)
                        break;

                    String product_no=Long.toString(i);

                    itm++;

                        int result=0;

                        String price = dataSnapshot.child(product_no).child("price").getValue(String.class);
                        String image1 = dataSnapshot.child(product_no).child("image1").getValue(String.class);

                        String adtitle = dataSnapshot.child(product_no).child("name").getValue(String.class);
                        assert adtitle != null;
                        adtitle=adtitle.trim();
                        String adtitle2=adtitle.toLowerCase();

                        String brand = dataSnapshot.child(product_no).child("brand").getValue(String.class);
                        assert brand != null;
                        brand=brand.trim();
                        String brand2=brand.toLowerCase();

                        price = "₹" + price;

                        String[] splited = searchitem.split(" ");

                        for (String ss : splited) {

                            boolean b=adtitle2.contains(ss);
                            if (b){
                                result=1;
                                break;
                            }

                            b=brand2.contains(ss);
                            if (b){
                                result=1;
                                break;
                            }

                        }

                        if(suggstring!=null){

                            String[] splited2 = suggstring.split(" ");

                            for (String ss : splited2) {

                                Boolean b=adtitle2.contains(ss);
                                if (b){
                                    result=1;
                                    break;
                                }

                                b=brand2.contains(ss);
                                if (b){
                                    result=1;
                                    break;
                                }

                            }
                        }

                        if(result==1){

                            ProductModel fire = new ProductModel();

                            fire.setPrice(price);
                            fire.setTitle(adtitle);
                            fire.setBrand(brand);
                            fire.setProductno(product_no);
                            fire.setUrl1(image1);

                            if(HomeActivity.listwish.contains(product_no))
                                fire.setWish("1");

                            else
                                fire.setWish("0");

                            searchlist.add(fire);

                            if(rsearchinsert==0){

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor searchhint = pref.edit();

                                int serachnumber=pref.getInt("serachnumber", 0);

                                String srchimg="recentimg"+Integer.toString(serachnumber);
                                searchhint.putString(srchimg,image1);

                                searchhint.apply();

                                SearchActivity.recsugglist.add(0,new Pair <> (Integer.toString(serachnumber),new Pair <>(sstring, image1)));
                                SearchActivity.recyclerAdapterRecent.notifyItemInserted(0);

                                rsearchinsert=1;
                            }

                            if(notify==1){

                                if(searchlist.size()>=1)
                                    recyclerAdapterSearch.notifyItemInserted(searchlist.size() - 1);

                                else
                                    recyclerAdapterSearch.notifyItemInserted(0);
                            }

                        }

                        else if(adend>0){
                            adend--;
                        }






                    if(i<=endstart)
                        break;




                    product_no=Long.toString(endstart);

                    itm++;
                    endstart++;

                    result=0;

                    price = dataSnapshot.child(product_no).child("price").getValue(String.class);
                    image1 = dataSnapshot.child(product_no).child("image1").getValue(String.class);

                    adtitle = dataSnapshot.child(product_no).child("name").getValue(String.class);
                        assert adtitle != null;
                        adtitle=adtitle.trim();
                        adtitle2=adtitle.toLowerCase();

                        brand = dataSnapshot.child(product_no).child("brand").getValue(String.class);
                        assert brand != null;
                        brand=brand.trim();
                        brand2=brand.toLowerCase();

                        splited = searchitem.split(" ");

                        for (String ss : splited) {

                            boolean b=adtitle2.contains(ss);
                            if (b){
                                result=1;
                                break;
                            }

                            b=brand2.contains(ss);
                            if (b){
                                result=1;
                                break;
                            }

                        }

                        if(suggstring!=null){

                            String[] splited2 = suggstring.split(" ");

                            for (String ss : splited2) {

                                boolean b=adtitle2.contains(ss);
                                if (b){
                                    result=1;
                                    break;
                                }

                                b=brand2.contains(ss);
                                if (b){
                                    result=1;
                                    break;
                                }

                            }
                        }

                        if(result==1){

                            ProductModel fire = new ProductModel();

                            fire.setPrice(price);
                            fire.setTitle(adtitle);
                            fire.setBrand(brand);
                            fire.setProductno(product_no);
                            fire.setUrl1(image1);

                            if(HomeActivity.listwish.contains(product_no))
                                fire.setWish("1");

                            else
                                fire.setWish("0");

                            searchlist.add(fire);

                            if(rsearchinsert==0){

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor searchhint = pref.edit();

                                int serachnumber=pref.getInt("serachnumber", 0);

                                String srchimg="recentimg"+Integer.toString(serachnumber);
                                searchhint.putString(srchimg,image1);

                                searchhint.apply();

                                SearchActivity.recsugglist.add(0,new Pair <> (Integer.toString(serachnumber),new Pair <>(sstring, image1)));
                                SearchActivity.recyclerAdapterRecent.notifyItemInserted(0);

                                rsearchinsert=1;
                            }


                            if(notify==1){

                                if(searchlist.size()>=1)
                                    recyclerAdapterSearch.notifyItemInserted(searchlist.size() - 1);

                                else
                                    recyclerAdapterSearch.notifyItemInserted(0);
                            }

                        }

                        else if(adend>0){
                            adend--;
                        }






                }

                adstart=adend;
                sclrct=1;
                scroll=1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void Adtraversal(final String searchst){

        progbar.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adstart= max(adcount,1);
                itm=0;
                adend=max(adstart-49,1);
                searchevent(searchst,adstart);
                progbar.setVisibility(View.GONE);
            }
        }, 2000);


        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                if (diff == 0 && sclrct==1 && itm<adcount && scroll==1 && adstart<=endstart) {

                    scroll=0;
                    notify=1;

                    progbar.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adend=max(adstart-25,1);
                            searchevent(searchst,adstart);

                            progbar.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
            }
        });


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    //Scroll DOWN
                }
                else if (scrollY < oldScrollY) {
                    if (sclrct==1 && itm<adcount && scroll==0) {
                        notify=0;
                    }
                }

                if (scrollY == 0) {
                    //TOP SCROLL
                }

                if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                    //BOTTOM SCROLL
                }
            }
        });

    }




    @Override
    public int compare(Pair<String,Integer> p1, Pair<String,Integer> p2) {
        return p1.first.length() - p2.first.length();
    }

    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

}
