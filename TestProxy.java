import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestProxy {
    public static void main(String[] args) throws IOException {
        while (true) {
            getProxyList();
            File file = new File("/usr/lib/zabbix/alertscripts/Proxy.txt");
            String url2 = "https://core.telegram.org";
            URL ur = new URL(url2);
            File file2 = new File("/usr/lib/zabbix/alertscripts/Log.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file2, true));
            FileInputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String str = new String(buffer);
            inputStream.close();
            String[] masStr = str.split("[\\r\\n]+");
            writer.write(masStr[0]);
            writer.newLine();
            writer.write(masStr[1]);
            writer.newLine();
            for (int i = 2; i < masStr.length; i++) {
                boolean b1 = true, b2 = true;
                while (b1) {
                    System.out.println("for ip: " + masStr[i]);
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(masStr[i], 3128));
                        HttpURLConnection connPr = (HttpURLConnection) ur.openConnection(proxy);
                        connPr.setConnectTimeout(10000);
                        connPr.connect();
                        if (connPr.getResponseCode() == 200) {

                            writer.write(" using : " + masStr[i] + " at: " + LocalDateTime.now() + " - all ok");
                            writer.newLine();
                            writer.flush();
                            scriptReWrite(masStr[i],b2);
                            b2=false;
                            int a = (int) (Math.random() * 600000);
                            System.out.println(a);
                            Thread.sleep(600000 + a);

                        }

                        connPr.disconnect();
                    } catch (Exception e) {
                        System.out.println("error");
                        writer.write(masStr[i] + " Dead at: " + LocalDateTime.now());
                        writer.newLine();
                        writer.flush();
                        b1 = false;
                    }
                }

            }
            writer.flush();
            writer.close();
        }


    }

    public static void getProxyList() throws IOException {
        int count = 0, workcount = 0, workWithProxyCount = 0;
        String url = "https://free-proxy-list.net";
        String url2 = "https://core.telegram.org";
        URL ur = new URL(url2);
        File file = new File("/usr/lib/zabbix/alertscripts/Proxy.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        System.out.println("test1 "+LocalDateTime.now());

            Connection conn = Jsoup.connect(url).timeout(108000);
            System.out.println("test2 " + LocalDateTime.now());
            Document doc = conn.userAgent("Mozilla").get();



        Elements element = doc.getElementsByAttributeValue("class", "odd");
        Element table = doc.select("table").first();
        Elements rows = table.select("tr");
        writer.write(" New list from: " + LocalDateTime.now());
        writer.newLine();
        writer.write("=======================================================");
        writer.newLine();
        for (int i = 1; i < rows.size() - 1; i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");
            if (("3128".equals(cols.get(1).text())) && (!"Russian Federation".equals(cols.get(3).text()))) {

                System.out.println(cols.get(0).text());
                System.out.println(cols.get(1).text());
                count++;
                if (reach(cols.get(0).text())) {
                    workcount++;
                    try {
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(cols.get(0).text(), 3128));
                        HttpURLConnection connPr = (HttpURLConnection) ur.openConnection(proxy);
                        connPr.setConnectTimeout(4000);
                        connPr.connect();
                        if (connPr.getResponseCode() == 200) {
                            workWithProxyCount++;
                            writer.write(cols.get(0).text());
                            writer.newLine();
                        }
                        connPr.disconnect();
                    } catch (Exception e) {
                        System.out.println("error");

                    }
                }

            }
        }
        writer.flush();
        writer.close();
        System.out.println(count);
        System.out.println(workcount);
        System.out.println(workWithProxyCount);
    }

    public static boolean reach(String adres) throws IOException {

        InetAddress test = InetAddress.getByName(adres);

        if (test.isReachable(4000)) {
            System.out.println("good");
            return true;
        } else
            return false;
    }

    public static void scriptReWrite(String s,boolean bo) throws IOException
    {
        if (bo)
        {
            BufferedReader readerScr = new BufferedReader(new FileReader("/usr/lib/zabbix/alertscripts/zabbix-to-telegram-bot.sh"));
            List<String> scriptContent = new ArrayList<>();
            String line;
            while ((line = readerScr.readLine())!=null)
            {
                scriptContent.add(line);
            }
            readerScr.close();
            String s0 = "curl -x http://";
            String s1 = ":3128 --header 'Content-Type: application/json' --request 'POST' --data \"{\\\"chat_id\\\":\\\"465801197\\\",\\\"text\\\":\\\"${SUBJECT}\\n${MESSAGE}\\\"}\" \"https://api.telegram.org/bot523584658:AAE5QmWYsbhrDRZaEtFcFw5bVj7VdZ17s4c/sendMessage\" | grep -q '\"ok\":false,'";
            String s2 = ":3128 --header 'Content-Type: application/json' --request 'POST' --data \"{\\\"chat_id\\\":\\\"88512903\\\",\\\"text\\\":\\\"${SUBJECT}\\n${MESSAGE}\\\"}\" \"https://api.telegram.org/bot523584658:AAE5QmWYsbhrDRZaEtFcFw5bVj7VdZ17s4c/sendMessage\" | grep -q '\"ok\":false,'";

            scriptContent.set(8,s0+s+s1);
            scriptContent.set(9,s0+s+s2);
            BufferedWriter writerScr = new BufferedWriter(new FileWriter("/usr/lib/zabbix/alertscripts/zabbix-to-telegram-bot.sh"));
            for (int j = 0; j < scriptContent.size(); j++) {
                writerScr.write(scriptContent.get(j));
                writerScr.newLine();

            }
            writerScr.close();

        }
    }
}
