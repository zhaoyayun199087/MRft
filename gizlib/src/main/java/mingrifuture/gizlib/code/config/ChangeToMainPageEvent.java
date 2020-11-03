package mingrifuture.gizlib.code.config;

/**
 * Created by pengl on 2016/8/4.
 */
public class ChangeToMainPageEvent {

	int index = 2;

	public ChangeToMainPageEvent(int i){
		index = i;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
