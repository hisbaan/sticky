package com.hisbaan.sticky.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.hisbaan.sticky.R;

import java.util.Objects;

public class NewBoardDialog extends AppCompatDialogFragment {
    //Declaring variables.
    private EditText newBoardEditText;
    private NewBoardDialog.NewBoardDialogListener listener;

    /**
     * Similar to an onCreate method. Runs the code for building the dialog and displaying it.
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_board_dialog, null);

        //Declaring variables.
        newBoardEditText = view.findViewById(R.id.new_board_edit_text);

        //Creating the dialog with an okay and cancel button.
        builder.setView(view).setTitle("New Board").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Gets the text inside of the box and sends it to the parent activity.
                String newBoardName = newBoardEditText.getText().toString();
                listener.applyText(newBoardName);
            }
        });
        return builder.create();
    } //End Method onCreateDialog.

    /**
     * Adding the listener when the dialog opens.
     *
     * @param context Context of the activity.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //If the class is not implemented correctly, throw an error.
        try {
            listener = (NewBoardDialog.NewBoardDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement NewBoardDialogListener");
        }
    } //End Method onAttach.

    /**
     * The method that needs to be implemented which returns the text in the edit text.
     */
    public interface NewBoardDialogListener {
        void applyText(String newBoardName);
    } //End Method NewGroupDialogListener.
}
