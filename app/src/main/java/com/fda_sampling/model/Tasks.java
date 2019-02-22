package com.fda_sampling.model;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    public static List<Task> list_task = new ArrayList<>();
    public static int position = -1;

    public static void init() {
        for (int i = 0; i < list_task.size(); i++) {
            list_task.get(i).setIsSelect(0);
        }
    }

    public static void select_all() {
        for (int i = 0; i < list_task.size(); i++) {
            list_task.get(i).setIsSelect(1);
        }
    }

    public static void select_clear() {
        for (int i = 0; i < list_task.size(); i++) {
            list_task.get(i).setIsSelect(0);
        }
    }

    public static void select_invert() {
        for (int i = 0; i < list_task.size(); i++) {
            if (list_task.get(i).getIsSelect() == 0) {
                list_task.get(i).setIsSelect(1);
            } else if (list_task.get(i).getIsSelect() == 1) {
                list_task.get(i).setIsSelect(0);
            }
        }
    }
}
