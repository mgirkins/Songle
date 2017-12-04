package com.example.maxgirkins.songle;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by MaxGirkins on 29/11/2017.
 */

//dialog triggered when user clicks reset progress in settings
//triggers a warning that all data will be removed.
public class ResetProgressDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Are You sure")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((SettingsActivity)getActivity()).doPositiveClickResetBtn();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((SettingsActivity)getActivity()).doNegativeClickResetBtn();
                            }
                        }
                )
                .setMessage("This will delete all progress you have made!")
                .create();
    }
}
