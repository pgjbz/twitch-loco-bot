package com.pgjbz.bot.starter.configs;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
           try(InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream(propertiesFile)) {
               if (nonNull(inputStream))
                   properties.load(inputStream);
               else
                   throw new FileNotFoundException(String.format("Properties file '%s' not found", propertiesFile));

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
