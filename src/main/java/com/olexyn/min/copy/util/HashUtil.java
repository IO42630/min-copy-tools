package com.olexyn.min.copy.util;

import com.olexyn.min.lock.FcState;
import com.olexyn.min.log.LogU;
import lombok.experimental.UtilityClass;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.math.BigInteger;
import java.nio.channels.Channels;
import java.security.MessageDigest;

@UtilityClass
public class HashUtil {





    public static @Nullable String getHash(FcState fcState) {
        if (fcState.isUnlocked()) {
            LogU.warnPlain("Will not hash a file that is not locked.");
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
