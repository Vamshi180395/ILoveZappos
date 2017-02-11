package com.example.ilovezappos;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Rama Vamshi Krishna on 2/7/2017.
 */
public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.BindingHolder> {
    private List<ProductDetails> items;
    static MainActivity activity;
    CartInterface cartinterface;
    int layoutid;
    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
            ImageView deleteview= (ImageView) v.findViewById(R.id.imageView6);
            TextView clicktoknowmore= (TextView) v.findViewById(R.id.txtviewmore);
            if(deleteview!=null) {
                deleteview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.deleteCartItem(getAdapterPosition());
                    }
                });
            }
            if(clicktoknowmore!=null) {

                    clicktoknowmore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.goToProductURL(getAdapterPosition());
                        }
                    });

            }

        }

        public ViewDataBinding getBinding() {

            return binding;
        }
    }

    public CartAdapter(List<ProductDetails> recyclerUsers,MainActivity activity,int layoutid) {
        this.items = recyclerUsers;
        this.activity=activity;
        this.layoutid=layoutid;
    }

    public CartAdapter() {
        super();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int type) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layoutid, parent, false);
        final BindingHolder holder = new BindingHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final ProductDetails item = items.get(position);
        holder.getBinding().setVariable(com.example.ilovezappos.BR.productdetails, item);
        holder.getBinding().executePendingBindings();

    }

    @Override
    public int getItemCount() {

        return items.size();
    }
public interface CartInterface{
        public void deleteCartItem(int position);
       public void goToProductURL(int position);
    }
}
