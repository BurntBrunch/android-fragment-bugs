package tests;

import me.delyan.fragmentbugs.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RetainInstances extends FragmentActivity{

	public static class RetainFragment extends Fragment{
		
		boolean isNew = true;
		private String mName;
		private int mBackground;
		private boolean mChildren;
		private boolean mRetain;
		
		public static RetainFragment newInstance(String name, int color, boolean children, boolean retain) {
			RetainFragment frag = new RetainFragment();
			Bundle args = new Bundle();
			args.putString("name", name);
			args.putInt("background", color);
			args.putBoolean("children", children);
			args.putBoolean("retain", retain);
			
			frag.setArguments(args);
			frag.setRetainInstance(retain);
			
			return frag;
		}
		
		public RetainFragment() {
			super();
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mName = getArguments().getString("name");
			mBackground = getArguments().getInt("background");
			mChildren = getArguments().getBoolean("children");
			mRetain = getArguments().getBoolean("retain");
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
			setTitle(view, 
					String.format("Fragment - %s - %s - %s", 
							mName, 
							(isNew ? "brand new" : "retained"), 
							(mRetain ? "will retain" : "won't retain")));
			isNew = false;
			
			if(mChildren && getChildFragmentManager().findFragmentById(R.id.container) == null)
				getChildFragmentManager()
					.beginTransaction()
					.add(R.id.container, RetainFragment.newInstance(mName + "1", Color.DKGRAY, false, !mRetain))
					.commit();
		}
	}

	
	boolean isNew = true;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.title_with_container);
		
		if(getSupportFragmentManager().findFragmentById(R.id.container) == null)
			getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.container, RetainFragment.newInstance("A", Color.LTGRAY, true, true))
				.add(R.id.container, RetainFragment.newInstance("B", Color.LTGRAY, true, false))
				.commit();
		
		setTitle(getWindow().getDecorView(), "Activity - " + (isNew ? "brand new" : "retained"));
		isNew = false;
	}
	
	static void setTitle(View view, String title) {
		((TextView) view.findViewById(android.R.id.title)).setText(title);
	}
}
