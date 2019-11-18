package com.osome.stickydecorator

open class Item(val value: Int)
class SectionItem(value: Int) : Item(value)

fun generateItem(count: Int): List<Item> {
    return (0 until count).map { Item(it + 1) }
}

fun generateItemWithSection(count: Int): List<Item> {
    val list = mutableListOf<Item>()
    (0 until count).forEach { value ->
        if ((value + 1) % 10 == 0 || value == 0)
            list += SectionItem(((value + 1) / 10) * 10)
        list += Item(value + 1)
    }
    return list
}

fun generateItemWithSectionReverse(count: Int): List<Item> {
    val list = mutableListOf<Item>()
    (0 until count).forEach { value ->
        if (value != 0 && ((value + 1) % 10 == 0) && value != count - 1)
            list += SectionItem((((value) / 10) * 10))
        list += Item(value + 1)

        if (value == count - 1)
            list += SectionItem((((value) / 10) * 10))
    }
    return list
}