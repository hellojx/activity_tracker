package com.svi.activitytracker.common;

import java.util.ArrayList;

public class InMemoryLog {

    private static InMemoryLog instance = null;

    public static InMemoryLog getInstance() {
        if (instance == null) {
            instance = new InMemoryLog();
        }
        return instance;
    }

    private InMemoryLog() {}

    public static class Item {
        private int tag;

        public String getMsg() {
            return msg;
        }

        public int getTag() {
            return tag;
        }

        private String msg;

        public Item(int tag, String msg) {
            this.tag = tag;
            this.msg = msg;
        }
    }

    private ArrayList<Item> data = new ArrayList<>();

    public void add(int tag, String msg) {
        data.add(new Item(tag, msg));
    }

    public void addAll(int tag, ArrayList<String> data) {
        for (String msg:data) {
            this.data.add(new Item(tag, msg));
        }
    }

    public void clear(int tag) {
        ArrayList<Item> toRemove = new ArrayList<>();
        for (Item item:data) {
            if (item.getTag() == tag) {
                toRemove.add(item);
            }
        }

        data.removeAll(toRemove);
    }

    public ArrayList<String> filter(final int tag) {
        ArrayList<String> result = new ArrayList<>();
        for (Item item:data) {
            if (item.getTag() == tag) {
                result.add(item.getMsg());
            }
        }
        return result;
    }
}
