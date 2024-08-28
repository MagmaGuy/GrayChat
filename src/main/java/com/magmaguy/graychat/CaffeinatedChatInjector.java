package com.magmaguy.graychat;

import co.casterlabs.caffeinated.pluginsdk.Caffeinated;
import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPlugin;
import co.casterlabs.caffeinated.pluginsdk.CaffeinatedPluginImplementation;
import co.casterlabs.koi.api.types.events.RichMessageEvent;
import co.casterlabs.koi.api.types.events.rich.ChatFragment;
import co.casterlabs.koi.api.types.events.rich.TextFragment;
import co.casterlabs.koi.api.types.user.SimpleProfile;
import co.casterlabs.koi.api.types.user.User;
import co.casterlabs.koi.api.types.user.UserPlatform;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@CaffeinatedPluginImplementation
public class CaffeinatedChatInjector extends CaffeinatedPlugin {

    @Override
    public void onInit() {
        FastLogger.logStatic("Starting Gray Chat Injector");
        try {
            StartServer.start();
        } catch (IOException e) {
//            FastLogger.logStatic("FAILED TO START CHAT INJECTOR SERVER");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose() {
//        this.getLogger().info("Goodbye World!");
        StartServer.shutdown();
    }

    @Override
    public String getName() {
        return "Gray Chat Injector";
    }

    @Override
    public String getId() {
        return "com.magmaguy.graychat";
    }

    private static final SimpleProfile CUSTOM_STREAMER = new SimpleProfile("youtube", "youtube", UserPlatform.CUSTOM_INTEGRATION);

    public static void sendMessage(String message, String username){
        String userLink = "https://...";
        String profilePictureUrl = "...";

        User sender = new User(
                username, username, UserPlatform.CUSTOM_INTEGRATION,
                Collections.emptyList(), Collections.emptyList(),
                "#ff0000", username, username, "", userLink, profilePictureUrl ,
                0, 0
        );

        ChatFragment messageFragment = new TextFragment(message);
        RichMessageEvent rich = new RichMessageEvent(
                CUSTOM_STREAMER, sender,
                Arrays.asList(messageFragment ), Collections.emptyList(), Collections.emptyList(),
                UUID.randomUUID().toString(), null
        );


        Caffeinated.getInstance().getKoi().broadcastEvent(rich);

//        FastLogger.logStatic("Sent message " + message);
    }

}
