package com.pgjbz.bot.starter.configs;

import com.pgjbz.bot.starter.factory.AbstractRepositoryFactory;
import com.pgjbz.bot.starter.model.Bot;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import static com.pgjbz.bot.starter.configs.BotConstants.CONFIG_FILE_SYSTEM_PROPERTY;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@Log4j2
public class Configuration {

   private static Map<String, String> configs;
   @Getter
   private static List<Bot> bots;

   public static void setEnvironment(String[] args) {
       String configFile = Stream.of(args).findFirst().orElse("");
       System.setProperty(CONFIG_FILE_SYSTEM_PROPERTY, configFile);
       bots = AbstractRepositoryFactory.getInstance().createBotRepository().findAll();
   }

   @SneakyThrows
   public static Map<String, String> getConfigs(String configFile)  {
       if(isNull(configs)) {
           log.info("Loading properties from {} ", configFile);
           configs = new HashMap<>();
           var properties = new Properties();
           if(isBlank(configFile))
               configFile = "config.properties";
           try(FileInputStream fs = new FileInputStream(configFile)) {
                   properties.load(fs);
               for (var key : properties.keySet())
                   configs.put(String.valueOf(key), properties.getProperty(String.valueOf(key)));
           } catch (IOException e) {
               log.error("Error: {} " , e.getMessage(), e);
               throw e;
           }
       }
       return configs;
   }

}
