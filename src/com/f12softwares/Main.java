package com.f12softwares;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Set set;
    private static final Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
    //    private static final Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.");
    private static String s;
    private static Matcher m;

    public static void main(String[] args) throws IOException {
        // write your code here

        mealMail();
        writeToFile();
        analytic();

    }


    static int  counter= 0;
    static int page = 16;
    static int retryCount =0;
    private static void mealMail() {
        Document doc = null;
        set = new HashSet<String>();

        try {
            for (int i = page; i < 20; i++) {
                page=i;
//                doc = Jsoup.connect("http://www.nairaland.com/search/%40gmail.com/0/0/0/1").get();
                doc = Jsoup.connect("http://www.nairaland.com/search/email/0/0/0/"+(i+1)).timeout(1000*60*5).get();

                m = p.matcher(doc.body().toString());

                while (m.find()) {
                    counter++;
//                    System.out.println(m.group());
                    set.add(m.group());
                }


                System.out.println("Page: "+(page+1));
            }
        } catch (IOException e) {
//            if(retryCount<10){
//                retryCount++;
//                mealMail();
//
//            }
            System.out.println("ERrrrrrrrrrrrrrrrrrrrrroooooooooooooo");
            e.printStackTrace();

        }

    }

    private static void writeToFile() throws IOException {
//        FileOutputStream fo = new FileOutputStream(new File(""));
        File file = new File("/Users/mac/Desktop/Folder/emails.txt");
        if (!file.exists())
            file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        // Writes the content to the file
        set.forEach((x) -> {
            try {
               if(page>0){
                   writer.append(x.toString() + "\n");
               }else {
                   writer.write(x.toString() + "\n");
               }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.flush();
        writer.close();

    }


    private static void analytic() {
        int total = counter - set.size();
        System.out.println(counter + " email scrapped\n" + total + " duplicate removed\n" + set.size() + "Writen to file");
    }


}


