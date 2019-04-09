package ru.basa62.wst.lab7;

import lombok.Value;

@Value
public class Pair<L, R> {
    private final L left;
    private final R right;

}
