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
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        Scanner scan = new Scanner(System.in);
        System.out.print(user_set.main_line);
        String comm = scan.next();
        Handler.comm_handler(comm);
    }

    public static void output(String out)
    {
        System.out.println(out);
    }

    private static void formKeyPressed(java.awt.event.KeyEvent evt) {
        switch (evt.getKeyCode()) {

            case KeyEvent.VK_UP:
                Scanner scan = new Scanner(System.in);
                System.out.print(user_set.main_line);
                String comm = scan.next(user_set.last_comm);
                Handler.comm_handler(comm);
                break;

            case KeyEvent.VK_DOWN:

                break;

            default:
        }
    }

}
