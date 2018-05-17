package rideScheduler;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.geom.Point2D;

public class Geocode {

  private static String baseURL = "https://maps.googleapis.com/maps/api/geocode/xml";
  private static String key = "AIzaSyBsw3d3BJE00w4P9yNh_zKP3HbsiipzJ1Q";

  private static Document getDOMFromURL(String u) throws IOException {
    URL url = new URL(u);
    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    StringBuilder sb = new StringBuilder();
    String curr = in.readLine();
    while (curr != null) {
      sb.append(curr);
      curr = in.readLine();
    }
    return Jsoup.parse(sb.toString(), "", Parser.xmlParser());
  }

  public static Point2D.Float getCoordinates(String addr) {
    String formattedAddr = addr.replace(" ", "+");
    Document doc = null;
    Element loc = null;
    try {
      doc = getDOMFromURL(baseURL + "?address=" + formattedAddr + "&key=" + key);
      loc = doc.selectFirst("location");
      System.out.println(loc.selectFirst("lat").text());
      System.out.println(loc.selectFirst("lng").text());
      return new Point2D.Float(Float.parseFloat(loc.selectFirst("lat").text()), Float.parseFloat(loc.selectFirst("lng").text()));
    } catch (IOException e) {
      System.out.println("Could not get page for addr input: " + addr);
    }
    return new Point2D.Float(-1,-1);
  }
}
