package bstack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrowserBounce {
  static Map<String, String[]> browserInfo = new HashMap<>();

  public static void fill() {
    String[] chrome =
        {"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", "chrome.exe",
            "C:\\Users\\hp pc\\AppData\\Local\\Google\\Chrome\\User Data\\Default"};
    String[] firefox =
        {"C:\\Program Files\\Mozilla Firefox\\firefox.exe", "firefox.exe",
            "C:\\Users\\hp pc\\AppData\\Local\\Mozilla\\Firefox\\Profiles"};
    browserInfo.put("chrome", chrome);
    browserInfo.put("firefox", firefox);
  }

  @RequestMapping(value = "/start", method = RequestMethod.GET)
  public ResponseEntity<String> startBrowser(@RequestParam("browser") String browser,
      @RequestParam("url") String url) {
    Runtime runtime = Runtime.getRuntime();
    if ("chrome".equalsIgnoreCase(browser) || "firefox".equalsIgnoreCase(browser)) {
      try {
        runtime.exec(new String[] {browserInfo.get(browser)[0], url});
      } catch (IOException e) {
        e.printStackTrace();
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return new ResponseEntity<>("Browser Started", HttpStatus.OK);
  }

  @RequestMapping(value = "/stop", method = RequestMethod.GET)
  public ResponseEntity<String> stopBrowser(@RequestParam("browser") String browser) {
    Runtime runtime = Runtime.getRuntime();
    if ("chrome".equalsIgnoreCase(browser) || "firefox".equalsIgnoreCase(browser)) {
      try {
        runtime.exec("taskkill /F /IM " + browserInfo.get(browser)[1]);
      } catch (IOException e) {
        e.printStackTrace();
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return new ResponseEntity<>("Browser Stopped", HttpStatus.OK);
  }

  @RequestMapping(value = "/cleanup", method = RequestMethod.GET)
  public ResponseEntity<String> cleanup(@RequestParam("browser") String browser) {
    try {
      if ("chrome".equalsIgnoreCase(browser) || "firefox".equalsIgnoreCase(browser)) {
        FileUtils.deleteDirectory(new File(browserInfo.get(browser)[2]));
      }
    } catch (IOException e) {
      e.printStackTrace();
      return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>("Clean UP", HttpStatus.OK);
  }

  @RequestMapping(value = "/url", method = RequestMethod.GET)
  public String getUrl(@RequestParam("browser") String browser) {
    return null;
  }
}
