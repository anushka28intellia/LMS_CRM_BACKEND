package com.its.crm.records.record;

/**
 * The Hot flag (brief §3.8.2): a user sets it, it moves the record to the top of lists, and it clears itself after
 * the "Hot flag clears after (days)" business setting ({@code hotExpiryDays}, default 30) unless a user renews it.
 * <p>
 * The flag is stored as the time it was set ({@code hot_since}). Rather than a job that rewrites rows, a flag older
 * than the setting simply reads as not Hot everywhere, so a change to the setting applies at once (brief §2.2) and
 * setting Hot again renews it from now.
 */
public final class HotFlag {

    /** Days a Hot flag lasts when the setting is missing. */
    public static final int DEFAULT_DAYS = 30;

    private HotFlag() {
    }

    /** SQL condition: the timestamp column holds a Hot flag that has not yet expired. */
    public static String isHot(String column) {
        return "(" + column + " IS NOT NULL AND " + column + " > now() - COALESCE((SELECT (s.setting_value #>> '{}')::int "
                + "FROM app_setting s WHERE s.setting_key = 'hotExpiryDays'), " + DEFAULT_DAYS + ") * interval '1 day')";
    }
}
