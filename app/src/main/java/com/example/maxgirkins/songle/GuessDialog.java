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
    public static GuessDialog newInstance(String title, String posBtn, String negBtn) {
        GuessDialog frag = new GuessDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("posBtn", posBtn);
        args.putString("negBtn", negBtn);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String posBtn = getArguments().getString("posBtn");
        String negBtn = getArguments().getString("negBtn");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(posBtn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Guess)getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton(negBtn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Guess)getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();
    }
}
