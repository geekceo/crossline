package com.geekmere;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class input_stream {

    public static String read_file(String path) throws IOException
    {
        String content = "";
        FileReader fr = new FileReader(path);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null)
        {
            content += line + "\n";
            line = reader.readLine();
        }
        reader.close();
        fr.close();
        return content;
    }
}
