package mn.ismartdev.lovehoroscope;

import java.sql.SQLException;
import java.util.List;

import mn.ismartdev.horoscope.model.DatabaseHelper;
import mn.ismartdev.horoscope.model.Zodiac;
import mn.ismartdev.horoscope.text.Bold;
import mn.ismartdev.horoscope.text.Regular;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class SampleListFragment extends ScrollTabHolderFragment implements
		OnScrollListener {

	private static final String ARG_POSITION = "position";

	private ListView mListView;
	private List<Zodiac> mListItems;
	private int FemaleiconResId[] = { R.drawable.aries, R.drawable.taurus,
			R.drawable.gemini, R.drawable.cancer, R.drawable.leo,
			R.drawable.virgo, R.drawable.libra, R.drawable.scorpio,
			R.drawable.sagittarius, R.drawable.capricorn, R.drawable.aquarius,
			R.drawable.pisces };
	private int MaleiconResId[] = { R.drawable.maries, R.drawable.mtaurus,
			R.drawable.mgemini, R.drawable.mcancer, R.drawable.mleo,
			R.drawable.mvirgo, R.drawable.mlibra, R.drawable.mscorpio,
			R.drawable.msagi, R.drawable.mcapricorn, R.drawable.maqua,
			R.drawable.mpisce };
	private int mPosition;
	private int mSex;
	private DatabaseHelper helper;
	private String shareText = "";

	public static Fragment newInstance(int position, int sex) {
		SampleListFragment f = new SampleListFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		b.putInt("sex", sex);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPosition = getArguments().getInt(ARG_POSITION);
		mSex = getArguments().getInt("sex");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list, container, false);

		mListView = (ListView) v.findViewById(R.id.listView);

		View placeHolderView = inflater.inflate(
				R.layout.view_header_placeholder, mListView, false);
		mListView.addHeaderView(placeHolderView);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		helper = new DatabaseHelper(getActivity());
		try {
			mListItems = helper.getZodiacDao().queryForEq("id", mPosition + 1);
			mListView.setOnScrollListener(this);
			if (mSex == 2)
				mListView.setAdapter(new ZodiacFAdapter(getActivity(),
						mListItems));
			if (mSex == 1)
				mListView.setAdapter(new ZodiacMAdapter(getActivity(),
						mListItems));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void adjustScroll(int scrollHeight) {
		if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
			return;
		}

		mListView.setSelectionFromTop(1, scrollHeight);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mScrollTabHolder != null)
			mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount, mPosition);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// nothing
	}

	private class ZodiacFAdapter extends ArrayAdapter<Zodiac> {
		Context mContext;

		public ZodiacFAdapter(Context context, List<Zodiac> objects) {
			super(context, 0, 0, objects);
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			Zodiac zodiac = getItem(position);

			Holder hol = null;
			if (v == null) {
				v = ((Activity) mContext).getLayoutInflater().inflate(
						R.layout.f_list_item, parent, false);
				hol = new Holder();
				hol.image = (ImageView) v.findViewById(R.id.zodiac_image);
				hol.duration = (Bold) v.findViewById(R.id.zodiac_duration);
				hol.main = (Regular) v.findViewById(R.id.zodiac_main);
				hol.love = (Regular) v.findViewById(R.id.zodiac_love);
				hol.balance = (Regular) v.findViewById(R.id.zodiac_balance);
				hol.style = (Regular) v.findViewById(R.id.zodiac_style);
				hol.FemaleBalance = (Regular) v
						.findViewById(R.id.zodiac_Fbalance);
				hol.sex = (Regular) v.findViewById(R.id.zodiac_sex);
				v.setTag(hol);
			} else
				hol = (Holder) v.getTag();
			hol.image.setImageBitmap(decodeSampledBitmapFromResource(
					mContext.getResources(), FemaleiconResId[mPosition], 150,
					150));
			hol.main.setText(zodiac.main);
			hol.love.setText(zodiac.mainFlove);
			shareText = zodiac.mainFlove;
			hol.duration.setText(getActivity().getString(R.string.zodiac_main)
					+ " " + zodiac.duration);
			hol.balance.setText(zodiac.balance);

			hol.style.setText(zodiac.style);

			hol.FemaleBalance.setText(zodiac.fBalance);

			hol.sex.setText(zodiac.sexF);

			return v;
		}

		class Holder {
			ImageView image;
			Bold duration;
			Regular main;
			Regular love;
			Regular balance;
			Regular style;
			Regular FemaleBalance;
			Regular sex;

		}
	}

	private class ZodiacMAdapter extends ArrayAdapter<Zodiac> {
		Context mContext;

		public ZodiacMAdapter(Context context, List<Zodiac> objects) {
			super(context, 0, 0, objects);
			// TODO Auto-generated constructor stub
			this.mContext = context;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			Zodiac zodiac = getItem(position);

			Holder hol = null;
			if (v == null) {
				v = ((Activity) mContext).getLayoutInflater().inflate(
						R.layout.m_list_item, parent, false);
				hol = new Holder();
				hol.image = (ImageView) v.findViewById(R.id.zodiac_mimage);
				hol.main = (Regular) v.findViewById(R.id.zodiac_mmain);
				hol.love = (Regular) v.findViewById(R.id.zodiac_mlove);
				hol.duration = (Bold) v.findViewById(R.id.zodiac_mduration);
				hol.balance = (Regular) v.findViewById(R.id.zodiac_mbalance);
				hol.mx = (Regular) v.findViewById(R.id.zodiac_mx);
				hol.gLove = (Regular) v.findViewById(R.id.zodiac_mGlove);
				hol.sex = (Regular) v.findViewById(R.id.zodiac_msex);
				v.setTag(hol);
			} else
				hol = (Holder) v.getTag();
			hol.image
					.setImageBitmap(decodeSampledBitmapFromResource(
							mContext.getResources(), MaleiconResId[mPosition],
							150, 150));
			hol.main.setText(zodiac.main);
			hol.love.setText(zodiac.mainMlove);
			shareText = zodiac.mainMlove;
			hol.duration.setText(getActivity().getString(R.string.zodiac_main)
					+ " " + zodiac.duration);
			hol.balance.setText(zodiac.balance);

			hol.gLove.setText(zodiac.mGetLove);

			hol.mx.setText(zodiac.mX);

			hol.sex.setText(zodiac.sexM);

			return v;
		}

		class Holder {
			ImageView image;
			Bold duration;
			Regular main;
			Regular love;
			Regular balance;
			Regular gLove;
			Regular mx;
			Regular sex;

		}
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return shareText;
	}
}