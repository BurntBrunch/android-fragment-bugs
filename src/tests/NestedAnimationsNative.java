package tests;

import me.delyan.fragmentbugs.R;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
public class NestedAnimationsNative extends Activity {
	
	public static class AnimatedFragment extends Fragment {
		private String mName;
		private int mBackground;
		private boolean mChildren;

		public AnimatedFragment(String name, int color, boolean children){
			super();
			mName = name;
			mBackground = color;
			mChildren = children;
		}
		
		public boolean hasChildren() {
			return mChildren;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if(mChildren)
				getChildFragmentManager()
					.beginTransaction()
					.add(R.id.container, new AnimatedFragment(mName + "1", Color.parseColor("#336699"), false))
					.commit();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.title_with_container, container, false);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			view.setBackgroundColor(mBackground);
			setTitle(view, "Fragment " + mName);
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.title_with_container);
		
		setTitle(getWindow().getDecorView(), "Activity");
		
		findViewById(R.id.container).setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				replaceFragments();
			}
		});
		
		getFragmentManager()
			.beginTransaction()
			.add(R.id.container, new AnimatedFragment("A", Color.LTGRAY, true))
			.commit();
	}
	
	private void replaceFragments() {
		boolean nextFragmentChildren = 
				!((AnimatedFragment) getFragmentManager().findFragmentById(R.id.container)).hasChildren();
		
		Fragment frag = new AnimatedFragment(nextFragmentChildren ? "A" : "B", Color.LTGRAY, nextFragmentChildren);
		
		getFragmentManager()
			.beginTransaction()
			.setCustomAnimations(R.animator.in_from_right, 
					R.animator.out_to_left,
					R.animator.in_from_left,
					R.animator.out_to_right)
			.replace(R.id.container, frag)
			.addToBackStack(null)
			.commit();
	}

	static void setTitle(View view, String title) {
		((TextView) view.findViewById(android.R.id.title)).setText(title);
	}
}
