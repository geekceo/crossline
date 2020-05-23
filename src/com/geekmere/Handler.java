package com.geekmere;

import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;

/* COMMANDS
* cd - Переход пути
* ls - вывод файлов в директории
* alias - создает в файле конфигурации сокращенные команды
* read - чтение из файла и вывод содержимого на экран
* write - аналог echo
* upl - Uniform Packet Loader - загрузчик пакетов приложений
*/

public class Handler
{

    public static String pwd = user_set.home_dir;
    private static final String PATTERN = "%s\r";
    public static String now_pack;

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

    public static String equal_tabs(File[] files, String curr_owner) throws IOException
    {
        int largest, diff;
        String files_str, tabs = "", owner = "";

        files_str = files[0].toString();
        try
        {
            owner = Files.getOwner(Paths.get(files_str)).getName();
        }
        catch (Exception e)
        {

        }

        largest = owner.length();
        for (int i = 0; i < files.length; i++)
        {
            files_str = files[i].toString();
            try
            {
                owner = Files.getOwner(Paths.get(files_str)).getName();
            }
            catch (Exception e)
            {

            }
            if (largest < owner.length())
            {
                largest = owner.length();
            }
        }
        diff = largest - curr_owner.length();

        if (diff != 0)
        {
            for (int i = 0; i < diff; i++)
            {
                tabs += " ";
            }
        }

        return tabs;
    }

