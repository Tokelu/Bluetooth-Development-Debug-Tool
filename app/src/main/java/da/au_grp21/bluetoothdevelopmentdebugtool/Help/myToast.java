package da.au_grp21.bluetoothdevelopmentdebugtool.Help;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import da.au_grp21.bluetoothdevelopmentdebugtool.R;

public class myToast {

    //This function makes oure Toaste
    public static void showToast(Context context, int stringId) {
        Toast t = Toast.makeText(context, context.getString(stringId), Toast.LENGTH_SHORT);
        View toastView = t.getView();
        toastView.setBackground(context.getResources().getDrawable(R.drawable.toast));

        TextView text = toastView.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(R.color.textOrange));
        text.setBackgroundColor(context.getResources().getColor(R.color.toast));

        t.setView(toastView);
        t.show();
    }
}
