package com.geekmere;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class Main
{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) throws IOException
    {

        File set = new File(user_set.set_path);

        if (!set.exists())
        {
            set.createNewFile();
        }

        while (true)
        {
            input();
        }
    }

    private static void input() throws IOException
    {
        Scanner scan = new Scanner(System.in);
        System.out.print(user_set.main_line);
        String comm = scan.nextLine();
        Handler.set_handler(comm);
    }


}
