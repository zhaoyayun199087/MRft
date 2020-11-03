package mingrifuture.gizlib.code.config;

/**
 * Created by Administrator on 2017/4/12.
 */
public class ChangeLanguageEvent {

    //为true是chain
    private boolean isChina;
    public ChangeLanguageEvent(boolean isChina){
      this.isChina=isChina;
    }


    public boolean getLanguage(){
        return isChina;
    }
}
