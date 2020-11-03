package mingrifuture.gizlib.code.config;

/**
 * Created by pengl on 2016/1/18.
 */
public class UpdateTimeEvent1 {
	private int hour;
	private int minute;

	public UpdateTimeEvent1(int hour, int minute) {
		this.hour = hour;
		this.minute = minute;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

}
