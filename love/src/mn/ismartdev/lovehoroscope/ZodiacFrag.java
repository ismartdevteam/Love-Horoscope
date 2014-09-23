package mn.ismartdev.lovehoroscope;

import java.sql.SQLException;
import java.util.List;

import mn.ismartdev.horoscope.model.DatabaseHelper;
import mn.ismartdev.horoscope.model.Zodiac;
import android.annotation.TargetApi;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.flavienlaurent.notboringactionbar.AlphaForegroundColorSpan;
import com.flavienlaurent.notboringactionbar.KenBurnsSupportView;
import com.nineoldandroids.view.ViewHelper;

public class ZodiacFrag extends Fragment implements ScrollTabHolder,
		ViewPager.OnPageChangeListener {

	private KenBurnsSupportView mHeaderPicture;
	private View mHeader;

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	View v;
	private int mActionBarHeight;
	private int mMinHeaderHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;

	private Bundle b;
	private TypedValue mTypedValue = new TypedValue();
	private SpannableString mSpannableString;
	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private ActionBar bar;
	private List<Zodiac> zodList;
	private DatabaseHelper helper;
	private int sex;

	public ZodiacFrag() {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		b = getArguments();
		sex = b.getInt("gender", 1);
		helper = new DatabaseHelper(getActivity());
		try {
			zodList = helper.getZodiacDao().queryBuilder().orderBy("id", true)
					.query();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPagerAdapter = new PagerAdapter(getActivity()
				.getSupportFragmentManager(), zodList);
		mPagerAdapter.setTabHolderScrollingContent(this);

		mViewPager.setAdapter(mPagerAdapter);

		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip.setOnPageChangeListener(this);
		mSpannableString = new SpannableString(getString(R.string.app_name));
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);

		// ViewHelper.setAlpha(getActionBarIconView(), 0f);
		bar = ((ActionBarActivity) getActivity()).getSupportActionBar();
		bar.setBackgroundDrawable(null);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMinHeaderHeight = getActivity().getResources().getDimensionPixelSize(
				R.dimen.min_header_height);
		mHeaderHeight = getActivity().getResources().getDimensionPixelSize(
				R.dimen.header_height);
		mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();

		v = inflater.inflate(R.layout.home_frag, container, false);
		Log.e("header", mMinHeaderTranslation + "");
		mHeaderPicture = (KenBurnsSupportView) v
				.findViewById(R.id.header_picture);
		if(sex==2)
		mHeaderPicture.setResourceIds(R.drawable.five, R.drawable.four,
				R.drawable.three, R.drawable.two, R.drawable.one);
		else
			mHeaderPicture.setResourceIds(R.drawable.mfive, R.drawable.mfour,
					R.drawable.mthree, R.drawable.mtwo, R.drawable.mone);
		mHeader = v.findViewById(R.id.header);

		mPagerSlidingTabStrip = (PagerSlidingTabStrip) v
				.findViewById(R.id.tabs);
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(4);

		return v;
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
			getActivity().getTheme().resolveAttribute(
					android.R.attr.actionBarSize, mTypedValue, true);
		} else {
			getActivity().getTheme().resolveAttribute(R.attr.actionBarSize,
					mTypedValue, true);
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

	public class PagerAdapter extends FragmentPagerAdapter {

		private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
		private final List<Zodiac> TITLES;
		private ScrollTabHolder mListener;
		private String[] signs = { "Хонь", "Үхэр", "Ихэр", "Мэлхий", "Арслан",
				"Охин", "Жинлүүр", "Хилэнц", "Нум", "Матар", "Хумх", "Загас", };

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

}
