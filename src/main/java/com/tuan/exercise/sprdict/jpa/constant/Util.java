package com.tuan.exercise.sprdict.jpa.constant;

import org.springframework.beans.factory.annotation.Value;

public class Util {
    private Util() {
    }

    public static class Paging {
        private Paging() {
        }

        @Value("${paging.item.count}")
        public static int wordItemCount;

        @Value("${paging.pagination.item.count}")
        public static int navigationItemCount;
    }
}
