package com.geekmere;

public class output_stream
{

    public output_stream(String out, int code) {
        switch (code)
        {
            case 0:
                System.out.println(out);
                break;

            case 1:
                System.out.println(Colors.RED + out + Colors.RESET);
                break;

            case 2:
                System.out.println(Colors.YELLOW + out + Colors.RESET);
                break;

            case 3:
                System.out.println(Colors.GREEN + out + Colors.RESET);
                break;

        }
    }

    public static String ous(String out, int code)
    {

        switch (code)
        {
            case 0:
                System.out.println(out);
                break;

            case 1:
                System.out.println(Colors.RED + out + Colors.RESET);
                break;

            case 2:
                System.out.println(Colors.YELLOW + out + Colors.RESET);
                break;

            case 3:
                System.out.println(Colors.GREEN + out + Colors.RESET);
                break;

        }

        return out;
    }
}
