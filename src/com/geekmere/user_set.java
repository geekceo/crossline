package com.geekmere;

import java.text.MessageFormat;

public class user_set
{
    public static  String last_comm = "help";
    public static String username = System.getProperty("user.name");
    public static String home_dir = System.getProperty("user.home");
    public static String main_line = MessageFormat.format("@{0}:{1}#$ ", username, home_dir);
}
