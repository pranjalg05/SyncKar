package com.senpai.synckar.core;

import java.nio.file.*;
import java.util.HashMap;

public class SyncManager {

    private final Path sourceDir;
    private final Path targetDir;
    private final boolean dryRun;

    enum ActionType {COPY, DELETE, SKIP, MODIFY}

    public SyncManager(String source, String target, boolean isDryRun) {
        this.sourceDir = Paths.get(source);
        this.targetDir = Paths.get(target);
        this.dryRun = isDryRun;
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
        System.out.println(dryRun ? "[Dry Run]" : "" + " Syncing files from " + sourceDir + " to " + targetDir);
        HashMap<Path, ActionType> actionMap = FileComparator.compare(sourceDir, targetDir);
    }

}
