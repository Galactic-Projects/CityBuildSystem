package net.galacticprojects.spigot.boost;

public enum GalacticBoostType {

    MINING_I(5, 1), MINING_II(10, 2), MINING_III(15, 3), FLY_I(5, 1), FLY_II(10, 1), XP_I(5, 1)
    , XP_II(10, 2), XP_III(15, 3), JUMP_I(5, 1), JUMP_II(10, 2), NIGHT_VISION_I(5, 1),
    NIGHT_VISION_II(10, 2), NIGHT_VISION_III(15, 3), STRENGTH_I(5, 1), STRENGTH_II(10, 2), STRENGTH_III(15, 3),
    REDUCE_I(5, 10), REDUCE_II(10, 15), REDUCE_III(20, 20),
    GLOBAL_MINING_I(10, 1), GLOBAL_MINING_II(20, 2), GLOBAL_MINING_III(30, 3),
    GLOBAL_FLY_I(10, 1), GLOBAL_FLY_II(20, 1), GLOBAL_XP_I(10, 1), GLOBAL_XP_II(20, 2), GLOBAL_XP_III(30, 3),
    GLOBAL_JUMP_I(10, 1), GLOBAL_JUMP_II(20, 1), GLOBAL_NIGHT_VISION_I(10, 1), GLOBAL_NIGHT_VISION_II(20, 2)
    , GLOBAL_NIGHT_VISION_III(30, 3), GLOBAL_STRENGTH_I(10, 1), GLOBAL_STRENGTH_II(20, 2), GLOBAL_STRENGTH_III(30, 3),
    GLOBAL_REDUCE_I(10, 10), GLOBAL_REDUCE_II(20, 15), GLOBAL_REDUCE_III(30, 20), GLOBAL_DISCOUNT_I(60, 40),
    GLOBAL_DISCOUNT_II(120, 50)
    ;

    int count;
    int level;

    private GalacticBoostType(int count, int level) {
        this.count = count;
        this.level = level;
    }

}
