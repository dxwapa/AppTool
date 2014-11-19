package net.lgpage.androidant.view;

public class LinearLayout extends ViewGroup{


	String orientation;

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public void setOrientation(int o) {
		if(o == 1){
			orientation = "horizontal";
		}else{
			orientation = "vertical";
		}
	}
	public LinearLayout() {
		super();
		setLayout(-1, -2);
		setOrientation(0);
	}

	@Override
	public void buildMap() {
		super.buildMap();
		getAttmap().put("android:orientation", orientation);
	}

	
	
	
}
