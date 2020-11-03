package mingrifuture.gizlib.code.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * ����px��  dp��sp��ת������
 * Created by Administrator on 2015/12/16.
 */
public class PxUtils {

    public static int dpToPx(int dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    public static int PxToDp(int dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp / scale + 0.5f);
    }

    public static int spToPx(int sp,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }


}