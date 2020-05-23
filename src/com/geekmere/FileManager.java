package com.geekmere;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileManager {
    private static Desktop desktop = null;

    public static void openFile(String path)
    {
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            desktop.open(new File(path));
        } catch (IOException e) {
            output_stream.ous("You don't have permission to open this package. Check your user permissions.", 0);
        }
    }
}
