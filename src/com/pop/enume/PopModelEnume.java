package com.pop.enume;

import com.pop.R;

/**
 * Created by xugang on 16/7/21.
 */
public enum  PopModelEnume {
    COLOR(1, R.drawable.color_pop),
    BLUE(2,R.drawable.blue_pop),
    GREEN(3, R.drawable.green_pop),
    LIGHTBLUE(4,R.drawable.light_blue_pop),
    PINK(5,R.drawable.pink_pop),
    PURPLE(6,R.drawable.purple_pop),
    REDYELLOW(7,R.drawable.rdylw_pop),
    YELLOW(8,R.drawable.yellow_pop),
    DARKGREENOLD(9,R.drawable.dark_green_old_pop),
    GREENOLD(10,R.drawable.green_old_pop),
    PURPLEOLD(11,R.drawable.purple_old_pop),
    REDOLD(12,R.drawable.red_old_pop),
    ;
    private int type;
    private int img;
    private PopModelEnume(int type,int img){
        this.type = type;
        this.img = img;
    }

    public static int getImg(int type){
        for (PopModelEnume c : PopModelEnume.values()) {
            if (c.type == type) {
                return c.img;
            }
        }
        return COLOR.img;
    }





}
