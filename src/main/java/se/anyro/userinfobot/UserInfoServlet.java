package se.anyro.userinfobot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class UserInfoServlet extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "probashi_info_bot";
    }

    @Override
    public String getBotToken() {
        return BuildVars.TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Your ID: " + chatId);

            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {

        TelegramBotsApi botsApi =
                new TelegramBotsApi(DefaultBotSession.class);

        botsApi.registerBot(new UserInfoServlet());
    }
}
