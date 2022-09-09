package org.appiansc.plugins.spt.functions.format;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

@SptPluginCategory
public class SPT_Fmt_BytesDisplaySize {
    private static final Logger LOG = Logger.getLogger(SPT_Fmt_BytesDisplaySize.class);

    // From https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java

    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    @Function
    public String spt_fmt_bytesdisplaysize(
            @Parameter Long bytes,
            @Parameter(required = false) Boolean binary
    ) {
        if (binary != null && binary)
            return humanReadableByteCountBin(bytes);
        else
            return humanReadableByteCountSI(bytes);
    }
}
