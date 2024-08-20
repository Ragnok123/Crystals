package Crystals.arena.manager;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class WorldManager {
      public static void addWorld(String name, String path) {
            File from = new File(path + "/worlds/crystals/" + name);
            File to = new File(path + "/worlds/" + name);

            try {
                  FileUtils.copyDirectory(from, to);
            } catch (IOException var5) {
                  var5.printStackTrace();
            }

      }

      public static void deleteWorld(String name, String path) {
            try {
                  File directory = new File(path + "/worlds/" + name);
                  FileUtils.deleteDirectory(directory);
            } catch (IOException var3) {
                  var3.printStackTrace();
            }

      }
}
