package com.senpai.synckar;

import com.senpai.synckar.cli.cliHandler;
import com.senpai.synckar.core.SyncManager;

public class Main {

    public static void main(String[] args) {

        if(args.length == 0){
            System.out.println("No Arguments Provided");
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
