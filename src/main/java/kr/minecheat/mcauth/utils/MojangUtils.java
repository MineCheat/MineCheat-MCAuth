package kr.minecheat.mcauth.utils;

import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.exception.AuthenticationException;
import kr.minecheat.mcauth.mcdata.MojangUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MojangUtils {
    public static CompletableFuture<MojangUser> checkValidity(String username, String hash) {
        CompletableFuture<MojangUser> completableFuture = new CompletableFuture<>();

        Server.getThreadPool().submit(() -> {
            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/hasJoined?username="+username+"&serverId="+hash);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setDoInput(true);
                huc.setConnectTimeout(10000);
                huc.setReadTimeout(10000);
                huc.setRequestMethod("GET");

                if (huc.getResponseCode() == 204) {
                    completableFuture.completeExceptionally(new AuthenticationException("Can not authenticate the user"));
                } else if (huc.getResponseCode() != 200) {
                    completableFuture.completeExceptionally(new AuthenticationException("Weird error"));
                } else {
                    MojangUser mojangUser = Server.getMapper().readValue(huc.getInputStream(), MojangUser.class);
                    completableFuture.complete(mojangUser);
                }
            } catch (java.net.SocketTimeoutException e) {
                completableFuture.completeExceptionally(new AuthenticationException("Auth server is down"));
            } catch (IOException e) {
                completableFuture.completeExceptionally(e);
                e.printStackTrace();
            }
        });

        return completableFuture;
    }

    public static UUID mojangUIDtoJavaUID(String mojangUID) {
        String s1= mojangUID.substring(0, 8);
        String s2= mojangUID.substring(8, 12);
        String s3= mojangUID.substring(12, 16);
        String s4= mojangUID.substring(16, 20);
        String s5= mojangUID.substring(20, 32);
        return UUID.fromString(s1 + "-" + s2 + "-" + s3 +"-"+ s4 +"-" + s5);
    }
}
