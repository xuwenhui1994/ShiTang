package net.xuwenhui.shitang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.xuwenhui.model.Shop;
import net.xuwenhui.shitang.R;
import net.xuwenhui.shitang.activity.ShopDetailActivity;
import net.xuwenhui.shitang.util.DensityUtils;
import net.xuwenhui.shitang.util.T;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商店适配器
 * <p/>
 * Created by xwh on 2016/4/15.
 */
public class ShopAdapter extends CommonAdapter<Shop> {

	public ShopAdapter(Context context, List<Shop> dataList) {
		super(context, dataList);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.item_shop, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		final ViewHolder viewHolder = (ViewHolder) holder;

		final Shop shop = mDataList.get(position);
		viewHolder.mTvName.setText(shop.getName());
		viewHolder.mTvSort.setText(shop.getSort_desc());
		viewHolder.mTvDeliveryTime.setText("约" + shop.getDelivery_time() + "分钟");
		viewHolder.mTvSell.setText("月售" + shop.getSell() + "单");

		// 设置评分条外观
		Drawable fill = new IconicsDrawable(mContext)
				.icon(GoogleMaterial.Icon.gmd_star)
				.color(mContext.getResources().getColor(R.color.colorPrimary))
				.sizeDp(20);
		Drawable empty = new IconicsDrawable(mContext)
				.icon(GoogleMaterial.Icon.gmd_star_border)
				.color(mContext.getResources().getColor(R.color.colorPrimary))
				.sizeDp(20);
		Drawable half = new IconicsDrawable(mContext)
				.icon(GoogleMaterial.Icon.gmd_star_half)
				.color(mContext.getResources().getColor(R.color.colorPrimary))
				.sizeDp(20);
		viewHolder.mRatingBar.setStarFillDrawable(fill);
		viewHolder.mRatingBar.setStarEmptyDrawable(empty);
		viewHolder.mRatingBar.setStarHalfDrawable(half);
		viewHolder.mRatingBar.setStar(shop.getAvg_star());

		if (shop.getImage_src().equals("")) {
			Picasso.with(mContext).load(R.mipmap.ic_launcher).into(viewHolder.mImgShop);
		} else {
			Picasso.with(mContext).load(shop.getImage_src())
					.resize(DensityUtils.dp2px(mContext, 64), DensityUtils.dp2px(mContext, 64))
					.centerCrop()
					.into(viewHolder.mImgShop, new Callback() {
						@Override
						public void onSuccess() {
							Bitmap bitmap = ((BitmapDrawable) viewHolder.mImgShop.getDrawable()).getBitmap();
							if (bitmap != null) {
								Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
									@Override
									public void onGenerated(Palette palette) {
										Palette.Swatch vibrant = palette.getVibrantSwatch();
										if (vibrant != null) {
											viewHolder.mCardShop.setBackgroundColor(vibrant.getRgb());
										}
									}
								});
							}
						}

						@Override
						public void onError() {

						}
					});
		}

		// 设置点击事件
		viewHolder.mCardShop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				T.show(mContext, viewHolder.mTvName.getText());
				Intent intent = new Intent(mContext, ShopDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("Shop", shop);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.img_shop)
		ImageView mImgShop;
		@Bind(R.id.tv_name)
		TextView mTvName;
		@Bind(R.id.tv_sort)
		TextView mTvSort;
		@Bind(R.id.tv_delivery_time)
		TextView mTvDeliveryTime;
		@Bind(R.id.layout1)
		LinearLayout mLayout1;
		@Bind(R.id.ratingBar)
		RatingBar mRatingBar;
		@Bind(R.id.tv_sell)
		TextView mTvSell;
		@Bind(R.id.layout2)
		LinearLayout mLayout2;
		@Bind(R.id.card_shop)
		CardView mCardShop;

		ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}
