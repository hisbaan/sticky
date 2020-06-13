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

/**
 * Dialog that gets the new name of an item to be renamed from the user.
 */
public class RenameDialog extends AppCompatDialogFragment {
    //Declaring variables.
    private EditText renameEditText;
    private RenameDialog.RenameDialogListener listener;

    /**
     * Similar to an onCreate method. Runs the code for building the dialog and displaying it.
     *
     * @param savedInstanceState Saved instance state that can be used to gather information from previous runs.
     * @return An inflated dialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rename_dialog, null);

        //Declaring variables.
        renameEditText = view.findViewById(R.id.rename_edit_text);

        //Creating the dialog with an okay and cancel button.
        builder.setView(view).setTitle("Rename").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Gets the text inside of the box and sends it to the parent activity.
                String newName = renameEditText.getText().toString();
                listener.applyText(newName);
            }
        });
        return builder.create();
    } //End method onCreateDialog.

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
            listener = (RenameDialog.RenameDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement NewBoardDialogListener");
        }
    } //End method onAttach.

    /**
     * The method that needs to be implemented which returns the text in the edit text.
     */
    public interface RenameDialogListener {
        void applyText(String newName);
    } //End method NewGroupDialogListener.
} //End class RenameDialog.
