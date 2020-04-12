package com.geekmere;

import java.io.IOException;
import java.util.ArrayList;

public class Handler
{
    public static void comm_handler(String comm)
    {
        if (comm.equals("help"))
        {
            Main.output("CrossLine - it's crossplatform command interface.\n(c) GeeMere 2016-2020");
            user_set.last_comm = "help";
        }

        if (comm.equals("clear"))
        {
            user_set.last_comm = "clear";
            clear();
        }

        if (comm.equals("exit"))
        {
            System.exit(0);
        }
    }

    public static void clear()
    {
        for (int i = 0; i <= 255; i ++)
        {
            Main.output((char)8 + " " + (char)8);
        }
    }

    public static void args_finder(String request)
    {
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        ArrayList<String> args = new ArrayList<String>()

        for (int i = 0; i < request.length(); i++)
        {
            try
            {
                if ()
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
