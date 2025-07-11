package com.senpai.synckar.core;

import java.nio.file.*;

public class SyncManager {

    private final Path sourceDir;
    private final Path targetDir;
    private final boolean isDryRun;

    public SyncManager(String source, String target, boolean isDryRun) {
        this.sourceDir = Paths.get(source);
        this.targetDir = Paths.get(target);
        this.isDryRun = isDryRun;
    }

    public boolean Valid() {
        if (Files.notExists(sourceDir) || Files.notExists(targetDir)) {
            if (Files.notExists(sourceDir))
                System.out.println("Invalid source path");
            if (Files.notExists(targetDir))
                System.out.println("Invalid target path");
            return false;
        }
        if (!Files.isDirectory(sourceDir)||!Files.isDirectory(targetDir)){
            if (!Files.isDirectory(sourceDir))
                System.out.println("Source path is not a directory");
            if (!Files.isDirectory(targetDir))
                System.out.println("Target path is not a directory");
            return false;
        }
        return true;
    }

}
