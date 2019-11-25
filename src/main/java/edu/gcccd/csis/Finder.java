package edu.gcccd.csis;

import java.io.*;
import java.nio.file.*;
import java.util.LinkedList;

public class Finder {
    /**
     * If no start location is given, then we start the search in the current dir
     *
     * @param args {@link String}[] start location for the largest file search.
     */
    public static void main(final String[] args) {
        final Path path = Paths.get(args.length < 1 ? "." : args[0]);
        final File ex = findExtremeFile(path);
        System.out.printf("Starting at : %s, the largest file was found here:\n%s\n its size is: %d\n",
                path.toAbsolutePath().toString(),
                ex.getAbsolutePath(),
                ex.length());
    }
    /**
     * Identifies the more extreme of two given files.
     * Modifying this method allows to search for other extremes, like smallest, oldest, etc.
     *
     * @param f1 {@link File} 1st file
     * @param f2 {@link File} 2nd file
     * @return {@link File} the more extreme of the two given files.
     */
    static File extreme(final File f1, final File f2) {
        if(f1 == null) {
            return f2;
        }
        if(f2 == null) {
            return f1;
        }
        if(f1.length() > f2.length()) {
            return f1;
        }
        return f2;
    }
    /**
     * DFS for the most extreme file, starting the search at a given directory path.
     *
     * @param p {@link Path} path to a directory
     * @return {@link File} most extreme file in the given path
     */
    static File findExtremeFile(final Path p) {
        File x = null;
        File current = null;
        final File[] fa = p.toFile().listFiles();
        if (fa != null) { // if null then directory is probably not accessible
            //
            // Since this is DFS, first find all sub-directories in the current directory
            //
            for (int i = 0; i < fa.length; i++) {
                if (fa[i].isDirectory()) {
                    current = findExtremeFile(Paths.get(fa[i].getPath()));
                    if (x == null) x = current;
                    else if (current != null && x.length() < current.length()) x = current;
                    else if (current != null && x.length() == current.length()) {
                        if (x.getPath().length() <= current.getPath().length()) x = current;
                    }
                }
            }
            //
            // Now let's look at al the files in the current dir
            //
            for (int i = 0; i < fa.length; i++) {
                if (fa[i].isFile()) {
                    if (x == null) x = fa[i];
                    else if (x.length() < fa[i].length()) x = fa[i];
                    else if (x.length() == fa[i].length()) {
                        if (x.getPath().length() <= fa[i].getPath().length()) x = fa[i];
                    }
                }
            }
        }
        return x;
    }
    static File findExtremeFileBFS(final Path p) {
        File x = null;
        File current = null;
        File[] fa = p.toFile().listFiles();

        LinkedList<Path> queue = new LinkedList();
        queue.add(p);

        if (fa != null) {
            // BFS approach
            do {
                if (fa != null) {

                    if (queue.peek() != null) {
                        fa = queue.pollFirst().toFile().listFiles();
                        for (int i = 0; i < fa.length; i++) {
                            if (fa[i].isDirectory()) {
                                queue.add(Paths.get(fa[i].getPath()));
                            }
                            if (fa[i].isFile()) {
                                if (x == null) x = fa[i];
                                else if (x.length() < fa[i].length()) x = fa[i];
                                else if (x.length() == fa[i].length()) {
                                    if (x.getPath().length() <= fa[i].getPath().length()) x = fa[i];
                                }
                            }
                        }
                    }
                }
            } while (queue.size() > 0);
        }
        return x;
    }
}