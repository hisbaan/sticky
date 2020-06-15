package com.hisbaan.sticky.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Class that houses methods for different kinds of search and replace of note names from save files.
 */
public class Refactor {

    /**
     * Method that renames instances of a note in board save files.
     *
     * @param context    Context of the application.
     * @param folderName Name of the folder the note is in.
     * @param oldName    The old name of the note.
     * @param newName    The new name of the note.
     */
    public void renameNote(Context context, String folderName, String oldName, String newName) {
        //TODO don't allow for the creation of multiple notes with the same name (even in different folders (maybe keep a log of the notes created and replace/edit that too when things are renamed or deleted)

        //Getting a list of boards.
        File boardsDir = context.getFilesDir();
        String[] boards = boardsDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4).equals(".txt");
            }
        });

        assert boards != null;
        for (String board : boards) {
            //Reading in the board file.
            String file = null;
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(board);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }
                file = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Replacing all instances of the note in the save file.
            assert file != null;
            file = file.replaceAll(folderName + "," + oldName, folderName + "," + newName);

            //Writing the changed save file to the disk.
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(board, Context.MODE_PRIVATE);
                fos.write(file.getBytes());
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
        }
    } //End method renameNote.

    /**
     * Method that deletes reference of a note from all save files.
     *
     * @param context Context of the application.
     * @param folderName Name of the folder that the note is in.
     * @param noteName Name of the note.
     */
    public void deleteNote(Context context, String folderName, String noteName) {
        //Getting a list of boards.
        File boardsDir = context.getFilesDir();
        String[] boards = boardsDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4).equals(".txt");
            }
        });

        assert boards != null;
        for (String board : boards) {
            //Reading in the board file.
            String file = null;
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(board);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }
                file = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            assert file != null;
            Scanner sc = new Scanner(file);
            String output = "";

            while(sc.hasNextLine()) {
                String currentLine = sc.nextLine();
                if (!currentLine.contains(folderName + "," + noteName + ",")) {
                    output += currentLine;
                }
            }

            //Writing the changed save file to the disk.
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(board, Context.MODE_PRIVATE);
                fos.write(output.getBytes());
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
        }
    } //End method deleteNote.


    /**
     * Method that renames any instances of a folder in the save files.
     *
     * @param context Context of the application.
     * @param oldName Old name of the folder.
     * @param newName New name of the folder.
     */
    public void renameFolder(Context context, String oldName, String newName) {
        File boardsDir = context.getFilesDir();
        String[] boards = boardsDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4).equals(".txt");
            }
        });

        assert boards != null;
        for (String board : boards) {
            //Reading in the save file.
            String file = null;
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(board);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }
                file = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Replacing all instances of the board name in the save files.
            assert file != null;
            file = file.replaceAll(oldName + ",", newName + ",");

            //Writing the edited save file to the disk.
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(board, Context.MODE_PRIVATE);
                fos.write(file.getBytes());
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
        }
    } //End method renameFolder.

    /**
     * Method that deletes reference of a folder from all save files.
     *
     * @param context Context of the application.
     * @param folderName Name of the folder.
     */
    public void deleteFolder(Context context, String folderName) {
        //Getting a list of boards.
        File boardsDir = context.getFilesDir();
        String[] boards = boardsDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.substring(name.length() - 4).equals(".txt");
            }
        });

        assert boards != null;
        for (String board : boards) {
            //Reading in the board file.
            String file = null;
            FileInputStream fis = null;
            try {
                fis = context.openFileInput(board);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;
                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }
                file = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            assert file != null;
            Scanner sc = new Scanner(file);
            String output = "";

            while(sc.hasNextLine()) {
                String currentLine = sc.nextLine();
                if (!currentLine.contains(folderName + ",")) {
                    output += currentLine;
                }
            }

            //Writing the changed save file to the disk.
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(board, Context.MODE_PRIVATE);
                fos.write(output.getBytes());
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
        }
    } //End method deleteNote.
} //End class Refactor.
