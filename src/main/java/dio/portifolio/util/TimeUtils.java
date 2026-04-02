package dio.portifolio.util;

public class TimeUtils {

    public static String formatarMinutos(long minutos) {

        String sinal = minutos >= 0 ? "+" : "-";

        long abs = Math.abs(minutos);

        long horas = abs / 60;
        long mins = abs % 60;

        return String.format("%s%02d:%02d", sinal, horas, mins);
    }
}