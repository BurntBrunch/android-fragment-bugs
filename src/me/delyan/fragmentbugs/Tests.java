package me.delyan.fragmentbugs;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import tests.ActivityResult;
import tests.ActivityResultNative;
import tests.NestedAnimations;
import tests.NestedAnimationsNative;
import tests.RetainInstances;
import tests.RetainInstancesNative;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

public class Tests extends Activity{
	
	final static Map<String, Class<?>> activities;
	static {
		Map<String, Class<?>> map = new LinkedHashMap<String, Class<?>>();
		
		map.put("Animations", NestedAnimations.class);
		map.put("Animations (native)", NestedAnimationsNative.class);
		map.put("Retain instances", RetainInstances.class);
		map.put("Retain instances (native)", RetainInstancesNative.class);
		map.put("Activity results", ActivityResult.class);
		map.put("Activity results (native)", ActivityResultNative.class);
		
		activities = Collections.unmodifiableMap(map);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		final String[] names = activities.keySet().toArray(new String[0]);
		
		new AlertDialog.Builder(this)
			.setTitle("Nested fragments bugs")
			.setSingleChoiceItems(names, -1, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
					Class<?> cls = activities.get(names[which]);
					
					Intent intent = new Intent(Tests.this, cls);
					startActivity(intent);
					finish();
				}
			})
			.show();
	}
}
