package com.pop.enume;

import com.pop.R;

import java.io.Serializable;

/**
 * Created by xugang on 16/7/21.
 */
public enum PopModelLayoutEnume implements Serializable{
    COLOR(1, R.id.color_pop_layout,"默认"),
    BLUE(2,R.id.blue_pop_layout,"梦幻蓝"),
    GREEN(3, R.id.green_pop_layout,"清新绿"),
    LIGHTBLUE(4,R.id.light_blue_pop_layout,"明亮蓝"),
    PINK(5,R.id.pink_pop_layout,"诱惑粉"),
    PURPLE(6,R.id.purple_pop_layout,"高贵紫"),
    REDYELLOW(7,R.id.rdylw_pop_layout,"迷醉红"),
    YELLOW(8,R.id.yellow_pop_layout,"淡雅黄"),
    DARKGREENOLD(9,R.id.dark_green_old_pop_layout,"迷幻蓝"),
    GREENOLD(10,R.id.green_old_pop_layout,"翡翠绿"),
    PURPLEOLD(11,R.id.purple_old_pop_layout,"深沉紫"),
    REDOLD(12,R.id.red_old_pop_layout,"烈焰红"),
    ;
    private int type;
    private int layout;
    private String name;
    private PopModelLayoutEnume(int type, int layout,String name){
        this.type = type;
        this.layout = layout;
        this.name = name;
    }

    public static int getLayout(int type){
        for (PopModelLayoutEnume c : PopModelLayoutEnume.values()) {
            if (c.type == type) {
                return c.layout;
            }
        }
        return COLOR.layout;
    }

    public static PopModelLayoutEnume getEnum(int layout){
        for (PopModelLayoutEnume c : PopModelLayoutEnume.values()) {
            if (c.layout == layout) {
                return c;
            }
        }
        return COLOR;
    }


    public int getType() {
        return type;
    }

    public int getLayout() {
        return layout;
    }

    public String getName() {
        return name;
    }
}
