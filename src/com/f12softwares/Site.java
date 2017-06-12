package com.f12softwares;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Site {

    private static Set set;
    private static final Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
    //    private static final Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.");
    private static String s;
    private static Matcher m;
    private static ArrayList<String> linkSet = new ArrayList<>();

    private static Set<String> failedLinks = new HashSet<String>();
    private static Set<String> processed = new HashSet<String>();
    private static final int EXPECTED_EMAIL = 2000;
    private static int counter = 0;
    //    static int page = 16;  // the current page in the set been processed
    private static int page = 0;
    private static int retryCount = 0;
    private static String currentUrl = null;
    private final static String URL ="http://www.nairaland.com";
    private final static String SAVE = "nairaland";

    public static void main(String[] args) throws IOException {
        // write your code here

        mealMail(URL);


//        linkSet.forEach((e)->{
//            if(counter>=EXPECTED_EMAIL)
////                break;
//                analytic();
////                return;
//            else{
//                mealMail(e);
//            }
//
//        });

        for (int i = 0; i < linkSet.size(); i++) {
//            linkSet.get(i)
//            if (counter >= EXPECTED_EMAIL)
            if (set.size() >= EXPECTED_EMAIL)
//                break;
                analytic();
//                return;
            else {
                mealMail(linkSet.get(i));
            }
        }
        writeToFile();

    }


    final static int TIME_OUT = 1000 * 60 * 2;

    private static void mealMail(String url) {
        currentUrl = url;
        if (set != null)
            if (set.size() >= EXPECTED_EMAIL)
                return;

        Document doc = null;
        if (set == null)
            set = new HashSet<String>();

        try {
            doc = Jsoup.connect(url).timeout(TIME_OUT).get();
            findLinks(doc);
            findEmails(doc);
        } catch (IOException e) {
            if (retryCount < 3) {
                retryCount++;
                mealMail(url);
            } else {
                failedLinks.add(url);
            }

            System.out.println("ERrrrrrrrrrrrrrrrrrrrrroooooooooooooo");
            e.printStackTrace();

        } finally {
            retryCount = 0;
        }

    }

    private static void writeToFile() throws IOException {

        System.out.println(set);
//
//
//        FileOutputStream fo = new FileOutputStream(new File(""));
        File file = new File("/Users/mac/Desktop/Folder/"+SAVE+".txt");
        if (!file.exists())
            file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        // Writes the content to the file
        writer.append("This sesion    \n\n");
        set.forEach((x) -> {
            try {
                if (page > 0) {
                    writer.append(x.toString() + "\n");
                } else {
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

    private static void findLinks(Document doc) {
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            linkSet.add(link.attr("abs:href"));
        }
        System.out.println("Total links found on : " + currentUrl + " is :" + links.size());
    }


    private static void findEmails(Document doc) {
        if (processed.contains(currentUrl))
            return;
        m = p.matcher(doc.body().toString());
        while (m.find()) {
//            if (counter >= EXPECTED_EMAIL) {
            if (set.size() >= EXPECTED_EMAIL) {
                doComplete();
                break;
            }
            counter++;
            String group = m.group();
            set.add(group);
            writeToTemp(group);
        }
        processed.add(currentUrl);
        System.out.println("Email Now Scraped: " + counter +".||    NO DUPLICATE: "+set.size());
    }


    private static void writeLeftUrlToFile() throws IOException {
        File file = new File("/Users/mac/Desktop/Folder/remainLink.txt");
        if (!file.exists())
            file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);
        linkSet.iterator().forEachRemaining((e) -> {
            try {
                writer.append(e.toString() + "\n");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        writer.flush();
        writer.close();
    }

    private static void doComplete() {

        try {
            tempWriter.flush();
            tempWriter.close();
//            writeLeftUrlToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static File tempfile = new File("/Users/mac/Desktop/Folder/tempEmail.txt");
    static FileWriter tempWriter;
    private static void writeToTemp(String x) {

        if (!tempfile.exists())
            try {
                tempfile.createNewFile();
                // creates a FileWriter Object
                FileWriter writer = new FileWriter(tempfile);
                linkSet.iterator().forEachRemaining((e) -> {
                    try {
                        tempWriter.append(e.toString() + "\n");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                });

            } catch (IOException e) {
                e.printStackTrace();
            }



    }
//
//    private static void print(String msg, Object... args) {
//        System.out.println(String.format(msg, args));
//    }
//
//    private static String trim(String s, int width) {
//        if (s.length() > width)
//            return s.substring(0, width - 1) + ".";
//        else
//            return s;
//    }

}

