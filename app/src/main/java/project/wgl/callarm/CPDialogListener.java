package project.wgl.callarm;

import android.content.DialogInterface;

/**
 * Created by WGL on 2018. 2. 8..
 */

public interface CPDialogListener extends DialogInterface.OnClickListener {
    @Override
    void onClick(DialogInterface dialog, int which);
}
