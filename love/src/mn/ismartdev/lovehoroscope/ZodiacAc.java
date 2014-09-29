package mn.ismartdev.lovehoroscope;

import java.sql.SQLException;
import java.util.List;

import mn.ismartdev.horoscope.model.DatabaseHelper;
import mn.ismartdev.horoscope.model.Zodiac;
import mn.ismartdev.lovehoroscope.utils.FacebookLogin;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.flavienlaurent.notboringactionbar.AlphaForegroundColorSpan;
import com.flavienlaurent.notboringactionbar.KenBurnsSupportView;
import com.nineoldandroids.view.ViewHelper;

public class ZodiacAc extends ActionBarActivity implements ScrollTabHolder,
		ViewPager.OnPageChangeListener, OnClickListener {

	private KenBurnsSupportView mHeaderPicture;
	private View mHeader;
	private ImageView fb;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	Animation clickAnim;
	View v;
	private int mActionBarHeight;
	private int mMinHeaderHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	private String[] signs = { "Хонь", "Үхэр", "Ихэр", "Мэлхий", "Арслан",
			"Охин", "Жинлүүр", "Хилэнц", "Нум", "Матар", "Хумх", "Загас", };
	private Bundle b;
	private TypedValue mTypedValue = new TypedValue();
	private SpannableString mSpannableString;
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private ActionBar bar;
	private List<Zodiac> zodList;
	private DatabaseHelper helper;
	private int sex;
	private String shareDesc = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMinHeaderHeight = this.getResources().getDimensionPixelSize(
				R.dimen.min_header_height);
		mHeaderHeight = this.getResources().getDimensionPixelSize(
				R.dimen.header_height);
		mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();

		setContentView(R.layout.home_frag);
		// share
		fb = (ImageView) findViewById(R.id.fb_share);
		clickAnim = AnimationUtils.loadAnimation(this,
				R.anim.but_click);
		clickAnim.setRepeatCount(Animation.INFINITE);
		
		fb.setOnClickListener(this);
		fb.setAnimation(clickAnim);
		
		mHeaderPicture = (KenBurnsSupportView) findViewById(R.id.header_picture);

		mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(1);

		b = getIntent().getExtras();
		sex = b.getInt("gender", 1);
		if (sex == 2)
			mHeaderPicture.setResourceIds(R.drawable.five, R.drawable.four,
					R.drawable.three, R.drawable.two, R.drawable.one);
		else
			mHeaderPicture.setResourceIds(R.drawable.mfive, R.drawable.mfour,
					R.drawable.mthree, R.drawable.mtwo, R.drawable.mone);
		mHeader = findViewById(R.id.header);
		helper = new DatabaseHelper(this);
		try {
			zodList = helper.getZodiacDao().queryBuilder().orderBy("id", true)
					.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPagerAdapter = new PagerAdapter(this.getSupportFragmentManager(),
				zodList);
		mPagerAdapter.setTabHolderScrollingContent(this);
		mPagerAdapter.notifyDataSetChanged();
		mViewPager.setAdapter(mPagerAdapter);
		
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip.setOnPageChangeListener(this);

		mSpannableString = new SpannableString(getString(R.string.app_name));
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);

		// ViewHelper.setAlpha(getActionBarIconView(), 0f);
		bar = ((ActionBarActivity) this).getSupportActionBar();
		bar.setBackgroundDrawable(null);
		bar.setHomeButtonEnabled(true);
		bar.setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// nothing
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// nothing
	}

	@Override
	public void onPageSelected(int position) {

		SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter
				.getScrollTabHolders();
		ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
		shareDesc = signs[position] + " ордны Хайр дурлал: "
				+ currentHolder.getDesc();
		currentHolder.adjustScroll((int) (mHeader.getHeight() + ViewHelper
				.getTranslationY(mHeader)));
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount, int pagePosition) {
		if (mViewPager.getCurrentItem() == pagePosition) {
			int scrollY = getScrollY(view);
			ViewHelper.setTranslationY(mHeader,
					Math.max(-scrollY, mMinHeaderTranslation));
			float ratio = clamp(ViewHelper.getTranslationY(mHeader)
					/ mMinHeaderTranslation, 0.0f, 1.0f);

			setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
		}
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		// nothing
	}

	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mHeaderHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			getTheme().resolveAttribute(android.R.attr.actionBarSize,
					mTypedValue, true);
		} else {
			getTheme()
					.resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
		}

		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());
		return mActionBarHeight;
	}

	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		bar.setTitle(mSpannableString);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private ImageView getActionBarIconView() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return (ImageView) v.findViewById(android.R.id.home);
		}

		return (ImageView) v
				.findViewById(android.support.v7.appcompat.R.id.home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// getMenuInflater().inflate(R.menu.ad_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		// if (id == R.id.action_search_ad) {
		// mPagerAdapter.filterCar();
		// }
		if (id == android.R.id.home)
			onBackPressed();
		return true;
	}

	public class PagerAdapter extends FragmentPagerAdapter {

		private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
		private final List<Zodiac> TITLES;
		private ScrollTabHolder mListener;

		public PagerAdapter(FragmentManager fm, List<Zodiac> titles) {
			super(fm);
			this.TITLES = titles;
			mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
		}

		public void setTabHolderScrollingContent(ScrollTabHolder listener) {
			mListener = listener;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return signs[TITLES.get(position).zodiac_id - 1];
		}

		@Override
		public int getCount() {
			return TITLES.size();
		}

		@Override
		public Fragment getItem(int position) {
			ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) SampleListFragment
					.newInstance(position, sex);

			mScrollTabHolders.put(position, fragment);
			if (mListener != null) {
				fragment.setScrollTabHolder(mListener);
			}

			return fragment;
		}

		public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
			return mScrollTabHolders;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == fb) {
			onPageSelected(mViewPager.getCurrentItem());
			Bundle b = new Bundle();
			b.putString("desc", shareDesc);
			Intent share = new Intent(ZodiacAc.this, FacebookLogin.class);
			share.putExtras(b);
			startActivity(share);
		}
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return "";
	}

}
