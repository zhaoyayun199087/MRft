package mingrifuture.gizlib.code.config;

/**
 * 更新数据事件
 * @author pengl
 *
 */
public class SendCmdEvent {

	private String cmd;
	public SendCmdEvent(String cmd){
		this.cmd = cmd;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	
}
