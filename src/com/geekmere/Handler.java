package com.geekmere;

import java.io.IOException;
import java.util.ArrayList;

public class Handler
{
    public static void comm_handler(String comm)
    {
        ArrayList<String> args = args_finder(comm);

        if (comm.contains("alias"))
        {
            if (comm.equals("alias"))
            {
                Main.output("alias -arg", 0);
            }
            else
            {
                if (args.size() > 1)
                {
                    Main.output("alias: unknowns arguments", 1);
                }
                else
                {
                    System.out.println(args.get(0));
                }
            }
            user_set.last_comm = comm;
        }

        if (comm.contains("help"))
        {
            if (comm.equals("help"))
            {
                Main.output("CrossLine - it's crossplatform command interface.\n(c) GeeMere 2016-2020", 0);
            }
            else
            {
                if (args.contains("-p"))
                {
                    Main.output("Person - Tagir Khalilov\n(c) GeeMere 2016-2020", 0);
                }
            }
            user_set.last_comm = comm;
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
        for (int i = 0; i <= 127; i ++)
        {
            Main.output((char)8 + " " + (char)8, 0);
        }
    }

    public static ArrayList<String> args_finder(String request)
    {
        String arg;
        char[] req_char = request.toCharArray();
        ArrayList<String> args = new ArrayList<String>();

        for (int i = 0; i < request.length(); i++)
        {
            arg = "";
            try
            {
                if (req_char[i] == ' ' &&  req_char[i+1] != ' ')
                {
                    for (int j = i + 1; j < request.length(); j++)
                    {
                        if (req_char[j] == ' ')
                        {
                            break;
                        }
                        else
                        {
                            arg += req_char[j];
                        }
                    }
                    args.add(arg);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return args;
    }
}
