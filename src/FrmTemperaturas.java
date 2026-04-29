import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import controladores.TemperaturaCiudadControlador;

import datechooser.beans.DateChooserCombo;
import modelos.TemperaturaCiudad;
import servicios.TemperaturaCiudadServicio;

public class FrmTemperaturas extends JFrame {

    private DateChooserCombo dccDesde, dccHasta;
    private DateChooserCombo dccFechaEspecifica;
    private JTabbedPane tpTemperaturas;
    private JPanel pnlGrafica;
    private JPanel pnlExtremos;
    private JButton btnCargarDatos;
    private JButton btnGraficar;
    private JButton btnExtremos;

    private List<TemperaturaCiudad> datos;

    public static void main(String[] args) throws Exception {
        new FrmTemperaturas().setVisible(true);
    }

    public FrmTemperaturas() {

        setTitle("Temperaturas por Ciudad");
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tb = new JToolBar();

        btnCargarDatos = new JButton();
        btnCargarDatos.setIcon(new ImageIcon(getClass().getResource("/iconos/CargarDatos.png")));
        btnCargarDatos.setToolTipText("Cargar datos de temperaturas");
        btnCargarDatos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCargarDatosClick();
            }
        });
        tb.add(btnCargarDatos);

        btnGraficar = new JButton();
        btnGraficar.setIcon(new ImageIcon(getClass().getResource("/iconos/Grafica.png")));
        btnGraficar.setToolTipText("Graficar promedio de temperaturas por ciudad");
        btnGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnGraficarClick();
            }
        });
        tb.add(btnGraficar);

        btnExtremos = new JButton();
        btnExtremos.setIcon(new ImageIcon(getClass().getResource("/iconos/Temperatura.png")));
        btnExtremos.setToolTipText("Mostrar ciudad mas y menos calurosa");
        btnExtremos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnExtremosClick();
            }
        });
        tb.add(btnExtremos);

        JPanel pnlTemperaturas = new JPanel();
        pnlTemperaturas.setLayout(new BoxLayout(pnlTemperaturas, BoxLayout.Y_AXIS));

        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setPreferredSize(new Dimension(pnlDatosProceso.getWidth(), 80));
        pnlDatosProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        pnlDatosProceso.setLayout(null);

        JLabel lblDesde = new JLabel("Fecha desde");
        lblDesde.setBounds(10, 10, 100, 25);
        pnlDatosProceso.add(lblDesde);

        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(110, 10, 120, 25);
        pnlDatosProceso.add(dccDesde);

        JLabel lblHasta = new JLabel("Fecha hasta");
        lblHasta.setBounds(250, 10, 100, 25);
        pnlDatosProceso.add(lblHasta);

        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(350, 10, 120, 25);
        pnlDatosProceso.add(dccHasta);

        JLabel lblFechaEspecifica = new JLabel("Fecha especifica");
        lblFechaEspecifica.setBounds(10, 45, 100, 25);
        pnlDatosProceso.add(lblFechaEspecifica);

        dccFechaEspecifica = new DateChooserCombo();
        dccFechaEspecifica.setBounds(110, 45, 120, 25);
        pnlDatosProceso.add(dccFechaEspecifica);

        pnlGrafica = new JPanel();
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);

        pnlExtremos = new JPanel();

        tpTemperaturas = new JTabbedPane();
        tpTemperaturas.addTab("Grafica de Promedios", spGrafica);
        tpTemperaturas.addTab("Ciudad mas/menos calurosa", pnlExtremos);

        pnlTemperaturas.add(pnlDatosProceso);
        pnlTemperaturas.add(tpTemperaturas);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlTemperaturas, BorderLayout.CENTER);

        btnGraficar.setEnabled(false);
        btnExtremos.setEnabled(false);
    }

    private void btnCargarDatosClick() {
        JFileChooser selector = new JFileChooser();
        selector.setCurrentDirectory(new File(System.getProperty("user.dir")));
        selector.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));

        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            datos = TemperaturaCiudadServicio.getDatos(selector.getSelectedFile().getAbsolutePath());

            btnGraficar.setEnabled(!datos.isEmpty());
            btnExtremos.setEnabled(!datos.isEmpty());

            if (datos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No fue posible cargar datos desde el archivo seleccionado.");
            }
        }
    }

    private void btnGraficarClick() {
        if (datos != null && !datos.isEmpty()) {
            LocalDate desde = dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate hasta = dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            TemperaturaCiudadControlador.graficarPromedios(pnlGrafica, datos, desde, hasta);
            tpTemperaturas.setSelectedIndex(0);
        }
    }

    private void btnExtremosClick() {
        if (datos != null && !datos.isEmpty()) {
            LocalDate fecha = dccFechaEspecifica.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            TemperaturaCiudadControlador.mostrarExtremos(pnlExtremos, datos, fecha);
            tpTemperaturas.setSelectedIndex(1);
        }
    }

}
