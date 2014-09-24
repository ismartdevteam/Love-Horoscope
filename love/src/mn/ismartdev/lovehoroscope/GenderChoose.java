package mn.ismartdev.lovehoroscope;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class GenderChoose extends Fragment implements OnClickListener {

	private View v;
	private ImageView male;
	private ImageView female;
	Animation clickAnim;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		clickAnim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.but_click);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.main_layout, container, false);
		male = (ImageView) v.findViewById(R.id.menu_male);
		female = (ImageView) v.findViewById(R.id.menu_female);
		male.setOnClickListener(this);
		female.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		v.startAnimation(clickAnim);
		clickAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				switch (v.getId()) {
				case R.id.menu_female:
					b.putInt("gender", 2);
					break;
				case R.id.menu_male:
					b.putInt("gender", 1);
					break;
				}
				Intent zodiac = new Intent(getActivity(), ZodiacAc.class);
				zodiac.putExtras(b);
				startActivity(zodiac);

			}
		});
	}

}
