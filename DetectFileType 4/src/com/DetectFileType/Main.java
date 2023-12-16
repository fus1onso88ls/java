package com.DetectFileType;
import java.util.*;
import java.io.*;

public class Main {
    public static thetype[] thetypes;
    public static boolean savebase(String filename){
        try{
            FileWriter writer = new FileWriter(filename, false);
            for(int i = 0; i< thetypes.length; i++) {
                String S=thetypes[i].savebase();
                if (i<thetypes.length-1) {
                    S=S+"\n";
                }
                writer.write(S);
            }
            writer.flush();
            writer.close();
            return true;
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public static boolean loadbase(String filename){
        try{
            FileReader fr = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fr);
            int i=1;
            int n=0;
            String line=reader.readLine();
            while (line !=null)
            {
                if (thetype.checkloadbase(line)){
                    n++;
                } else{
                    System.out.println("Ошибка в базе! Строка "+Integer.toString(i));
                }
                line=reader.readLine();
                i++;
            }
            reader.close();
            if (n==0){
                System.out.println("База пустая!");
                return false;
            }
            thetypes = new thetype[n];
            n=0;
            fr = new FileReader(filename);
            reader = new BufferedReader(fr);
            line=reader.readLine();
            while (line !=null)
            {
                if (thetype.checkloadbase(line)){
                    thetypes[n] = new thetype(line);
                    n++;
                }
                line=reader.readLine();
            }
            reader.close();
            return true;
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    public static void main(String[] args) {

        if (!loadbase("bdfiletype.txt")){
            return;
        }
        Scanner scanner = new Scanner(System.in);
        String filename="";
        int readcount;
        while(true) {
            System.out.println("Введите имя файла");
            boolean enter=false;
            while (!enter) {
                try {
                    filename = scanner.nextLine();
                    if (filename.length()>0){
                        enter=true;
                    }
                } catch(InputMismatchException ex) {
                    System.out.println("Ошибка ввода");
                    enter=false;
                }
            }
            if (filename.equals("exit")){
                break;
            }
            byte[] buffer = new byte[256];
            try {
                FileInputStream fin = new FileInputStream(filename);
                readcount = fin.read(buffer);
                fin.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                readcount = -1;
            }

            if (readcount > 12) {
                boolean matched = false;
                for (int i = 0; !matched && i < thetypes.length; i++) {
                    if (thetypes[i].checkbuffer(buffer)) {
                        if (thetypes[i].typenum==1){//одно расширение
                            boolean add_ex=true;
                            if (filename.length()>thetypes[i].typename.length()){
                                String curtypename=filename.substring(filename.length()-thetypes[i].typename.length());
                                if (curtypename.equals(thetypes[i].typename)){
                                    add_ex=false;
                                }
                            }
                            if (add_ex){
                                File file = new File(filename);
                                File file2 = new File(filename+thetypes[i].typename);
                                if (file.renameTo(file2)) {
                                    System.out.println("Добавлено расширение файла " + thetypes[i].typename);
                                }
                                else{
                                    System.out.println("Неудачная попытка добавить расширение файла " + thetypes[i].typename);
                                }

                            }
                            else{
                                System.out.println("Найдено в базе расширение файла " + thetypes[i].typename);
                            }
                        }
                        else{
                            System.out.println("Найдено в базе расширение файла " + thetypes[i].typename);
                        }
                        matched = true;
                    }
                }
                if (!matched) {
                    System.out.println("Расширение файла в базе не найдено!");
                }

            } else if (readcount >= 0) {
                System.out.println("Ошибка с размером файла!");
            }
        }
    }
}
