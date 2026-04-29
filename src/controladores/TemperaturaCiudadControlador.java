package controladores;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import modelos.TemperaturaCiudad;
import servicios.TemperaturaCiudadServicio;

public class TemperaturaCiudadControlador {

    public static void graficarPromedios(JPanel pnlGrafica,
            List<TemperaturaCiudad> temperaturas,
            LocalDate desde, LocalDate hasta) {
        var datosFiltrados = TemperaturaCiudadServicio.filtrarPorRango(temperaturas, desde, hasta);
        var promedios = TemperaturaCiudadServicio.getPromediosPorCiudad(datosFiltrados);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        promedios.forEach((ciudad, promedio) -> dataset.addValue(promedio, "Temperatura", ciudad));

        JFreeChart graficador = ChartFactory.createBarChart(
                "Grafica de promedio de temperatura por ciudad",
                "Ciudad",
                "Temperatura",
                dataset);

        ChartPanel pnlGraficador = new ChartPanel(graficador);
        pnlGraficador.setPreferredSize(new Dimension(500, 300));

        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(pnlGraficador, BorderLayout.CENTER);
        pnlGrafica.revalidate();
    }

    public static void mostrarExtremos(JPanel pnlExtremos,
            List<TemperaturaCiudad> temperaturas,
            LocalDate fecha) {
        var datosFiltrados = TemperaturaCiudadServicio.filtrarPorFecha(temperaturas, fecha);
        var ciudadMasCalurosa = TemperaturaCiudadServicio.getCiudadMasCalurosa(datosFiltrados);
        var ciudadMenosCalurosa = TemperaturaCiudadServicio.getCiudadMenosCalurosa(datosFiltrados);

        pnlExtremos.removeAll();

        var gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        pnlExtremos.add(new JLabel("Ciudad mas calurosa"), gbc);
        gbc.gridx = 1;
        pnlExtremos.add(new JLabel(ciudadMasCalurosa), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        pnlExtremos.add(new JLabel("Ciudad menos calurosa"), gbc);
        gbc.gridx = 1;
        pnlExtremos.add(new JLabel(ciudadMenosCalurosa), gbc);

        pnlExtremos.revalidate();
    }

}
