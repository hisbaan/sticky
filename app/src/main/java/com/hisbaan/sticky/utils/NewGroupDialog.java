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
import androidx.fragment.app.DialogFragment;

import com.hisbaan.sticky.R;

/**
 * Custom dialog that prompts the user for the name of the new group they want to create.
 */
public class NewGroupDialog extends DialogFragment {
    //Declaring variables.
    private EditText newGroupEditText;
    private NewGroupDialogListener listener;

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
        View view = inflater.inflate(R.layout.layout_new_group_dialog, null);

        //Declaring variables.
        newGroupEditText = view.findViewById(R.id.new_group_edit_text);

        //Creating the dialog with an okay and cancel button.
        builder.setView(view).setTitle("New Group").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Gets the text inside of the box and sends it to the parent activity.
                //TODO breaks when adding a new note. I didn't change anythinggggg
                String newGroupName = newGroupEditText.getText().toString();
                listener.applyText(newGroupName);
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
            listener = (NewGroupDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement NewGroupDialogListener");
        }
    } //End method onAttach.

    /**
     * The method that needs to be implemented which returns the text in the edit text.
     */
    public interface NewGroupDialogListener {
        void applyText(String newGroupName);
    } //End method NewGroupDialogListener.
} //End class NewGroupDialog.
