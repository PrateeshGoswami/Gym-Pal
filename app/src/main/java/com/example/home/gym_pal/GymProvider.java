package com.example.home.gym_pal;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/18/2016
 */

@ContentProvider(authority = GymProvider.AUTHORITY, database = GymDatabase.class)
public final class GymProvider {

    public static final String AUTHORITY = "com.example.home.gym_pal.GymProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String ATTENDANCE = "attendance";
    }

    private static Uri buildUri(String...paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = GymDatabase.ATTENDANCE) public static class Attendance{
        @ContentUri(
                path = "attendance",
                type = "vnd.android.cursor.dir/attendance",
                defaultSort = GymColumns._ID + " ASC")
//        public static final Uri ATTENDANCE = Uri.parse("content://" + AUTHORITY + "/attendance");
        public static final Uri CONTENT_URI = buildUri(Path.ATTENDANCE);
    }
}
