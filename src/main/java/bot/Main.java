package bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import res.resourceLoader;


public class Main {
		    
	public static void main(String[] args) {
		
    
	    Properties prop = resourceLoader.loadProperties("configuracion.properties");
        
        try {
        	// Instantiate Telegram Bots API
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);           
            Principal bot = new Principal();
            botsApi.registerBot(bot);                   
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("Restaurants4U BOT successfully started (v 1.2.1) !!!!!!!!!!!");       
    }
}