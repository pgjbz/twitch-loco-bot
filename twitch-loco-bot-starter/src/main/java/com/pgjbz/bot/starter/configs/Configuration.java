package com.pgjbz.bot.starter.configs;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import static java.util.Objects.isNull;

@Log
@Getter
public class Configuration {

   private static Map<String, String> configs;

   @SneakyThrows
   public static Map<String, String> getConfigs()  {
       if(isNull(configs)) {
           configs = new HashMap<>();
           var properties = new Properties();
           String propertiesFile = "config.properties";
           try(FileInputStream fs = new FileInputStream(propertiesFile)) {
                   properties.load(fs);
               for (var key : properties.keySet())
                   configs.put(String.valueOf(key), properties.getProperty(String.valueOf(key)));
           } catch (IOException e) {
               log.log(Level.SEVERE, "Error: " + e.getMessage(), e);
               throw e;
           }
       }
       return configs;
   }

}
