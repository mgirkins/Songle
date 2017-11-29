package com.example.maxgirkins.songle;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by MaxGirkins on 29/11/2017.
 */

public class GuessDialog extends DialogFragment {
    public static GuessDialog newInstance(String title, String youtubeLink) {
        GuessDialog frag = new GuessDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("youtubeLink", youtubeLink);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                .setMessage("You're right, " + title+" is the correct song. Congratulations!")
                .create();
    }
}
