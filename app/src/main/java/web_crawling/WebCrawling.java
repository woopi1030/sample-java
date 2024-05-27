package web_crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawling {

    public static void main(String[] args) {
        System.out.println("### Web Crawling Test !!! ###");
        
        Document document;
        try {
            document = Jsoup.connect("https://www.38.co.kr/html/fund/index.htm?o=k").get();
            // System.out.println(document.body());

            // Elements trElements = document.select("table[summary='공모주 청약일정'] tr"); --> 이게 왜 안될까? 이해가 안되는군....
            Elements trElements = document.select("table[summary] tr");
            for (Element trElement : trElements) {
                // System.out.println(trElement.html());
    
                Elements tdElements = trElement.select("td");

                // 헤더 제외
                if (tdElements.size() != 7) {
                    continue;
                }
                tdElements.get(0).text(); // 종목명
                tdElements.get(1).text(); // 공모주일정
                tdElements.get(2).text(); // 확정공모가
                tdElements.get(3).text(); // 희망공모가
                tdElements.get(4).text(); // 청약경쟁률
                tdElements.get(5).text(); // 주간사

                System.out.println(tdElements.get(0).text());
                System.out.println(tdElements.get(0).select("a").attr("href"));
                System.out.println(tdElements.get(1).text());
                System.out.println(tdElements.get(2).text());
                System.out.println(tdElements.get(3).text());
                System.out.println(tdElements.get(4).text());
                System.out.println(tdElements.get(5).text());
                System.out.println();
                

            }

        } catch (Exception e) {
            e.printStackTrace();            
        }

        

    }
    
}
