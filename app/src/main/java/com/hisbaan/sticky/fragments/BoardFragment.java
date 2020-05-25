package com.hisbaan.sticky.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hisbaan.sticky.R;
import com.hisbaan.sticky.activities.BoardActivity;
import com.hisbaan.sticky.adapters.BoardAdapter;
import com.hisbaan.sticky.models.BoardItem;
import com.hisbaan.sticky.utils.NewBoardDialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Displays the boards in a grid that the user can click on and interact with.
 */
public class BoardFragment extends Fragment {

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
    } //End Method onCreateView.

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
        final ArrayList<BoardItem> boardItems = new ArrayList<>();

        //Finding the text files in /data/data/com.hisbaan.sticky/files/ which store data about the boards.
        final File directoryToSearch = requireActivity().getFilesDir();
        final File[] boardNames = directoryToSearch.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4).equals(".txt");
            }
        });

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
        final BoardAdapter boardAdapter = new BoardAdapter(boardItems);

        boardRecyclerView.setLayoutManager(boardGridLayoutManager);
        boardRecyclerView.setAdapter(boardAdapter);
        boardRecyclerView.setHasFixedSize(true);

        //Setting up the click listener that opens a board when clicked.
        boardAdapter.setOnItemClickListener(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //If the add tile is clicked, open a dialog to make a new board. If not, open the board.
                if (position == boardItems.size() - 1) {
                    NewBoardDialog newBoardDialog = new NewBoardDialog();
                    newBoardDialog.show(requireActivity().getSupportFragmentManager(), "new board dialog");
                } else {
                    Intent intent = new Intent(requireContext().getApplicationContext(), BoardActivity.class);
                    intent.putExtra("board_name", boardItems.get(position).getBoardName());
                    startActivity(intent);
                }
            }
        });
    } //End Method onViewCreated.
} //End Class BoardFragment.