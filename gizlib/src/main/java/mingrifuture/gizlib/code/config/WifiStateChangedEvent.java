package mingrifuture.gizlib.code.config;

public class WifiStateChangedEvent {
	private int state;
	
	public WifiStateChangedEvent(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
}
