package dominio;

public enum TramoHora {
    MAÑANA_1("08:00 - 10:00"),
    MAÑANA_2("10:00 - 12:00"),
    TARDE_1("12:00 - 14:00"),
    TARDE_2("14:00 - 16:00"),
    TARDE_3("16:00 - 18:00");

    private final String range;

    TramoHora(String range) {
        this.range = range;
    }

    public String getRange() {
        return this.range;
    }

    @Override
    public String toString() {
        return range;
    }
}
