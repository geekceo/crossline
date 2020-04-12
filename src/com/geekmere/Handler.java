package com.geekmere;

import java.io.*;
import java.util.ArrayList;

public class Handler
{
    /*
    try {
                            FileReader fr = new FileReader(user_set.set_path);
                            BufferedReader reader = new BufferedReader(fr);
                            String line = reader.readLine();
                            while (line != null)
                            {

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
    */

    public static void set_handler(String comm) throws IOException
    {
        boolean find = false;
        String f_comm = "";

        FileReader fr = new FileReader(user_set.set_path);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null)
        {
            if (line.contains(comm))
            {
                find = true;
                char[] line_char = line.toCharArray();
                for (int i = line.indexOf("/")+1; i < line.length(); i++)
                {
                    f_comm += line_char[i];
                }
                comm_handler(f_comm);
            }
            line = reader.readLine();
        }
        reader.close();
        fr.close();
        if (find == false)
        {
            comm_handler(comm);
        }
    }

    public static void comm_handler(String comm)
    {

        String[] log_exceptions =
                {
                "alias: more than one argument",
                "alias: argument doesn't contain delimiter '/', for example - e/exit",
                };

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
                    Main.output(log_exceptions[0], 1);
                }
                else
                {
                    if (args.get(0).contains("/"))
                    {
                        try(FileWriter writer = new FileWriter(user_set.set_path, true))
                        {
                            writer.write(args.get(0) + '\n');

                            writer.flush();
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Main.output(log_exceptions[1], 1);
                    }
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
                //e.printStackTrace();
            }
        }
        return args;
    }
}
