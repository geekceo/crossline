package com.geekmere;

import java.awt.event.KeyEvent;
import java.util.Scanner;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class Main {

    public static void main(String[] args)
    {
        while (true)
        {
            input();
        }
    }

    private static void input()
    {
        Scanner scan = new Scanner(System.in);
        System.out.print(user_set.main_line);
        String comm = scan.nextLine();
        Handler.comm_handler(comm);
    }

    public static void output(String out)
    {
        System.out.println(out);
    }


}
