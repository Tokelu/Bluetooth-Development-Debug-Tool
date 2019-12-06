package da.au_grp21.bluetoothdevelopmentdebugtool.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import da.au_grp21.bluetoothdevelopmentdebugtool.R;

public class PopUpNotSaved extends Activity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));
    }
}
