package com.queen.kartish.queen.Home.Searching;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.View;
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
import com.queen.kartish.queen.ProductModel;
import com.queen.kartish.queen.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    SearchView search;
    LinearLayout back,viewed;
    List<Pair<String,Integer>> sugglist=new ArrayList<>();
    List<Pair<String,Integer>> list=new ArrayList<>();
    public static List<Pair<String,Pair<String,String>>> recsugglist=new ArrayList<>();
    NestedScrollView scrollView;
    LinearLayout books,mobile,cycle,electronics;
    ProgressBar progbar;
    List<String> userads=new ArrayList<>();
    public static TextView edit;
    public static LinearLayout recent;
    ValueEventListener eventListener;
    List<ProductModel> listitms=new ArrayList<>();
    CoordinatorLayout coordinatorLayout;
    private static final String ACTION_VOICE_SEARCH = "com.google.android.gms.actions.SEARCH_ACTION";
    public static RecyclerView suggestionrecycle,horzontalrecycle,recycleitm;
    SuggestionAdapter suggestionAdapter;
    public static RecentSearchAdapter recyclerAdapterRecent;
    ImageView fadeimage;
    ItemAdapter recycleitmAdapter;
    private int overallXScroll = 0;
    int bkct=0;
    String userid;
    float alpha = 1.0f;
    float newAlpha = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = findViewById(R.id.search);
        assert searchManager != null;
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setQueryRefinementEnabled(true);

        back=findViewById(R.id.backbt);
        scrollView=findViewById(R.id.scrollView2);
        progbar=findViewById(R.id.prog);
        suggestionrecycle =findViewById(R.id.suggestion);
        horzontalrecycle=findViewById(R.id.horzontalrecycle);
        edit=findViewById(R.id.edit);
        recycleitm=findViewById(R.id.itmrecycle);
        fadeimage=findViewById(R.id.imageView4);
        recent=findViewById(R.id.recent);
        books=findViewById(R.id.books);
        cycle=findViewById(R.id.cycle);
        mobile=findViewById(R.id.mobile);
        electronics=findViewById(R.id.electronics);
        viewed=findViewById(R.id.viewed);

        recyclerAdapterRecent = new RecentSearchAdapter(recsugglist, SearchActivity.this);
        RecyclerView.LayoutManager recycesugg = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        horzontalrecycle.setLayoutManager(recycesugg);
        horzontalrecycle.setLayoutManager(recycesugg);
        horzontalrecycle.setAdapter(recyclerAdapterRecent);

        displayrecentsearch();

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        suggestionAdapter = new SuggestionAdapter(sugglist, SearchActivity.this);
        RecyclerView.LayoutManager recyceSugg = new GridLayoutManager(SearchActivity.this,1);
        suggestionrecycle.setLayoutManager(recyceSugg);
        recyceSugg.setAutoMeasureEnabled(false);
        suggestionrecycle.setItemAnimator( new DefaultItemAnimator());
        suggestionrecycle.setAdapter(suggestionAdapter);

        recycleitmAdapter = new ItemAdapter(listitms, SearchActivity.this);
        RecyclerView.LayoutManager recyceitms = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycleitm.setLayoutManager(recyceitms);
        recycleitm.setLayoutManager(recyceitms);
        recycleitm.setAdapter(recycleitmAdapter);

        recycleitm.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                overallXScroll = overallXScroll + dx;

                newAlpha = alpha - 0.0055f;
                if (dx > 0 && newAlpha>=0.0f && newAlpha<=1.0f){
                    fadeimage.setAlpha(newAlpha);
                    alpha = newAlpha;
                }

                //if scroll right
                newAlpha = alpha + 0.012f;
                if (dx < 0 && newAlpha>=0.0f && newAlpha<=1.0f && overallXScroll<=480){
                    fadeimage.setAlpha(newAlpha);
                    alpha = newAlpha;
                }

                newAlpha = 1.0f;
                if(overallXScroll==0) {
                    fadeimage.setAlpha(newAlpha);
                    alpha = newAlpha;
                }

                newAlpha = 0.0f;
                if(overallXScroll>=480) {
                    fadeimage.setAlpha(newAlpha);
                    alpha = newAlpha;
                }
            }
        });

        viewed.setVisibility(View.GONE);

        handleVoiceSearch(getIntent());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String s = bundle.getString("active");

            if(s != null && s.equals("1")) {

                String ss = bundle.getString("searchitem");

                if(ss != null && !ss.isEmpty()) {
                    String x=ss;
                    search.setQuery(x,false);
                    ss = ss.toLowerCase();

                    Intent intent=new Intent(getApplicationContext(), ProductDisplayActivity.class);
                    intent.putExtra("searchstring", ss);
                    intent.putExtra("click", "1");
                    startActivity(intent);
                }
            }
        }


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

                    Intent intent=new Intent(getApplicationContext(), ProductDisplayActivity.class);
                    intent.putExtra("searchstring", s);
                    if(sugglist.size()>0 && list.size()==0)
                        intent.putExtra("sugghstring", sugglist.get(0).first);
                    startActivity(intent);
                    return true;
                }

                return false;
            }

        });

    }

    public void displayrecentsearch(){

        recsugglist.clear();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int serachnumber=pref.getInt("serachnumber", 0);

        int ind=0;

        for(int i=serachnumber;i>=1;i--){
            String srchkey="recent"+(i);
            String srchimgkey="recentimg"+(i);

            String searchitm=pref.getString(srchkey, null);
            String searchimgitm=pref.getString(srchimgkey, null);

            if(searchitm!=null && searchimgitm!=null){
                recsugglist.add(new Pair <> (Integer.toString(i),new Pair <>(searchitm, searchimgitm)));
                recyclerAdapterRecent.notifyItemInserted(recsugglist.size() - 1);

                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor searchhint = pref.edit();

                String srchitm="recent"+recsugglist.get(ind).first;
                searchhint.putString(srchitm,recsugglist.get(ind).second.first);

                String srchimg="recentimg"+recsugglist.get(ind).first;
                searchhint.putString(srchimg,recsugglist.get(ind).second.second);
                searchhint.apply();

                ind++;

                searchhint.putInt("serachnumber",recsugglist.size());
                searchhint.apply();
            }
        }

        if(recsugglist.size()==0)
            recent.setVisibility(View.GONE);
    }


    private void handleVoiceSearch(Intent intent) {
        if (intent != null && ACTION_VOICE_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search.setQuery(query, true);

            Intent intent2=new Intent(getApplicationContext(), ProductDisplayActivity.class);
            intent2.putExtra("searchstring", query);
            startActivity(intent);
        }
    }

}
