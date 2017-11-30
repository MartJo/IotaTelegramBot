package Iota.Bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;



public class IotaTelegramBot extends TelegramLongPollingBot {
	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			String message = update.getMessage().getText().toString().toLowerCase();

			if(message.equals("/iota")) {
				Ticker tickerEUR = null;
				Ticker tickerUSD = null;

				try {
					tickerEUR = getIotaTickerInformation("https://api.cryptonator.com/api/ticker/iot-Eur");
					tickerUSD = getIotaTickerInformation("https://api.cryptonator.com/api/ticker/iot-usd");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				SendMessage sendMessage = new SendMessage() // Create a SendMessage object with mandatory fields
						.setChatId(update.getMessage().getChatId())
						.setText("IOTA\n" + tickerEUR.getPrice() + tickerEUR.getTarget() + "   " + tickerEUR.getChange() + "% (1h) \n" + tickerUSD.getPrice() + tickerUSD.getTarget() + "   " + tickerUSD.getChange() + "% (1h)" );
				try {
					execute(sendMessage); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String getBotUsername() {
		return "IotaBot";
	}

	@Override
	public String getBotToken() {
		// TODO
		return "489957281:AAHloWTMQczAdfOplPMXfl4sqyGtss7OxPE";
	}

	private Ticker getIotaTickerInformation(String url) throws Exception {
		String jason = readUrl(url);
		Ticker ticker = new Ticker();

		JSONObject obj = new JSONObject(jason);

		JSONObject tickerObj = obj.getJSONObject("ticker");

		ticker.setBase(tickerObj.getString("base"));
		ticker.setTarget(tickerObj.getString("target"));
		ticker.setPrice(tickerObj.getString("price"));
		ticker.setVolume(tickerObj.getString("volume"));
		ticker.setChange(tickerObj.getString("change"));

		System.out.println(tickerObj.getString("change"));
		return ticker;
	}

	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read); 

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

}

