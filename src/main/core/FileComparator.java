package core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;

public class FileComparator {

    public static HashMap<Path, SyncManager.ActionType> compare(Path source, Path target){
        HashMap<Path, SyncManager.ActionType> resultMap = new HashMap<>();

        try{

            Files.walk(source).filter(Files::isRegularFile).forEach(
                    srcFile -> {
                        try{
                            Path relativePath = source.relativize(srcFile);
                            Path targetFile = target.resolve(relativePath);
                            SyncManager.ActionType action = SyncManager.ActionType.SKIP;
                            if(!Files.exists(targetFile)){
                                action = SyncManager.ActionType.COPY;
                                System.out.println("[NEW] " + srcFile);
                            }
                            else{

                                long srcSize = Files.size(srcFile);
                                long targetSize = Files.size(targetFile);

                                FileTime srcTime = Files.getLastModifiedTime(srcFile);
                                FileTime targetTime = Files.getLastModifiedTime(targetFile);

                                if(srcSize!=targetSize||!srcTime.equals(targetTime)) {
                                    action = SyncManager.ActionType.MODIFY;
                                    System.out.println("[MODIFIED] " + targetFile);
                                }
                            }
                            resultMap.put(relativePath, action);
                        } catch (Exception e) {
                            System.err.println("Error processing: " + srcFile + e.getMessage());
                        }
                    }
            );

            Files.walk(target).filter(Files::isRegularFile).forEach(
                    targetFile -> {
                        try {
                            Path relative = target.relativize(targetFile);
                            Path src = source.resolve(relative);
                            if(!Files.exists(src)) {
                                resultMap.put(relative, SyncManager.ActionType.DELETE);
                                System.out.println("[ADDITIONAL] " + targetFile);
                            }
                        } catch (Exception e){
                            System.err.println("Error processing: " + targetFile + e.getMessage());
                        }
                    }
            );

        } catch (IOException e){
            System.err.println("Error walking directories " + e.getMessage());
        }

        return resultMap;
    }
}
