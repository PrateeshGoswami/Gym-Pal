package com.example.home.gym_pal;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 7/18/2016
 */
public interface GymColumns {
    @DataType(DataType.Type.INTEGER)@PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(DataType.Type.INTEGER)
    String COUNT = "count";

    @DataType(DataType.Type.INTEGER)
    String DATETIME = "datetime";
}
