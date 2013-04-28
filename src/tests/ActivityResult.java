package tests;

import me.delyan.fragmentbugs.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ActivityResult extends FragmentActivity {

	static Handler handler = new Handler();

	public static class ResultFragment extends Fragment {

		private String mName;
		private int mBackground = Color.LTGRAY;

		public ResultFragment(String name) {
			super();
			mName = name;
		}
		
		public ResultFragment(String name, int color) {
			super();
			mName = name;
			mBackground = color;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.title_with_container, container,
					false);
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			view.setBackgroundColor(mBackground);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							ActivityResult.class);

					// seems appropriate
					intent.setAction(Intent.ACTION_BUG_REPORT);

					startActivityForResult(intent, 0);
				}
			});
			setTitle(view, "Fragment " + mName);
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			String result = resultCode == RESULT_OK ? "result OK" : "result "
					+ resultCode;

			setTitle(getView(), "Fragment " + mName + " - " + result);

			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					setTitle(getView(), "Fragment " + mName);
				}
			}, 700);
		}
	}
	
	public static class ResultFragmentA extends ResultFragment{
		public ResultFragmentA() {
			super("A");
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			ResultFragment fragmentA1 = new ResultFragment("A1", Color.DKGRAY);
			ResultFragment fragmentA2 = new ResultFragment("A2", Color.DKGRAY);
			
			getChildFragmentManager().beginTransaction()
				.add(R.id.container, fragmentA1)
				.add(R.id.container, fragmentA2)
				.commit();
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.title_with_container);

		if (Intent.ACTION_BUG_REPORT.equalsIgnoreCase(getIntent().getAction())) {
			// We were called to return a result, so just that and finish
			setResult(RESULT_OK);
			finish();
		} else {
			ResultFragment fragmentA = new ResultFragmentA(),  
					fragmentB = new ResultFragment("B");

			getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.container, fragmentA)
				.add(R.id.container, fragmentB)
				.commit();

			setTitle(getWindow().getDecorView(), "Activity");
			
			findViewById(android.R.id.title).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ActivityResult.this, ActivityResult.class);

					// seems appropriate
					intent.setAction(Intent.ACTION_BUG_REPORT);

					startActivityForResult(intent, 0);
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		setTitle(getWindow().getDecorView(), "Activity");

		String result = resultCode == RESULT_OK ? "result OK" : "result "
				+ resultCode;

		setTitle(getWindow().getDecorView(), "Activity - " + result);

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setTitle(getWindow().getDecorView(), "Activity");
			}
		}, 700);
	}

	static void setTitle(View view, String title) {
		((TextView) view.findViewById(android.R.id.title)).setText(title);
	}
}
