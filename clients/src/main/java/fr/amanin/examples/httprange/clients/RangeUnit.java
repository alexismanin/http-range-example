package fr.amanin.examples.httprange.clients;

public enum RangeUnit {
    none,
    bytes,
    unsupported;

    public static RangeUnit parse(String acceptRange) {
        if (acceptRange == null || (acceptRange = acceptRange.trim()).isEmpty()) return none;
        try {
            return RangeUnit.valueOf(acceptRange);
        } catch (IllegalArgumentException e) {
            return unsupported;
        }
    }
}
