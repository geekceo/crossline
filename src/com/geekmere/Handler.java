package com.geekmere;

import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipal;
import java.text.MessageFormat;
import java.util.ArrayList;

public class Handler
{
    public static void set_handler(String comm) throws IOException
    {
        boolean find = false;
        String f_comm = "";

        FileReader fr = new FileReader(user_set.set_path);
        BufferedReader reader = new BufferedReader(fr);
        String line = reader.readLine();
        while (line != null)
        {
            char[] line_char = line.toCharArray();
            String alias = "";
            int index_sl = line.indexOf("/");
            for (int i = 0; i < index_sl; i++)
            {
                alias += line_char[i];
            }
            if (alias.equals(comm))
            {
                find = true;
                for (int j = index_sl+1; j < line.length(); j++)
                {
                    f_comm += line_char[j];
                }
                comm_handler(f_comm);
            }
            line = reader.readLine();
        }
        reader.close();
        fr.close();
        if (!find)
        {
            comm_handler(comm);
        }
    }

    public static void comm_handler(String comm) throws IOException
    {

        String[] alias_log_exceptions =
                {
                "alias: more than one argument",
                "alias: argument doesn't contain delimiter '/', for example - e/exit",
                };

        String[] ls_log_exceptions =
                {
                        "ls: more than one argument",
                        "alias: argument doesn't contain delimiter '/', for example - e/exit",
                };

        String[] help_log_exceptions =
                {
                        "help: unknowns argument",
                };

        ArrayList<String> args = args_finder(comm);

        if (comm.contains("ls"))
        {
            File dir = new File(user_set.pwd);
            File[] files = dir.listFiles();
            String files_str = "", files_list = "";
            File file;
            String owner, read = "", write = "", exec = "";
            boolean checker;

            if (comm.equals("ls"))
            {
                for (int i = 0; i < files.length; i++)
                {
                    checker = false;
                    files_str = files[i].toString();
                    for (int j = files_str.lastIndexOf("/") + 1; j < files_str.length(); j++)
                    {
                        file = new File(files_str);
                        //System.out.println(files_list.toCharArray()[j]);
                        if ((file.isDirectory() && !file.isHidden()))
                        {
                            checker = true;
                            files_list += Colors.CYAN + files_str.toCharArray()[j] + Colors.RESET;
                        }
                        else if (file.isFile() && !file.isHidden())
                        {
                            checker = true;
                            files_list += files_str.toCharArray()[j];
                        }
                    }
                    if (checker)
                    {
                        files_list += "\n";
                    }
                }
                output_stream.ous(files_list, 0);
            }
            else
            {
                if (args.size() > 1)
                {
                    output_stream.ous(ls_log_exceptions[0], 2);
                }
                else
                {
                    if (args.get(0).equals("-a"))
                    {
                        for (int i = 0; i < files.length; i++)
                        {
                            files_str = files[i].toString();
                            for (int j = files_str.lastIndexOf("/") + 1; j < files_str.length(); j++)
                            {
                                file = new File(files_str);
                                if ((file.isDirectory() && file.isHidden()) || (file.isHidden()))
                                {
                                    files_list += Colors.RED + files_str.toCharArray()[j] + Colors.RESET;
                                }
                                else if (file.isDirectory() && !file.isHidden())
                                {
                                    files_list += Colors.CYAN + files_str.toCharArray()[j] + Colors.RESET;
                                }
                                else
                                {
                                    files_list += files_str.toCharArray()[j];
                                }
                            }
                            files_list += "\n";
                        }
                        output_stream.ous(files_list, 0);
                    }

                    if (args.get(0).equals("-x"))
                    {
                        output_stream.ous("owner        type/mod        filename\n", 3);
                        for (int i = 0; i < files.length; i++)
                        {
                            files_str = files[i].toString();

                            file = new File(files_str);
                            owner = Files.getOwner(Paths.get(files_str)).getName();

                            if (Files.isReadable(Paths.get(files_str))){read="r";}else{read="-";}
                            if (Files.isWritable(Paths.get(files_str))){write="w";}else{write="-";}
                            if (Files.isExecutable(Paths.get(files_str))){exec="x";}else{exec="-";}

                            if ((file.isDirectory() && file.isHidden()) || (file.isHidden()))
                            {
                                files_list += Colors.GREEN + owner + MessageFormat.format("            d{0}{1}{2}", read,write,exec) + "        ";
                            }
                            else if (file.isDirectory() && !file.isHidden())
                            {
                                files_list += Colors.GREEN + owner + MessageFormat.format("            d{0}{1}{2}", read,write,exec) + "        ";
                            }
                            else
                            {
                                files_list += Colors.GREEN + owner + MessageFormat.format("            -{0}{1}{2}", read,write,exec) + "        ";
                            }

                            for (int j = files_str.lastIndexOf("/") + 1; j < files_str.length(); j++)
                            {
                                file = new File(files_str);
                                if ((file.isDirectory() && file.isHidden()) || (file.isHidden()))
                                {
                                    files_list += Colors.RED + files_str.toCharArray()[j] + Colors.RESET;
                                }
                                else if (file.isDirectory() && !file.isHidden())
                                {
                                    files_list += Colors.CYAN + files_str.toCharArray()[j] + Colors.RESET;
                                }
                                else
                                {
                                    files_list += Colors.RESET + files_str.toCharArray()[j];
                                }
                            }
                            files_list += "\n";
                        }
                        output_stream.ous(files_list, 0);
                    }
                }
            }

        }

        else if (comm.contains("alias"))
        {
            if (comm.equals("alias"))
            {
                output_stream.ous("alias -arg", 0);
            }
            else
            {
                if (args.size() > 1)
                {
                    output_stream.ous(alias_log_exceptions[0], 2);
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
                        output_stream.ous(alias_log_exceptions[1], 2);
                    }
                }
            }
            user_set.last_comm = comm;
        }

        else if (comm.contains("help"))
        {
            if (comm.equals("help"))
            {
                output_stream.ous("CrossLine - it's crossplatform command interface.\n(c) GeeMere 2016-2020", 0);
            }
            else
            {
                if (args.contains("-p"))
                {
                    output_stream.ous("Person - Tagir Khalilov\n(c) GeeMere 2016-2020", 0);
                }
                else
                {

                }
            }
            user_set.last_comm = comm;
        }

        else if (comm.equals("clear"))
        {
            user_set.last_comm = "clear";
            clear();
        }

        else if (comm.equals("exit"))
        {
            System.exit(0);
        }

        else
        {
            output_stream.ous(MessageFormat.format("{0}: {1}: command not found", kernel_set.NAME, comm), 1);
        }
    }

    public static void clear()
    {
        for (int i = 0; i <= 127; i ++)
        {
            output_stream.ous((char)8 + " " + (char)8, 0);
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
