package net.xuwenhui.shitang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.iconics.view.IconicsImageView;

import net.xuwenhui.core.ActionCallbackListener;
import net.xuwenhui.model.Address;
import net.xuwenhui.model.Order;
import net.xuwenhui.model.OrderItem;
import net.xuwenhui.shitang.R;
import net.xuwenhui.shitang.adapter.OrderItem2Adapter;
import net.xuwenhui.shitang.util.DateHandleUtil;
import net.xuwenhui.shitang.util.T;

import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 确认订单界面
 * <p/>
 * Created by xwh on 2016/4/28.
 */
public class ConfirmOrderActivity extends BaseActivity {

	@Bind(R.id.toolbar)
	Toolbar mToolbar;
	@Bind(R.id.btn_order)
	Button mBtnOrder;
	@Bind(R.id.tv_total_price)
	TextView mTvTotalPrice;
	@Bind(R.id.layout_confirm_order_bottom)
	RelativeLayout mLayoutConfirmOrderBottom;
	@Bind(R.id.icon_location)
	IconicsImageView mIconLocation;
	@Bind(R.id.icon_forward)
	IconicsImageView mIconForward;
	@Bind(R.id.tv_address_1)
	TextView mTvAddress1;
	@Bind(R.id.tv_address_2)
	TextView mTvAddress2;
	@Bind(R.id.layout_address)
	RelativeLayout mLayoutAddress;
	@Bind(R.id.img_divider1)
	ImageView mImgDivider1;
	@Bind(R.id.icon_note)
	IconicsImageView mIconNote;
	@Bind(R.id.tv_note)
	TextView mTvNote;
	@Bind(R.id.layout_note)
	RelativeLayout mLayoutNote;
	@Bind(R.id.img_divider2)
	ImageView mImgDivider2;
	@Bind(R.id.icon_shop)
	IconicsImageView mIconShop;
	@Bind(R.id.tv_order_title)
	TextView mTvOrderTitle;
	@Bind(R.id.layout_shop)
	RelativeLayout mLayoutShop;
	@Bind(R.id.list_order_item)
	RecyclerView mListOrderItem;
	@Bind(R.id.layout_order)
	RelativeLayout mLayoutOrder;

	OrderItem2Adapter mOrderItem2Adapter;

	int shop_id;
	float total_price;
	List<OrderItem> orderItemList;

	Address address;

	private static final int REQUEST_CODE_CHOOSE_ADDRESS = 1;//获取选择收货地址信息

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_confirm_order;
	}

	@Override
	protected void initData() {
		// 设置toolbar
		mToolbar.setTitle("确认订单");
		setSupportActionBar(mToolbar);
		// 设置返回键<-
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});

		// 获取Intent
		Bundle bundle = getIntent().getExtras();
		shop_id = bundle.getInt("shop_id");
		total_price = bundle.getFloat("total_price");
		orderItemList = (List<OrderItem>) bundle.getSerializable("OrderItemList");
		mTvNote.setTag("");
		// 初始化订单列表
		mListOrderItem.setLayoutManager(new LinearLayoutManager(mContext));
		mListOrderItem.setItemAnimator(new DefaultItemAnimator());
		mOrderItem2Adapter = new OrderItem2Adapter(mContext, orderItemList);
		mListOrderItem.setAdapter(mOrderItem2Adapter);
		// 初始化总价
		mTvTotalPrice.setText("总价 " + total_price);
	}

	@Override
	protected void initListener() {
		// 管理收货地址
		mLayoutAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mContext, AddressActivity.class);
				startActivityForResult(intent, REQUEST_CODE_CHOOSE_ADDRESS);
			}
		});

		// 填写备注
		mLayoutNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new MaterialDialog.Builder(mContext)
						.title("填写备注")
						.negativeText(R.string.disagree)
						.positiveText(R.string.agree)
						.input("", "", false, new MaterialDialog.InputCallback() {
							@Override
							public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
								mTvNote.setText(input.toString());
								mTvNote.setTag(input.toString());
							}
						}).show();
			}
		});

		// 确认订单
		mBtnOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (address == null) {
					T.show(mContext, "请选择一个收货地址！");
					return;
				}
				mAppAction.order_create(mApplication.getUser().getUser_id(), shop_id, address.getAddress_id(), DateHandleUtil.convertToStandard(new Date()), total_price, (String) mTvNote.getTag(), orderItemList, new ActionCallbackListener<Order>() {
					@Override
					public void onSuccess(Order data) {
						T.show(mContext, "订单已生成，请赶快支付。");
						Intent intent = new Intent(mContext, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}

					@Override
					public void onFailure(String errorCode, String errorMessage) {
						T.show(mContext, errorMessage);
					}
				});
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_CHOOSE_ADDRESS) {
			if (resultCode == Activity.RESULT_OK) {
				address = (Address) data.getExtras().getSerializable("Address");
				// 显示选择收货地址信息
				mTvAddress1.setText(address.getName() + " " + address.getSex() + address.getPhone_num());
				mTvAddress2.setText(address.getAddress_desc());
			}
		}
	}
}
