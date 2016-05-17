package net.xuwenhui.shitang.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import net.xuwenhui.shitang.R;
import net.xuwenhui.shitang.fragment.HomeFragment;
import net.xuwenhui.shitang.fragment.OrderFragment;
import net.xuwenhui.shitang.fragment.PersonFragment;
import net.xuwenhui.shitang.fragment.SettingFragment;
import net.xuwenhui.shitang.fragment.admin.AdminFragment;
import net.xuwenhui.shitang.fragment.merchant.ShopFragment;
import net.xuwenhui.shitang.util.DensityUtils;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主界面
 * <p/>
 * Created by xwh on 2016/3/15.
 */
public class MainActivity extends BaseActivity {

	@Bind(R.id.toolbar)
	Toolbar mToolbar;
	@Bind(R.id.frame_content)
	FrameLayout mFrameContent;

	private Drawer mDrawer;

	/**
	 * 更新信息
	 */
	public void updateInfo() {
		// 设置抽屉顶部
		setupDrawerHeader();
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initData() {
		// 设置toolbar
		mToolbar.setTitle("首页");
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(false);
		// 设置抽屉导航菜单
		setupDrawerMenu();
		// 设置抽屉顶部
		setupDrawerHeader();
	}

	@Override
	protected void initListener() {
		/**
		 * 抽屉导航菜单点击事件
		 */
		mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
			@Override
			public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
				if (drawerItem != null) {
					// 设置气泡
					if (drawerItem instanceof Badgeable) {
						Badgeable badgeable = (Badgeable) drawerItem;
						if (badgeable.getBadge() != null) {
							//note don't do this if your badge contains a "+"
							//only use toString() if you set the test as String
							int badge = Integer.valueOf(badgeable.getBadge().toString());
							if (badge > 0) {
								badgeable.withBadge(String.valueOf("0"));
								mDrawer.updateItem(drawerItem);
							}
						}
					}
					// 跳转界面
					switch ((int) drawerItem.getIdentifier()) {
						case 1:
							getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new HomeFragment()).commit();
							mToolbar.setTitle("首页");
							break;
						case 2:
							getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new OrderFragment()).commit();
							mToolbar.setTitle("我的订单");
							break;
						case 3:
							getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new PersonFragment()).commit();
							mToolbar.setTitle("我的信息");
							break;
						case 4:
							getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new SettingFragment()).commit();
							mToolbar.setTitle("设置");
							break;
						case 5:
							getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new ShopFragment()).commit();
							mToolbar.setTitle("我的店铺");
							break;
						case 6:
							getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new AdminFragment()).commit();
							mToolbar.setTitle("管理员专属");
						default:
					}
				}
				return false;
			}
		});
		mDrawer.setSelectionAtPosition(1); // 默认首页，管理员为专属界面
	}

	/**
	 * 设置抽屉导航菜单（分角色）
	 */
	private void setupDrawerMenu() {
		PrimaryDrawerItem item_home = new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1);
		PrimaryDrawerItem item_order = new PrimaryDrawerItem().withName(R.string.drawer_item_order).withIcon(GoogleMaterial.Icon.gmd_assignment).withIdentifier(2);
		PrimaryDrawerItem item_person = new PrimaryDrawerItem().withName(R.string.drawer_item_person).withIcon(GoogleMaterial.Icon.gmd_person).withIdentifier(3);
		SecondaryDrawerItem item_settings = (SecondaryDrawerItem) new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(4);
		PrimaryDrawerItem item_shop_management = new PrimaryDrawerItem().withName(R.string.drawer_item_shop).withIcon(GoogleMaterial.Icon.gmd_local_dining).withIdentifier(5);
		PrimaryDrawerItem item_admin = new PrimaryDrawerItem().withName(R.string.drawer_item_admin).withIcon(GoogleMaterial.Icon.gmd_security).withIdentifier(6);

		if (mApplication.getUser() == null) {
			// 游客
			mDrawer = new DrawerBuilder()
					.withActivity(this)
					.withHeader(R.layout.layout_drawer_header)
					.withToolbar(mToolbar)
					.addDrawerItems(
							item_home,
							new SectionDrawerItem().withName(R.string.drawer_item_others),
							item_settings
					).build();
		} else if (mApplication.getUser().getRole_id() == 2) {
			// 普通用户
			mDrawer = new DrawerBuilder()
					.withActivity(this)
					.withHeader(R.layout.layout_drawer_header)
					.withToolbar(mToolbar)
					.addDrawerItems(
							item_home,
							item_order,
							item_person,
							new SectionDrawerItem().withName(R.string.drawer_item_others),
							item_settings
					).build();
		} else if (mApplication.getUser().getRole_id() == 3) {
			// 商家
			mDrawer = new DrawerBuilder()
					.withActivity(this)
					.withHeader(R.layout.layout_drawer_header)
					.withToolbar(mToolbar)
					.addDrawerItems(
							item_home,
							item_shop_management,
							item_order,
							item_person,
							new SectionDrawerItem().withName(R.string.drawer_item_others),
							item_settings
					).build();
		} else if (mApplication.getUser().getRole_id() == 1) {
			// 管理员
			mDrawer = new DrawerBuilder()
					.withActivity(this)
					.withHeader(R.layout.layout_drawer_header)
					.withToolbar(mToolbar)
					.addDrawerItems(
							item_admin,
							new SectionDrawerItem().withName(R.string.drawer_item_others),
							item_settings
					).build();
		}
	}

	/**
	 * 设置抽屉顶部
	 */
	private void setupDrawerHeader() {
		if (mApplication.getUser() == null) {
			// 没登录，则点击跳转登录界面
			CircleImageView circle_image_person = (CircleImageView) mDrawer.getHeader().findViewById(R.id.circle_image_person);
			TextView tv_nickname = (TextView) mDrawer.getHeader().findViewById(R.id.tv_nickname);
			Picasso.with(mContext).load(R.mipmap.ic_launcher).into(circle_image_person);
			tv_nickname.setText("登录/注册");
			mDrawer.getHeader().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
				}
			});
		} else {
			CircleImageView circle_image_person = (CircleImageView) mDrawer.getHeader().findViewById(R.id.circle_image_person);
			TextView tv_nickname = (TextView) mDrawer.getHeader().findViewById(R.id.tv_nickname);
			if (mApplication.getUser().getImage_src().equals("")) {
				Picasso.with(mContext).load(R.mipmap.ic_launcher).into(circle_image_person);
			} else {
				Picasso.with(mContext).load(mApplication.getUser().getImage_src())
						.resize(DensityUtils.dp2px(mContext, 96), DensityUtils.dp2px(mContext, 96))
						.centerCrop().into(circle_image_person);
			}
			if (mApplication.getUser().getNickname().equals("")) {
				tv_nickname.setText("暂无昵称");
			} else {
				tv_nickname.setText(mApplication.getUser().getNickname());
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		//handle the back press :D close the drawer first and if the drawer is closed close the activity
		if (mDrawer != null && mDrawer.isDrawerOpen()) {
			mDrawer.closeDrawer();
		} else {
			super.onBackPressed();
		}
	}

}
