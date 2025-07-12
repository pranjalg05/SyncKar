package com.pranjal.synckar.core;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.HashMap;

public class SyncManager {

    private final Path sourceDir;
    private final Path targetDir;
    private final boolean dryRun;
    private HashMap<Path, ActionType> actionMap;

    enum ActionType {COPY, DELETE, SKIP, MODIFY}

    public SyncManager(String source, String target, boolean isDryRun) {
        this.sourceDir = Paths.get(source);
        this.targetDir = Paths.get(target);
        this.dryRun = isDryRun;
    }

    public boolean Sanity(){
        var absSource = sourceDir.toAbsolutePath().normalize();
        var absTarget = targetDir.toAbsolutePath().normalize();

        var sane = true;

        if(absSource.startsWith(absTarget)){
            System.err.println("Source is inside Target - this is not allowed");
            sane = false;
        }
        if(absTarget.startsWith(absSource)){
            System.err.println("Target is inside Source - this is not allowed");
            sane = false;
        }
        return sane;
    }

    public boolean Valid() {

        boolean isValid = true;

        if (!Files.exists(sourceDir)) {
            System.out.println("Source path is not a directory");
            isValid = false;
        } else if (!Files.isDirectory(sourceDir)) {
            System.out.println("Source path is not a directory");
            isValid = false;
        }

        if (!Files.exists(targetDir)) {
            System.out.println("Target path is not a directory");
            isValid = false;
        } else if (!Files.isDirectory(targetDir)) {
            System.out.println("Target path is not a directory");
            isValid = false;
        }

        return isValid;
    }

    public void start() {
        System.out.println((dryRun ? "[Dry Run] " : "") + "Syncing files from " + sourceDir + " to " + targetDir);
        this.actionMap = FileComparator.compare(sourceDir, targetDir);
        SyncIt();
        clear();
    }

    private void SyncIt(){
        for(var Entry: actionMap.entrySet()){
            var relPath = Entry.getKey();
            var task = Entry.getValue();

            var srcPath = sourceDir.resolve(relPath);
            var tarPath = targetDir.resolve(relPath);

            try {
                switch (task) {
                    case COPY:
                    case MODIFY: {
                        if (dryRun) {
                            System.out.println("Would copy " + srcPath + " ➜ " + targetDir);
                        } else {
                            Files.createDirectories(tarPath.getParent());
                            System.out.println("Copying " + srcPath + " ➜ " + tarPath);
                            Files.copy(srcPath, tarPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                        break;
                    }
                    case DELETE: {
                        if (dryRun) {
                            System.out.println("Would delete " + tarPath);
                        } else {
                            System.out.println("Deleting " + tarPath);
                            Files.deleteIfExists(tarPath);
                        }
                    }
                    default:
                }
            } catch (Exception e) {
                System.out.println("Error Modifying " + tarPath);
            }
        }
    }

    private void clear(){
        try {
            Files.walk(targetDir)
                    .sorted(Comparator.reverseOrder())
                    .filter(Files::isDirectory)
                    .forEach(
                            path -> {
                                try {
                                    if(Files.list(path).findAny().isEmpty()) {
                                        System.out.println("Deleting [EMPTY FOLDER] " + path);
                                        Files.delete(path);
                                    }
                                } catch (IOException e){
                                    System.out.println("Couldn't delete folder: " + path + " - " + e.getMessage());
                                }
                            }
                    );
        } catch (IOException e) {
            System.out.println("Error clearing empty folders " + e.getMessage());
        }
    }

}
