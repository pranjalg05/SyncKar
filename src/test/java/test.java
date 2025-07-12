import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test  {
    public static void main(String[] args) {
        Path src = Paths.get("src/test/resources/source");
        Path dest = Paths.get("src/test/resources/target");
        try {
            var list = Files.walk(src).toList();
            for(Path p: list){
                p = src.relativize(p);
                System.out.println(p);
                var t = dest.resolve(p);
                System.out.println(Files.exists(t));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
