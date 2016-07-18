package com.example.home.gym_pal;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/18/2016
 */

@Database(version = GymDatabase.VERSION)
public final class GymDatabase {

    public static final int VERSION = 1;
    @Table(GymColumns.class) public static final String ATTENDANCE = "attendance";
}
