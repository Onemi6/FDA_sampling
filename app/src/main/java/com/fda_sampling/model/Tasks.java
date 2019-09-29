package com.fda_sampling.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    public static List<Task> list_task = new ArrayList<>();
    public static int position = -1, pos_copy = -1;

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

    public static void Task_copy(int c, int p) {

        Task task_p = list_task.get(p);
        Task task_new = (Task) list_task.get(c).clone();

        //将不需要复制的信息改回原来的
        task_new.setSTATE(task_p.getSTATE());
        task_new.setCHECK_INFO(task_p.getCHECK_INFO());
        task_new.setNO(task_p.getNO());
        task_new.setCUSTOM_NO(task_p.getCUSTOM_NO());
        task_new.setBUSINESS_SOURCE(task_p.getBUSINESS_SOURCE());
        task_new.setSAMPLE_TYPE(task_p.getSAMPLE_TYPE());
        task_new.setCHILD_FOOD_KIND_ID(task_p.getCHILD_FOOD_KIND_ID());
        task_new.setFOOD_KIND1(task_p.getFOOD_KIND1());
        task_new.setFOOD_KIND2(task_p.getFOOD_KIND2());
        task_new.setFOOD_KIND3(task_p.getFOOD_KIND3());
        task_new.setFOOD_KIND4(task_p.getFOOD_KIND4());
        task_new.setSAMPLING_NOTICE_CODE(task_p.getSAMPLING_NOTICE_CODE());
        task_new.setGOODS_TYPE(task_p.getGOODS_TYPE());
        task_new.setGOODS_NAME(task_p.getGOODS_NAME());

        //以下抽样单位信息
        task_new.setDRAW_ORG(task_p.getDRAW_ORG());
        task_new.setDRAW_ORG_ADDR(task_p.getDRAW_ORG_ADDR());
        task_new.setDRAW_PERSON(task_p.getDRAW_PERSON());
        task_new.setDRAW_PHONE(task_p.getDRAW_PHONE());
        task_new.setDRAW_FAX(task_p.getDRAW_FAX());
        task_new.setDRAW_ZIPCODE(task_p.getDRAW_ZIPCODE());
        task_new.setDRAW_DATE(task_p.getDRAW_DATE());
        task_new.setDRAW_MAN(task_p.getDRAW_MAN());
        task_new.setREMARK(task_p.getREMARK());

        //将新的对象set到list里
        list_task.set(p, task_new);
    }
}
