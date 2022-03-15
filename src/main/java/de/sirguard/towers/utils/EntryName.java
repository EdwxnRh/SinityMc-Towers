package de.sirguard.towers.utils;

public enum EntryName {
    ENTRY_0(0, "§a"),
    ENTRY_1(1, "§a§a"),
    ENTRY_2(2, "§a§a§a"),
    ENTRY_3(3, "§a§a§a§a"),
    ENTRY_4(4, "§a§a§a§a§a"),
    ENTRY_5(5, "§a§a§a§a§a§a"),
    ENTRY_6(6, "§a§a§a§a§a§a§a"),
    ENTRY_7(7, "§a§a§a§a§a§a§a§a"),
    ENTRY_8(8, "§b"),
    ENTRY_9(9, "§b§b"),
    ENTRY_10(10, "§b§b§b"),
    ENTRY_11(11, "§b§b§b§b"),
    ENTRY_12(12, "§b§b§b§b§b"),
    ENTRY_13(13, "§b§b§b§b§b§b"),
    ENTRY_14(14, "§b§b§b§b§b§b§b"),
    ENTRY_15(15, "§b§b§b§b§b§b§b§b");

    private final int entry;

    private final String entryName;

    EntryName(int entry, String entryName) {
        this.entry = entry;
        this.entryName = entryName;
    }

    public int getEntry() {
        return this.entry;
    }

    public String getEntryName() {
        return this.entryName;
    }
}
