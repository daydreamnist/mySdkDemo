package com.broadlink.mysdkdemo.commonUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.broadlink.mysdkdemo.R;
import com.broadlink.mysdkdemo.activity.MainActivity;
import com.broadlink.mysdkdemo.activity.ProductManageActivity;
import com.broadlink.mysdkdemo.model.ProductInfo;

import java.util.List;

import cn.com.broadlink.sdk.BLLet;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    public static final String PRODUCT_ICON_PATH = "/ec4/v1/system/configfile";

    private OnItemClickListener onItemClickListener;

    private List<ProductInfo> productInfos;

    private Context context;


    public ProductRecyclerAdapter(List<ProductInfo> productInfos) {
        this.productInfos = productInfos;
    }

    @NonNull
    @Override
    public ProductRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_product_item,viewGroup,false);
        if (onItemClickListener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(v,(Integer) v.getTag());
                    return true;
                }
            });
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerAdapter.ViewHolder viewHolder, int i) {
        ProductInfo productInfo = productInfos.get(i);
        viewHolder.mTv_productInfo.setText(productInfo.getName());
        String imageUrl = getProductIconUrl(productInfo.getShortcuticon());
        new ImageLoadTask(imageUrl, new loadCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                viewHolder.mIv_productIcon.setImageBitmap(bitmap);
            }
        }).execute();
    }

    private String getProductIconUrl(String shortcuticon) {
        String lid = BLLet.getLicenseId();
        String basePath = String.format(ProductManageActivity.BASE_APP_MANAGE,lid);

        StringBuilder stringBuilder = new StringBuilder(basePath);
        stringBuilder.append(PRODUCT_ICON_PATH);
        stringBuilder.append(shortcuticon);
        return stringBuilder.toString();
    }


    @Override
    public int getItemCount() {
        return productInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView mIv_productIcon;
        private TextView mTv_productInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIv_productIcon = itemView.findViewById(R.id.mIv_productIcon);
            mTv_productInfo = itemView.findViewById(R.id.mTv_productInfo);

        }
    }
}
