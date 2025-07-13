package com.pranjal.synckar;

import com.pranjal.synckar.cli.cliHandler;
import com.pranjal.synckar.core.SyncManager;

public class Main {

    public static void main(String[] args) {

        if(args.length == 0){
            cliHandler.printUsage();
            return;
        }

        cliHandler cli = new cliHandler(args);

        if(cli.hasUnknowns()){
            cli.printUnknowns();
            return;
        }

        String source = cli.get("source");
        String target = cli.get("target");
        boolean dryRun = cli.hasflag("dryrun");

        if(source == null || target == null){
            System.out.println("Required arguments missing");
            return;
        }

        SyncManager manager = new SyncManager(source, target, dryRun);
        if(!manager.Sanity()) return;
        if(!manager.Valid()) return;
        manager.start();

    }
}
