package servicios;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import modelos.TemperaturaCiudad;

public class TemperaturaCiudadServicio {

    public static List<TemperaturaCiudad> getDatos(String archivo) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("d/M/yyyy");

        try {
            var lineas = Files.lines(Paths.get(archivo));

            return lineas.skip(1)
                    .map(linea -> linea.split(","))
                    .map(textos -> new TemperaturaCiudad(textos[0],
                            LocalDate.parse(textos[1], formatoFecha),
                            Double.parseDouble(textos[2])))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<String> getCiudades(List<TemperaturaCiudad> temperaturas) {
        return temperaturas.stream()
                .map(TemperaturaCiudad::getCiudad)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public static List<TemperaturaCiudad> filtrarPorRango(List<TemperaturaCiudad> temperaturas, LocalDate desde, LocalDate hasta) {
        return temperaturas.stream()
                .filter(temperatura -> !temperatura.getFecha().isBefore(desde) &&
                        !temperatura.getFecha().isAfter(hasta))
                .collect(Collectors.toList());
    }

    public static Map<String, Double> getPromediosPorCiudad(List<TemperaturaCiudad> temperaturas) {
        return temperaturas.stream()
                .collect(Collectors.groupingBy(TemperaturaCiudad::getCiudad,
                        Collectors.averagingDouble(TemperaturaCiudad::getTemperatura)));
    }

    public static List<TemperaturaCiudad> filtrarPorFecha(List<TemperaturaCiudad> temperaturas, LocalDate fecha) {
        return temperaturas.stream()
                .filter(temperatura -> temperatura.getFecha().isEqual(fecha))
                .collect(Collectors.toList());
    }

    public static String getCiudadMasCalurosa(List<TemperaturaCiudad> temperaturas) {
        return temperaturas.stream()
                .max(Comparator.comparingDouble(TemperaturaCiudad::getTemperatura))
                .map(TemperaturaCiudad::getCiudad)
                .orElse("");
    }

    public static String getCiudadMenosCalurosa(List<TemperaturaCiudad> temperaturas) {
        return temperaturas.stream()
                .min(Comparator.comparingDouble(TemperaturaCiudad::getTemperatura))
                .map(TemperaturaCiudad::getCiudad)
                .orElse("");
    }

}
