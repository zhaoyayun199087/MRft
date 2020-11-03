package mingrifuture.gizlib.code.config;

public class BatteryStateChangedEvent {
	private boolean colorChanged;

	private int color;

	private int level;

	private int scale;

	public BatteryStateChangedEvent(){}

	public BatteryStateChangedEvent(boolean colorChanged,int color) {
		this.colorChanged = colorChanged;
		this.color = color;
	}
	
	public BatteryStateChangedEvent(boolean colorChanged,int level,int scale){
		this.colorChanged = colorChanged;
		this.level = level;
		this.scale = scale;
	}

	public boolean isColorChanged() {
		return colorChanged;
	}

	public int getColor() {
		return color;
	}

	public int getLevel() {
		return level;
	}

	public int getScale() {
		return scale;
	}

	public void setColorChanged(boolean colorChanged) {
		this.colorChanged = colorChanged;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
}
