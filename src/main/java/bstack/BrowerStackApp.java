package bstack;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrowerStackApp {

  public static void main(String[] args) {
    BrowserBounce.fill();
    SpringApplication.run(BrowerStackApp.class, args);
  }

}
