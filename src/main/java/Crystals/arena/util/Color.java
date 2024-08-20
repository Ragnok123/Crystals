package Crystals.arena.util;

public final class Color {
      private static final int BIT_MASK = 255;
      public static final int WHITE = 16777215;
      public static final int SILVER = 12632256;
      public static final int GRAY = 8421504;
      public static final int BLACK = 0;
      public static final int RED = 16711680;
      public static final int MAROON = 8388608;
      public static final int YELLOW = 16776960;
      public static final int OLIVE = 8421376;
      public static final int LIME = 65280;
      public static final int GREEN = 32768;
      public static final int AQUA = 65535;
      public static final int TEAL = 32896;
      public static final int BLUE = 255;
      public static final int NAVY = 128;
      public static final int FUCHSIA = 16711935;
      public static final int PURPLE = 8388736;
      public static final int ORANGE = 16753920;

      public static int toDecimal(int bgr) {
            int r = bgr >> 16 & 255;
            int g = bgr >> 8 & 255;
            int b = bgr >> 0 & 255;
            return hex2decimal(toHex(r, g, b));
      }

      private static String toHex(int r, int g, int b) {
            return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
      }

      private static String toBrowserHexValue(int number) {
            StringBuilder builder = new StringBuilder(Integer.toHexString(number & 255));

            while(builder.length() < 2) {
                  builder.append("0");
            }

            return builder.toString().toUpperCase();
      }

      private static int hex2decimal(String s) {
            String digits = "0123456789ABCDEF";
            s = s.toUpperCase();
            int val = 0;

            for(int i = 0; i < s.length(); ++i) {
                  char c = s.charAt(i);
                  int d = digits.indexOf(c);
                  val = 16 * val + d;
            }

            return val;
      }
}
