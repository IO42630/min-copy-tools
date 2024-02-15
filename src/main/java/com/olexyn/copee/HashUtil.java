package com.olexyn.copee;

import java.math.BigInteger;
import java.nio.channels.Channels;
import java.security.MessageDigest;

import com.olexyn.min.lock.CFile;
import com.olexyn.min.log.LogU;
import org.jetbrains.annotations.Nullable;

public final class HashUtil {

    private HashUtil() {
    }

    @Nullable
    public static String getHash(@Nullable CFile fcState) {
        if (fcState == null) {
            return null;
        }
        try (var is = Channels.newInputStream(fcState.getFc())) {
            var m = MessageDigest.getInstance("SHA256");
            byte[] buffer = new byte[262144];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                m.update(buffer, 0, bytesRead);
            }
            var i = new BigInteger(1, m.digest());
            return String.format("%1$032X", i);
        } catch (Exception e) {
            LogU.warnPlain("Failed to create Hash.\n%s", e.getMessage());
            return null;
        }
    }

}
