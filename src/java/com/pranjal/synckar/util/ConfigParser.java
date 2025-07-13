package com.pranjal.synckar.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigParser {

    public static HashMap<String, String> parse(String path){
        HashMap<String, String> map = new HashMap<>();
        try{
            String data = Files.readString(Paths.get(path));
            data = data.trim();

            if(data.startsWith("{")&&data.endsWith("}"))
                data = data.substring(1, data.length()-1).trim();

            List<String> arguements = splitlevels(data);

            for(String s: arguements){
                String[] content = s.split(":", 2);

                String key = content[0].trim().replaceAll("^\"|\"$", "");
                String rawValue = content[1].trim();
                String val = "";

                if(rawValue.startsWith("\""))
                    val = rawValue.replaceAll("^\"|\"$", "");
                else{
                    val = rawValue.trim();
                }

                map.put(key, val);
            }

        } catch (IOException e){
            System.out.println("Error reading config file " + e.getMessage());
        }
        return map;
    }

    private static List<String> splitlevels(String data){
        ArrayList<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(char c: data.toCharArray()){
            if(c==',') {
                list.add(sb.toString());
                sb.setLength(0);
            }
            else
                sb.append(c);
        }

        if(!sb.isEmpty()) list.add(sb.toString());

        return list;
    }
}
