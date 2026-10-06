package minurl.domain.util;

public final class Base62 {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    private Base62() {}

    public static String encode(long number) {
        if (number <= 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % BASE);
            sb.append(ALPHABET.charAt(remainder));
            number /= BASE;
        }

        return sb.reverse().toString();
    }
}
