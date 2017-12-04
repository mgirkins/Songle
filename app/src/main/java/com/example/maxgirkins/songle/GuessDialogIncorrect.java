package com.example.maxgirkins.songle;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by MaxGirkins on 29/11/2017.
 */
//dialog triggered when user guesses a song incorrectly
public class GuessDialogIncorrect extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Sorry!")
                .setPositiveButton("Try Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Guess)getActivity()).doPositiveClickIncorrect();
                            }
                        }
                )
                .setNegativeButton("Look for more Lyrics",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Guess)getActivity()).doNegativeClickIncorrect();
                            }
                        }
                )
                .setMessage("Sorry, that was not the Song you were looking for.")
                .create();
    }
}
