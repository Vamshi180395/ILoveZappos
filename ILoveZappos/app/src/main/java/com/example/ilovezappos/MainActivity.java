package com.example.ilovezappos;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements CartAdapter.CartInterface {
    Intent shareIntent;
    CartAdapter alllistadapter;
    RelativeLayout cart;
    MenuItem shareItem;
    DrawerLayout layout;
    RecyclerView cartlistview;
    FloatingActionButton fab;
    TextView badgecount;
    Animation fade_in, fade_out,hide_fab;
    ViewFlipper viewFlipper;
    ImageView upperdisplayimage;
   String apikey="b743e26728e16b81da139182bb2094357c31d331";
    ArrayList<ProductDetails> productslist =new ArrayList<ProductDetails>();
    ArrayList<ProductDetails> productslistwithoneitem;
    ArrayList<ProductDetails> cartlist = new ArrayList<ProductDetails>();
    TextView viewmore;
    View rootview;
    RecyclerView itemsview,allitemsview;
    ShareActionProvider myShareActionProvider;
    Button checkoutwithzappos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("I "+getEmijoByUnicode(0x2661)+" Zappos");
        animateBackgrounds();
        findViewByIds();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productslistwithoneitem!=null && productslistwithoneitem.size()!=0){
                    addToCart(productslistwithoneitem.get(0));
                }
                else{
                    animateFAB("Please search for valid product to add.");
                }
            }
        });
        MenuInflater inflater = getMenuInflater();
        allitemsview.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, float x, float y) {
                ProductDetails productforupperdisplay=productslist.get(position);
                setUpperRecycleView(productforupperdisplay);
            }
        }));

    }
    public String getEmijoByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }


    private void animateFAB(String s) {
        Animation rotation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        fab.startAnimation(rotation);
        Snackbar.make(fab, s,
                Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private void addToCart(final ProductDetails itemselected) {
        if(checkIfItemalreadyAdded(itemselected)){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Alert!!!");
            builder.setMessage("The item selected is already exists in your cart. Clicking yes will add another quantity of the existing item.");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int k) {
                    cartlist.add(itemselected);
                   updateBadgeCount();
                    animateFAB("Item added to Cart Successfully !!!");
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            final AlertDialog alert1 = builder.create();
            alert1.show();
        }

        else{
            cartlist.add(itemselected);
            updateBadgeCount();
            animateFAB("Item added to Cart Successfully !!!");
        }

    }

    private void updateBadgeCount() {
        badgecount.setVisibility(View.VISIBLE);
        badgecount.setText(cartlist.size()+"");
    }

    private boolean checkIfItemalreadyAdded(ProductDetails itemselected) {
        if(cartlist.contains(itemselected)){
            return true;
        }
        else{
            return false;
        }
    }

    private void animateBackgrounds() {
        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
        fade_in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();
    }

    private void findViewByIds() {
        upperdisplayimage=(ImageView) findViewById(R.id.imageView10);
        fab = (FloatingActionButton) findViewById(R.id.fab);
       itemsview=(RecyclerView) findViewById(R.id.recycleview);
        allitemsview=(RecyclerView) findViewById(R.id.recycleview10);
      layout= (DrawerLayout) findViewById(R.id.main_DrawerLayout);
        layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
      cartlistview= (RecyclerView) layout.findViewById(R.id.cartlistview);
        rootview=findViewById(R.id.clayout);
        checkoutwithzappos= (Button) findViewById(R.id.drawerButton1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        shareItem = menu.findItem(R.id.action_share);
         myShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareIntent(createShareIntent());
        shareItem.setVisible(false);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.layout_badge);
         cart = (RelativeLayout) MenuItemCompat.getActionView(item);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout.isDrawerOpen(Gravity.LEFT)){
                    closeDrawerIfOpen();
                }
                else{
                    displayCart(cartlist);
                }
            }
        });
        badgecount= (TextView) cart.findViewById(R.id.textOne);
        badgecount.setVisibility(View.INVISIBLE);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isConnected()) {
                    query = correctQueryifwrong(query);
                    configureRetrofit(query);
                }
                else{
                    setSnackBar("No Connection. Please check you internnet connection and retry");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return true;
    }

    private void setSnackBar(String s) {
        Snackbar.make(rootview,s,
                Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private String correctQueryifwrong(String query) {
        if(query.contains(" ")){
            query=query.replaceAll("\\s","");
        }
        return query;
    }

    private void configureRetrofit(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.zappos.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON(query,apikey);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                productslist = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
                setRecycleViews(productslist);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    private void setShareIntent(Intent shareIntent) {
        if (myShareActionProvider != null) {
            myShareActionProvider.setShareIntent(shareIntent);
        }
    }
    private Intent createShareIntent() {
         shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        return shareIntent;
    }
    private void displayCart(ArrayList<ProductDetails> cartlist) {
        if(cartlist.size()!=0 && cartlist!=null) {
                CartAdapter adapter = new CartAdapter(cartlist,MainActivity.this,R.layout.cart_item_layout);
                cartlistview.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                cartlistview.setLayoutManager(layoutManager);
                layout.openDrawer(Gravity.LEFT);
                layout.setScrimColor(ContextCompat.getColor(MainActivity.this, android.R.color.transparent));
        }
        else if(cartlist.size()==0 && productslist.size()==0 && productslistwithoneitem==null){
            displayToast("Your cart is empty. Start adding items you like.");
        }
        else{
            closeDrawerIfOpen();
            animateFAB("Your cart is empty. Start adding items you like.");
        }

    }



    private void setRecycleViews(ArrayList<ProductDetails> productslist) {
        if(productslist!=null && productslist.size()!=0) {
            getSupportActionBar().collapseActionView();
            viewFlipper.setVisibility(View.GONE);
            upperdisplayimage.setVisibility(View.GONE);
            closeDrawerIfOpen();
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainlayout);
            layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            alllistadapter = new CartAdapter(productslist,MainActivity.this,R.layout.all_items_layout);
            allitemsview.setAdapter(alllistadapter);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            layoutManager1.setSmoothScrollbarEnabled(true);
            allitemsview.setLayoutManager(layoutManager1);
            setUpperRecycleView(productslist.get(0));
            allitemsview.setNestedScrollingEnabled(false);
            fab.setVisibility(View.VISIBLE);


        }
        else{
            closeDrawerIfOpen();
            displayToast("Sorry there are no items that matched your query. Please try again.");
        }
    }

    private void setUpperRecycleView(ProductDetails productDetails) {
        productslistwithoneitem = new ArrayList<ProductDetails>();
        productslistwithoneitem.add(productDetails);
        CartAdapter adapter = new CartAdapter(productslistwithoneitem,MainActivity.this,R.layout.productdisplaylayout);
        itemsview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        itemsview.setLayoutManager(layoutManager);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this amazing product on Zappos.Follow the link to get there: "+productslistwithoneitem.get(0).getProductUrl().toString());
        myShareActionProvider.setShareIntent(shareIntent);
        shareItem.setVisible(true);
    }

    private void closeDrawerIfOpen() {
        if(layout.isDrawerOpen(Gravity.LEFT)){
            layout.closeDrawer(Gravity.LEFT);}
    }

    private void displayToast(String s) {

        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void deleteCartItem(int position) {
        cartlist.remove(position);
        updateBadgeCount();
        displayCart(cartlist);
    }

    @Override
    public void goToProductURL(int position) {
        if(productslistwithoneitem.get(position)!=null){
            Uri uri = Uri.parse(productslistwithoneitem.get(0).getProductUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);}
    }

    private boolean isConnected() {
        ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni= cm.getActiveNetworkInfo();
        if(ni!=null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Please Confirm !!!");
        builder.setMessage("Exit App?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        final AlertDialog alert1 = builder.create();
        alert1.show();
    }

    public void checkOutWithZappos(View view) {
        if(cartlist!=null && cartlist.size()!=0){
            Uri uri = Uri.parse("https://auth.zappos.com/ap/signin?openid.return_to=https%3A%2F%2Fauth.zappos.com%2Fzap%2FloginComplete&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.assoc_handle=zappos_us&openid.oa2.response_type=code&openid.mode=checkid_setup&openid.ns.oa2=http%3A%2F%2Fwww.amazon.com%2Fap%2Fext%2Foauth%2F2&siteState=1844bade-0432-4a13-8fd8-567b71e92c06%7E1486743004460%3A2UEMQK2bkghXxPOsFtAcWvjwOE%2FSYUtxbNWQ8jP1rfQ%3D%3Anull&openid.oa2.scope=auth_code&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.oa2.client_id=iba%3Aamzn1.application-oa2-client.ce5075dead7c4aa7ae316059988839d5&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);}
        else {
            displayToast("Your cart is empty. Add items you like and proceed to checkout");
        }

    }
}
