package com.geekmere;

public class output_stream
{
    public static String ous(String out, int code)
    {
        String output = "";

        switch (code)
        {
            case 0:
                System.out.println(out);
                break;

            case 1:
                System.out.println("\u001B[31m" + out + "\u001B[0m");
                break;

            case 2:
                System.out.println("\u001B[33m" + out + "\u001B[0m");
                break;

            case 3:
                System.out.println("\u001B[32m" + out + "\u001B[0m");
                break;

            default:
                System.out.println(out);
                break;
        }

        return output;
    }
}
