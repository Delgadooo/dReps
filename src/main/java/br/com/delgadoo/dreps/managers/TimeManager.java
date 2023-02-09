package br.com.delgadoo.dreps.managers;

public class TimeManager {

    public static String getTime(long time) {
        long variacao = time;
        long vsegundos = variacao / 1000L % 60L;
        long vminutos = variacao / 60000L % 60L;
        long vhoras = variacao / 3600000L % 24L;
        long vdias = variacao / 86400000L % 7L;

        String segundos = String.valueOf(vsegundos).replaceAll("-", "");
        String minutos = String.valueOf(vminutos).replaceAll("-", "");
        String horas = String.valueOf(vhoras).replaceAll("-", "");
        String dias = String.valueOf(vdias).replaceAll("-", "");
        if (dias.equals("0") && horas.equals("0") && minutos.equals("0")) {
            return "" + segundos + "s";
        }
        if (dias.equals("0") && horas.equals("0")) {
            return "" + minutos + "m " + segundos + "s";
        }
        if (dias.equals("0")) {
            return "" + horas + "h " + minutos + "m " + segundos + "s";
        }
        return "" + dias + "d " + horas + "h " + minutos + "m " + segundos + "s ";
    }

}
