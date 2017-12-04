package com.example.maxgirkins.songle;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by MaxGirkins on 29/11/2017.
 */

//dialog triggered when user guesses a song correctly.
public class GuessDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //get song title and artist
        String title = getArguments().getString("title");
        return new AlertDialog.Builder(getActivity())
                .setTitle("Correct!")
                .setPositiveButton("New Game",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Guess)getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton("Listen on youtube",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Guess)getActivity()).doNegativeClick();
                            }
                        }
                )
                .setMessage("You're right, " + title +" is the correct song. Congratulations!")
                .create();
    }
}
