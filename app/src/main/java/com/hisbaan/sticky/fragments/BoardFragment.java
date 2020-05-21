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

public class BoardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().setTitle("Boards");

        final ArrayList<BoardItem> boardItems = new ArrayList<>();

        final File directoryToSearch = requireActivity().getFilesDir();

        final File[] boardNames = directoryToSearch.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4).equals(".txt");
            }
        });

        assert boardNames != null;
        for (File boardName : boardNames) {
            String[] tempName = boardName.toString().split("/");
            String nameWithTXT = tempName[tempName.length - 1];
            boardItems.add(new BoardItem(nameWithTXT.substring(0, nameWithTXT.length() - 4)));
        }
        boardItems.add(new BoardItem("Add"));

        RecyclerView boardRecyclerView = requireView().findViewById(R.id.board_recycler_view);
        RecyclerView.LayoutManager boardGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        final BoardAdapter boardAdapter = new BoardAdapter(boardItems);

        boardRecyclerView.setLayoutManager(boardGridLayoutManager);
        boardRecyclerView.setAdapter(boardAdapter);
        boardRecyclerView.setHasFixedSize(true);

        boardAdapter.setOnItemClickListener(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == boardItems.size() - 1) {
                    //TODO add a board here.
                    //make a dialog asking for the name of the new board (reuse the old dialog?) then make a file with it and then open up the board
                    NewBoardDialog newBoardDialog = new NewBoardDialog();
                    newBoardDialog.show(requireActivity().getSupportFragmentManager(), "new board dialog");

                } else {
                    Intent intent = new Intent(requireContext().getApplicationContext(), BoardActivity.class);
                    intent.putExtra("board_name", boardItems.get(position).getBoardName());
                    startActivity(intent);
                }
            }
        });
    }
}