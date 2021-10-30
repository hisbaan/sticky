package com.hisbaan.sticky.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.activities.BoardActivity;
import com.hisbaan.sticky.activities.MainActivity;
import com.hisbaan.sticky.adapters.BoardAdapter;
import com.hisbaan.sticky.models.BoardItem;
import com.hisbaan.sticky.utils.NewBoardDialog;
import com.hisbaan.sticky.utils.RenameDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Displays the boards in a grid that the user can click on and interact with.
 */
public class BoardFragment extends Fragment {
    public static ArrayList<BoardItem> boardItems;
    public static BoardAdapter boardAdapter;
    public static int renameIndex;

    /**
     * Inflates the fragment when it is created.
     *
     * @param inflater           Layout inflater.
     * @param container          The activity that the fragment is inside.
     * @param savedInstanceState Saved instance state that can contain information from previous runs.
     * @return Returns an inflated fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board, container, false);
    } //End method onCreateView.

    /**
     * Similar to an onCreateMethod that runs code when the fragment is created.
     *
     * @param view               The view that is being created.
     * @param savedInstanceState Saved instance state that can contain information from previous runs.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting the title of the toolbar.
        requireActivity().setTitle("Boards");

        //Creating an array list of items for the recycler view.
        boardItems = new ArrayList<>();

        //Finding the text files in /data/data/com.hisbaan.sticky/files/ which store data about the boards.
        final File directoryToSearch = requireActivity().getFilesDir();
        final File[] boardNames = directoryToSearch.listFiles((dir, name) -> name.endsWith(".txt"));

        //Formatting the directory names to remove the .txt from the name.
        assert boardNames != null;
        for (File boardName : boardNames) {
            String[] tempName = boardName.toString().split("/");
            String nameWithTxt = tempName[tempName.length - 1];
            boardItems.add(new BoardItem(nameWithTxt.substring(0, nameWithTxt.length() - 4)));
        }
        boardItems.add(new BoardItem("Add"));

        //Setting up the recycler view.
        RecyclerView boardRecyclerView = requireView().findViewById(R.id.board_recycler_view);
        RecyclerView.LayoutManager boardGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        boardAdapter = new BoardAdapter(boardItems);

        boardRecyclerView.setLayoutManager(boardGridLayoutManager);
        boardRecyclerView.setAdapter(boardAdapter);
        boardRecyclerView.setHasFixedSize(true);

        //Setting up the click listener that opens a board when clicked.
        boardAdapter.setOnItemClickListener(position -> {
            //If the add tile is clicked, open a dialog to make a new board. If not, open the board.
            if (position == boardItems.size() - 1) {
                MainActivity.applyTextState = MainActivity.BOARD_ACTIVITY;
                NewBoardDialog newBoardDialog = new NewBoardDialog();
                newBoardDialog.show(requireActivity().getSupportFragmentManager(), "new board dialog");
            } else {
                Intent intent = new Intent(requireContext().getApplicationContext(), BoardActivity.class);
                intent.putExtra("board_name", boardItems.get(position).getBoardName());
                startActivity(intent);
            }
        });
    } //End method onViewCreated.

    /**
     * Method that runs when a context menu item is selected.
     *
     * @param item Item on which the context menu was triggered on.
     * @return Whether or not an action was taken.
     */
    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case 121: //Ask for confirmation then delete the note.
                if (item.getGroupId() == boardItems.size() - 1) {
                    return false;
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                    alert.setTitle("Confirm Delete");
                    alert.setMessage("Are you sure you want to delete?\nThis action cannot be undone");
                    alert.setPositiveButton(android.R.string.yes, (dialog, which) -> removeItem(item.getGroupId()));
                    alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());

                    alert.show();
                }
                return true;
            case 122: //Ask for the new board name then rename it.
                MainActivity.applyTextState = MainActivity.BOARD_ACTIVITY_RENAME;
                renameIndex = item.getGroupId();
                RenameDialog renameDialog = new RenameDialog();
                renameDialog.show(requireActivity().getSupportFragmentManager(), "rename dialog");
                return true;
            case 123: //Clear the board after asking for confirmation.
                AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                alert.setTitle("Confirm Delete");
                alert.setMessage("Are you sure you want to delete?\nThis action cannot be undone");
                alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    FileOutputStream fos = null;
                    try {
                        fos = requireContext().openFileOutput(boardItems.get(item.getGroupId()).getBoardName() + ".txt", MODE_PRIVATE);
                        fos.write("".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel()).show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    } //End method onContextItemSelected.

    /**
     * Method that removes an item from the array and the recycler view with an animation.
     *
     * @param position The position of the item that is to be removed.
     */
    private void removeItem(int position) {
        if (new File(requireContext().getFilesDir() + "/" + boardItems.get(position).getBoardName() + ".txt").delete()) {
            System.out.println("File deleted successfully.");
        } else {
            System.out.println("File deletion failed.");
        }

        boardItems.remove(position);
        boardAdapter.notifyItemRemoved(position);
    } //End method removeItem.
} //End class BoardFragment.