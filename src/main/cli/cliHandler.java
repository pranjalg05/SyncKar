package com.pranjal.synckar.cli;

import com.pranjal.synckar.util.ConfigParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class cliHandler {

    private final HashMap<String, String> map = new HashMap<>();
    private final HashSet<String> flags = new HashSet<>();
    private final HashSet<String> unknowns = new HashSet<>();

    private static final Set<String> VALID_ARGS = Set.of("--source", "--target", "--dryrun", "--help", "--config");

    public cliHandler(String[] args) {
        parse(args);
    }

    private void parse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            var arg = args[i];

            if(!arg.startsWith("--")) continue;

            if(!VALID_ARGS.contains(arg)){
                unknowns.add(arg);
                continue;
            }

            switch (arg) {
                case "--help": {
                    printUsage();
                    return;
                }
                case "--config":{
                    if(i+1<args.length){
                        var x = ConfigParser.parse(args[i+1]);
                        if(x.get("dryrun").equals("true"))
                            flags.add("dryrun");
                        map.put("source", x.getOrDefault("source", ""));
                        map.put("target", x.getOrDefault("target", ""));
                    }
                    else{
                        System.out.println("Missing config file path after --config");
                    }
                    break;
                }
                case "--source":
                case "--target":{
                    if(i+1<args.length && !args[i+1].startsWith("--")) {
                        map.put(arg.substring(2), args[i+1]);
                        i++;
                    } else {
                        System.out.println("Missing value for " + arg + "");
                    }
                    break;
                }
                case "--dryrun":
                    flags.add(arg.substring(2));
                    break;
                default:
                    break;
            }

        }
    }

    public String get(String key) {
        return map.get(key);
    }

    public boolean hasflag(String flag) {
        return flags.contains(flag);
    }

    public boolean hasUnknowns() {
        return !unknowns.isEmpty();
    }

    public void printUnknowns(){
        if (hasUnknowns()) {
            System.out.println("Unknown arguments: " + unknowns);
        }
    }

    public static void printUsage(){
        System.out.println("Usage: ");
        System.out.println("  --source <sourcePath>   (required)");
        System.out.println("  --target <targetPath>   (required)");
        System.out.println("  --dryrun                (optional flag)");
        System.out.println("-------------------OR--------------------------");
        System.out.println("  --config <config.json path>   (required)");
    }

}
