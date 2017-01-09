package com.fpm.backend.data;

public enum Ata {

	AIRCONDITIONING("ATA21"), FUEL("ATA28"), PNEUMATICS("ATA36");
	
    private final String name;

    private Ata(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
