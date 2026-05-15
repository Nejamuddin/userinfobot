package se.anyro.userinfobot;

import com.sun.net.httpserver.HttpServer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.InetSocketAddress;

public class UserInfoServlet extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (!update.hasMessage()) return;

            Message message = update.getMessage();
            long chatId = message.getChatId();

            String text;

            if (message.getForwardFrom() != null) {
                text =
                    "Forwarded User Info:\n\n" +
                    "ID: " + message.getForwardFrom().getId() + "\n" +
                    "First Name: " + message.getForwardFrom().getFirstName() + "\n" +
                    "Username: @" + message.getForwardFrom().getUserName();
            } else {
                text =
                    "Welcome to UserInfo Bot ✅\n\n" +
                    "Your Info:\n" +
                    "ID: " + message.getFrom().getId() + "\n" +
                    "First Name: " + message.getFrom().getFirstName() + "\n" +
                    "Username: @" + message.getFrom().getUserName() + "\n\n" +
                    "Forward any message to me, I will try to show that user's ID.";
            }

            SendMessage msg = new SendMessage();
            msg.setChatId(String.valueOf(chatId));
            msg.setText(text);
            execute(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String portText = System.getenv().getOrDefault("PORT", "10000");
        int port = Integer.parseInt(portText);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", exchange -> {
            String response = "Bot is running";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });
        server.start();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new UserInfoServlet());

        System.out.println("Bot started on port " + port);
    }
}
