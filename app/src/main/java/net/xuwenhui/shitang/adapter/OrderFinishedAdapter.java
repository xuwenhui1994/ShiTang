package net.xuwenhui.shitang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hedgehog.ratingbar.RatingBar;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import net.xuwenhui.core.ActionCallbackListener;
import net.xuwenhui.core.AppAction;
import net.xuwenhui.model.Evaluation;
import net.xuwenhui.model.Order;
import net.xuwenhui.shitang.R;
import net.xuwenhui.shitang.activity.OrderDetailActivity;
import net.xuwenhui.shitang.util.DateHandleUtil;
import net.xuwenhui.shitang.util.DensityUtils;
import net.xuwenhui.shitang.util.T;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 订单已完成适配器
 * <p/>
 * Created by xwh on 2016/5/2.
 */
public class OrderFinishedAdapter extends CommonAdapter<Order> {

	AppAction mAppAction;

	public OrderFinishedAdapter(Context context, List<Order> dataList, AppAction appAction) {
		super(context, dataList);
		mAppAction = appAction;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(mContext).inflate(R.layout.item_order_finished, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		final ViewHolder viewHolder = (ViewHolder) holder;

		final Order order = mDataList.get(position);
		viewHolder.mTvName.setText(order.getName());
		viewHolder.mTvTime.setText(order.getCreate_time());
		viewHolder.mTvTotalPrice.setText("总价 ￥" + order.getTotal_price());

		// 初始化订单项列表
		viewHolder.mListOrderItem.setLayoutManager(new LinearLayoutManager(mContext));
		viewHolder.mListOrderItem.setItemAnimator(new DefaultItemAnimator());
		viewHolder.mListOrderItem.setAdapter(new OrderItem2Adapter(mContext, order.getOrderItemList()));
		if (order.getImage_src().equals("")) {
			Picasso.with(mContext).load(R.mipmap.ic_launcher).into(viewHolder.mCircleImageShop);
		} else {
			Picasso.with(mContext).load(order.getImage_src())
					.resize(DensityUtils.dp2px(mContext, 48), DensityUtils.dp2px(mContext, 48))
					.centerCrop().into(viewHolder.mCircleImageShop);
		}

		// 设置点击事件
		// 根据评价信息设置外观及点击事件
		if (!order.is_evaluate()) {
			viewHolder.mBtnEvaluate.setText("去评价");
			viewHolder.mBtnEvaluate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
							.title("去评价")
							.customView(R.layout.dialog_evaluate, true)
							.negativeText(R.string.disagree)
							.positiveText(R.string.agree)
							.build();
					final Map<String, String> map = new HashMap<>();
					map.put("star", String.valueOf(0));
					map.put("content", "");
					final RatingBar ratingBar = (RatingBar) dialog.getCustomView().findViewById(R.id.ratingBar);
					final EditText edtContent = (EditText) dialog.getCustomView().findViewById(R.id.edt_content);
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
					ratingBar.setStarFillDrawable(fill);
					ratingBar.setStarEmptyDrawable(empty);
					ratingBar.setStarHalfDrawable(half);
					ratingBar.setStar(0);

					ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
						@Override
						public void onRatingChange(int RatingCount) {
							map.put("star", String.valueOf(RatingCount));
						}
					});

					dialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							map.put("content", edtContent.getText().toString().trim());
							if (map.get("star").equals(String.valueOf(0))) {
								T.show(mContext, "评价星级不能为空");
								return;
							}
							if (TextUtils.isEmpty(map.get("content"))) {
								T.show(mContext, "评价内容不能为空");
								return;
							}

							mAppAction.evaluation_create(order.getOrder_id(), Integer.parseInt(map.get("star")), map.get("content"), DateHandleUtil.convertToStandard(new Date()), new ActionCallbackListener<Evaluation>() {
								@Override
								public void onSuccess(Evaluation data) {
									T.show(mContext, "评价成功");
									order.setEvaluate(true);
									notifyItemChanged(position);
								}

								@Override
								public void onFailure(String errorCode, String errorMessage) {
									T.show(mContext, errorMessage);
								}
							});
							dialog.dismiss();
						}
					});
					dialog.show();
				}
			});
		} else {
			viewHolder.mBtnEvaluate.setText("查看评价");
			viewHolder.mBtnEvaluate.setBackgroundColor(Color.BLACK);
			viewHolder.mBtnEvaluate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MaterialDialog dialog = new MaterialDialog.Builder(mContext)
							.title("查看评价")
							.customView(R.layout.dialog_evaluate, true)
							.positiveText(R.string.agree)
							.build();
					final RatingBar ratingBar = (RatingBar) dialog.getCustomView().findViewById(R.id.ratingBar);
					final EditText edtContent = (EditText) dialog.getCustomView().findViewById(R.id.edt_content);
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
					ratingBar.setStarFillDrawable(fill);
					ratingBar.setStarEmptyDrawable(empty);
					ratingBar.setStarHalfDrawable(half);
					ratingBar.setEnabled(false);
					// 设置评论内容
					edtContent.setEnabled(false);

					mAppAction.evaluation_query(order.getOrder_id(), new ActionCallbackListener<Evaluation>() {
						@Override
						public void onSuccess(Evaluation data) {
							ratingBar.setStar(data.getStar());
							edtContent.setText(data.getContent());
						}

						@Override
						public void onFailure(String errorCode, String errorMessage) {
							T.show(mContext, errorMessage);
							// 测试数据
							ratingBar.setStar(4);
							edtContent.setText("很好，味道不错。");
						}
					});
					dialog.show();
				}
			});
		}

		// 查看订单详情界面
		viewHolder.mLayoutMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mContext, OrderDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("Order", order);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.tv_time)
		TextView mTvTime;
		@Bind(R.id.layout_more)
		RelativeLayout mLayoutMore;
		@Bind(R.id.circle_image_shop)
		CircleImageView mCircleImageShop;
		@Bind(R.id.tv_name)
		TextView mTvName;
		@Bind(R.id.layout1)
		RelativeLayout mLayout1;
		@Bind(R.id.list_order_item)
		RecyclerView mListOrderItem;
		@Bind(R.id.tv_total_price)
		TextView mTvTotalPrice;
		@Bind(R.id.layout2)
		RelativeLayout mLayout2;
		@Bind(R.id.btn_evaluate)
		Button mBtnEvaluate;
		@Bind(R.id.card_order_finished)
		CardView mCardOrderFinished;

		ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}
}
