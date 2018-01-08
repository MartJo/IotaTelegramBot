package Iota.Bot;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.toIntExact;

import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;



public class IotaTelegramBot extends TelegramLongPollingBot {
	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			String message = update.getMessage().getText().toString().toLowerCase();

			System.out.println(update.getMessage().getFrom());
			if(update.getMessage().getFrom().getFirstName().equals("Johannes")){
				SendMessage sendMessage = new SendMessage() // Create a SendMessage object with mandatory fields
						.setChatId(update.getMessage().getChatId())
						.setText("Johannes ist der Geilste!!!" );

				try {
					execute(sendMessage); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}

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
						.setText("IOTA\n" + tickerEUR.getPrice() + tickerEUR.getTarget() + "   " + tickerEUR.getChange() + "% (1h) \n" + tickerUSD.getPrice() + tickerUSD.getTarget() + "   " + tickerUSD.getChange() + "% (1h)");
				try {
					execute(sendMessage); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}

			if(message.equals("/donate")) {
				SendMessage sendMessage = new SendMessage() // Create a SendMessage object with mandatory fields
						.setChatId(update.getMessage().getChatId())
						.setText("Thanks for your support!\n99IQJBLMDAVTJNKOVZSWOKVIXJCAZYLOVWBARSCZPCVZWRMKWAUPGVBAIHMKPXSBRCFNOZTFCIIBCGRVXEK9BSAPIY");

				try {
					execute(sendMessage); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}

			if(message.equals("/help")) {
				SendMessage sendMessage = new SendMessage() // Create a message object object
						.setChatId(update.getMessage().getChatId())
						.setText("You send /help");
				InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
				List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
				List<InlineKeyboardButton> rowInline = new ArrayList<>();
				rowInline.add(new InlineKeyboardButton().setText("Current Iota price").setCallbackData("/iota"));
				// Set the keyboard to the markup
				rowsInline.add(rowInline);
				// Add it to the message
				markupInline.setKeyboard(rowsInline);
				sendMessage.setReplyMarkup(markupInline);
				rowInline.add(new InlineKeyboardButton().setText("Help to keep the Bot alive").setCallbackData("/donate"));
				try {
					execute(sendMessage); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}else if (update.hasCallbackQuery()) {
			String call_data = update.getCallbackQuery().getData();
			long message_id = update.getCallbackQuery().getMessage().getMessageId();
			long chat_id = update.getCallbackQuery().getMessage().getChatId();

			if (call_data.equals("/iota")) {
				Ticker tickerEUR = null;
				Ticker tickerUSD = null;

				try {
					tickerEUR = getIotaTickerInformation("https://api.cryptonator.com/api/ticker/iot-Eur");
					tickerUSD = getIotaTickerInformation("https://api.cryptonator.com/api/ticker/iot-usd");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				EditMessageText new_message = new EditMessageText()
						.setChatId(chat_id)
						.setMessageId(toIntExact(message_id))
						.setText("IOTA\n" + tickerEUR.getPrice() + tickerEUR.getTarget() + "   " + tickerEUR.getChange() + "% (1h) \n" + tickerUSD.getPrice() + tickerUSD.getTarget() + "   " + tickerUSD.getChange() + "% (1h)");
				try {
					execute(new_message); // Call method to send the message
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}

			if (call_data.equals("/donate")) {
				EditMessageText new_message = new EditMessageText()
						.setChatId(chat_id)
						.setMessageId(toIntExact(message_id))
						.setText("Thanks for your support!\n99IQJBLMDAVTJNKOVZSWOKVIXJCAZYLOVWBARSCZPCVZWRMKWAUPGVBAIHMKPXSBRCFNOZTFCIIBCGRVXEK9BSAPIY");

				try {
					execute(new_message); // Call method to send the message
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
		return "489957281:AAGruh5OUX2AFSFmoZjI4J7gBy29exrXT80";
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

