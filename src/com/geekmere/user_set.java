package com.geekmere;

import java.text.MessageFormat;

public class user_set
{
    public static  String last_comm = "help";
    public static String username = System.getProperty("user.name");
    public static String home_dir = System.getProperty("user.home");
    public static String pwd = home_dir;
    public static  String set_path = home_dir + "/cl_set.cfg";
    public static String main_line = MessageFormat.format("@{0}:{1}#$ ", username, home_dir);
}