    public static void comm_handler(String comm) throws IOException
    {
        String[] command = comm.split(" ");
        DownThread downThread = new DownThread();

        String[] alias_log_exceptions =
                {
                "alias: more than one argument",
                "alias: argument doesn't contain delimiter '/', for example - e/exit",
                };

        String[] write_log_exceptions =
                {
                        "write: more than one argument"
                };

        String[] cd_log_exceptions =
                {
                        "cd: not a directory"
                };

        String[] read_log_exceptions =
                {
                        "read: more than one argument",
                        "read: file not found",
                        "read: not a file"
                };

        String[] ls_log_exceptions =
                {
                        "ls: more than one argument",
                        "ls: argument doesn't contain delimiter '/', for example - e/exit",
                };

        String[] upl_log_exceptions =
                {
                        "upl: wrong argument",
                        "upl: argument doesn't contain delimiter '/', for example - e/exit",
                };

        String[] help_log_exceptions =
                {
                        "help: unknowns argument"
                };

        ArrayList<String> args = args_finder(comm);

        if (command[0].equals("ls"))
        {
            File dir = new File(pwd);
            File[] files = dir.listFiles();
            String files_str = "", files_list = "";
            File file;
            int largest = 0, boof = 0;
            String owner = "", read = "", write = "", exec = "";
            boolean checker;

            if (DiffOs.isWindows())
            {
                if (comm.equals("ls") && files != null)
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
                                files_list += files_str.toCharArray()[j];
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
                        if (!args.isEmpty())
                        {
                            if (args.get(0).equals("-a") && files != null)
                            {
                                for (int i = 0; i < files.length; i++)
                                {
                                    files_str = files[i].toString();
                                    for (int j = files_str.lastIndexOf("/") + 1; j < files_str.length(); j++)
                                    {
                                        file = new File(files_str);
                                        if ((file.isDirectory() && file.isHidden()) || (file.isHidden()))
                                        {
                                            files_list += files_str.toCharArray()[j];
                                        }
                                        else if (file.isDirectory() && !file.isHidden())
                                        {
                                            files_list += files_str.toCharArray()[j];
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

                            if (args.get(0).equals("-x") && files != null)
                            {
                                output_stream.ous("owner" + equal_tabs(files, owner) + "    type/mod        filename\n", 99);
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
                                        files_list += owner + equal_tabs(files, owner) + MessageFormat.format("            d{0}{1}{2}", read,write,exec) + "        ";
                                    }
                                    else if (file.isDirectory() && !file.isHidden())
                                    {
                                        files_list += owner + equal_tabs(files, owner) + MessageFormat.format("            d{0}{1}{2}", read,write,exec) + "        ";
                                    }
                                    else
                                    {
                                        files_list += owner + equal_tabs(files, owner) + MessageFormat.format("            -{0}{1}{2}", read,write,exec) + "        ";
                                    }

                                    for (int j = files_str.lastIndexOf("/") + 1; j < files_str.length(); j++)
                                    {
                                        file = new File(files_str);
                                        if ((file.isDirectory() && file.isHidden()) || (file.isHidden()))
                                        {
                                            files_list += files_str.toCharArray()[j];
                                        }
                                        else if (file.isDirectory() && !file.isHidden())
                                        {
                                            files_list += files_str.toCharArray()[j]
                                            ;
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
                        }

                    }
                }
            }
            else
            {
                if (comm.equals("ls") && files != null)
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
                        if (!args.isEmpty())
                        {
                            if (args.get(0).equals("-a") && files != null)
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

                            if (args.get(0).equals("-x") && files != null && files.length != 0)
                            {
                                output_stream.ous("owner" + equal_tabs(files, owner) + "    type/mod        filename\n", 3);
                                for (int i = 0; i < files.length; i++)
                                {
                                    files_str = files[i].toString();

                                    file = new File(files_str);
                                    try
                                    {
                                        owner = Files.getOwner(Paths.get(files_str)).getName();
                                    }
                                    catch (Exception e)
                                    {

                                    }


                                    if (Files.isReadable(Paths.get(files_str))){read="r";}else{read="-";}
                                    if (Files.isWritable(Paths.get(files_str))){write="w";}else{write="-";}
                                    if (Files.isExecutable(Paths.get(files_str))){exec="x";}else{exec="-";}

                                    if ((file.isDirectory() && file.isHidden()) || (file.isHidden()))
                                    {
                                        files_list += Colors.GREEN + owner + equal_tabs(files, owner) + MessageFormat.format("            d{0}{1}{2}", read,write,exec) + "        ";
                                    }
                                    else if (file.isDirectory() && !file.isHidden())
                                    {
                                        files_list += Colors.GREEN + owner + equal_tabs(files, owner) + MessageFormat.format("            d{0}{1}{2}", read,write,exec) + "        ";
                                    }
                                    else
                                    {
                                        files_list += Colors.GREEN + owner + equal_tabs(files, owner) + MessageFormat.format("            -{0}{1}{2}", read,write,exec) + "        ";
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
            }



        }

        else if (command[0].equals("upl"))
        {
            if (comm.equals("upl"))
            {
                if (DiffOs.isWindows())
                {
                    output_stream.ous("upl -arg1 -arg2", 0);
                }
                else
                {
                    output_stream.ous("upl -arg1 -arg2", 0);
                }
            }
            else
            {
                if (args.size() > 2)
                {
                    if (DiffOs.isWindows())
                    {
                        output_stream.ous(upl_log_exceptions[0], 0);
                    }
                    else
                    {
                        output_stream.ous(upl_log_exceptions[0], 2);
                    }
                }
                else
                {
                    String pack_file = input_stream.read_file(user_set.home_dir + "/upl_pack.cl");
                    String[] pack_rep = pack_file.split("\n");
                    if ((args.get(0).contains("list")) && (args.size() == 1))
                    {
                        output_stream.ous("Packet list:", 0);

                        for (int i = 0; i < pack_rep.length; i++)
                        {
                            //output_stream.ous(pack_rep[i].split(";")[1], 0);
                            System.out.println(pack_rep[i].split(";")[1]);
                        }
                    }
                    if ((args.get(0).contains("install")) && (args.size() == 1))
                    {
                        output_stream.ous("Write name of packet", 0);
                    }
                    else if ((args.get(0).contains("install")) && (args.size() == 2))
                    {
                        if (DiffOs.isWindows())
                        {
                            downThread.start();
                        }
                        else
                        {
                            boolean check = false;
                            for (int i = 0; i < pack_rep.length; i++)
                            {
                                if (args.get(1).equals(pack_rep[i].split(";")[1]))
                                {
                                    now_pack = pack_rep[i].split(";")[0];
                                    check = true;
                                    break;
                                }
                            }
                            //output_stream.ous("Packet is Loading", 0);
                            if (check)
                            {
                                downThread.start();
                                while (downThread.isAlive()){}
                            }
                            else
                            {
                                output_stream.ous("Packet does not exist", 0);
                            }
                        }
                    }
                }
            }
            user_set.last_comm = comm;
        }

        else if (command[0].equals("alias"))
        {
            if (comm.equals("alias"))
            {
                if (DiffOs.isWindows())
                {
                    output_stream.ous("alias -arg", 0);
                }
                else
                {
                    output_stream.ous("alias -arg", 0);
                }
            }
            else
            {
                if (args.size() > 1)
                {
                    if (DiffOs.isWindows())
                    {
                        output_stream.ous(alias_log_exceptions[0], 0);
                    }
                    else
                    {
                        output_stream.ous(alias_log_exceptions[0], 2);
                    }
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
                        if (DiffOs.isWindows())
                        {
                            output_stream.ous(alias_log_exceptions[0], 0);
                        }
                        else
                        {
                            output_stream.ous(alias_log_exceptions[0], 2);
                        }
                    }
                }
            }
            user_set.last_comm = comm;
        }

        //TODO RELATIVE PATH
        else if(command[0].equals("cd"))
        {
            if (comm.equals("cd"))
            {
                output_stream.ous("cd --path_to_file", 0);
            }
            else
            {

                if (DiffOs.isWindows())
                {
                    if (args.size() > 0)
                    {
                        Path rel_path = Paths.get(pwd + "\\" + args.get(0));
                        Path abs_path = Paths.get("\\" + args.get(0));
                        if (Files.isDirectory(rel_path))
                        {
                            if (args.get(0).toCharArray()[0]!='\\')
                            {
                                pwd = "\\" +  args.get(0);
                            }
                            else
                            {
                                pwd = args.get(0);
                            }
                        }
                        else if(Files.isDirectory(abs_path))
                        {
                            if (args.get(0).toCharArray()[0]!='/')
                            {
                                pwd = "/" +  args.get(0);
                            }
                            else
                            {
                                pwd = args.get(0);
                            }
                        }
                        else
                        {
                            output_stream.ous(cd_log_exceptions[0], 0);
                        }
                    }
                }
                else
                {
                    if (args.size() > 0)
                    {
                        Path rel_path = Paths.get(pwd + "/" + args.get(0));
                        Path abs_path = Paths.get("/" + args.get(0));
                        if (Files.isDirectory(rel_path))
                        {
                            pwd += "/" + args.get(0);
                        }
                        else if(Files.isDirectory(abs_path))
                        {
                            pwd = args.get(0);
                        }
                        else
                        {
                            output_stream.ous(cd_log_exceptions[0], 1);
                        }
                    }
                }
            }
            user_set.last_comm = comm;
        }

        else if(command[0].equals("write"))
        {
            if (comm.equals("write"))
            {
                if (DiffOs.isWindows())
                {
                    output_stream.ous("write -arg, for example: write Hello, World!", 0);
                }
                else
                {
                    output_stream.ous("write -arg, for example: write Hello, World!", 2);
                }
            }
            else
            {
                if (args.size() > 1)
                {
                    if (!args.isEmpty())
                    {
                        if (DiffOs.isWindows())
                        {
                            output_stream.ous(write_log_exceptions[0], 0);
                        }
                        else
                        {
                            output_stream.ous(write_log_exceptions[0], 2);
                        }
                    }
                }
                else
                {
                    output_stream.ous(args.get(0), 0);
                }
            }
            user_set.last_comm = comm;
        }

        else if(command[0].equals("read"))
        {
            String content;
            if (comm.equals("read"))
            {
                if (DiffOs.isWindows())
                {
                    output_stream.ous("read -arg, for example: read file.txt", 0);
                }
                else
                {
                    output_stream.ous("read -arg, for example: read file.txt", 2);
                }
            }
            else
            {
                if (args.size() > 1)
                {
                    if (DiffOs.isWindows())
                    {
                        output_stream.ous(read_log_exceptions[0], 0);
                    }
                    else
                    {
                        output_stream.ous(read_log_exceptions[0], 2);
                    }
                }
                else
                {
                    content = input_stream.read_file(args.get(0));
                    new output_stream(content, 0);
                }
            }

            user_set.last_comm = comm;
        }

        else if (command[0].equals("help"))
        {
            if (comm.equals("help"))
            {
                output_stream.ous("CrossLine - it's crossplatform command interface.\n(c) GeekMere 2016-2020", 0);
            }
            else
            {
                if (args.contains("-p"))
                {
                    output_stream.ous("Person - Tagir Khalilov\n(c) GeekMere 2016-2020", 0);
                }
                else
                {

                }
            }
            user_set.last_comm = comm;
        }

        else if (command[0].equals("clear"))
        {
            user_set.last_comm = "clear";
            clear();
        }

        else if (command[0].equals("exit"))
        {
            System.exit(0);
        }

        else
        {
            if (!comm.isEmpty())
                output_stream.ous(MessageFormat.format("{0}: {1}: command not found", kernel_set.NAME, comm), 1);
        }
    }

    public static void clear()
    {
        if(DiffOs.isWindows())
        {
            try {
                Runtime.getRuntime().exec("cls");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    //TODO 'WRITE' IN FILE
    public static ArrayList<String> args_finder(String request)
    {
        String arg;
        char[] req_char = request.toCharArray();
        ArrayList<String> args = new ArrayList<String>();

        if (!request.contains("write"))
        {
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
        }
        else
        {
            for (int i = 0; i < request.length(); i++)
            {
                arg = "";
                try
                {
                    if (req_char[i] == ' ' &&  req_char[i+1] != ' ')
                    {
                        for (int j = i + 1; j < request.length(); j++)
                        {
                            if ((req_char[j] == ' ' && req_char[j+1] == '>') || req_char[j] == '>')
                            {
                                break;
                            }
                            else
                            {
                                arg += req_char[j];
                            }
                        }
                        args.add(arg);
                        break;
                    }
                }
                catch (Exception e)
                {
                    //e.printStackTrace();
                }
            }
        }

        return args;
    }

    public static class DownThread extends Thread {
        public void run() {
            String prog = "=";
            String[] loading = {"/","-","\\","|"};
            int load_count = 0;
            System.out.println();
            long packet_size = DownloadPacket();
            if (packet_size >= 0)
            {
                System.out.println("GET " + now_pack + " [OK]");
                System.out.println("Size: " + packet_size + " Bytes");

                for (int i = 0; i <= 9; i++) {
                    if (load_count == 3)
                        load_count = 0;
                    System.out.print("[" +(i+1)*10 + "%]");
                    System.out.format("   Download " + "[" + loading[load_count] + "] " + PATTERN, prog);
                    prog += "=";
                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    load_count++;
                }
                output_stream.ous("\nDownload success", 2);
                String[] a = {"g"};
            }
            else
            {
                System.out.println("GET" + now_pack + "[ERROR]");
                System.out.println("No connection!");
            }
        }
    }

    public static long DownloadPacket()
    {
        long removeFileSize = 0;
        URL url = null;
        try {
            url = new URL(now_pack);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            httpConnection.setRequestMethod("HEAD");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        removeFileSize = httpConnection.getContentLengthLong();
        return removeFileSize;
    }
}
