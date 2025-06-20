
package misClases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConBD {

   public static java.sql.Connection conectar() {
        try {
            String url = "jdbc:mysql://localhost:3306/adivinaquien";
            String user = "root";
            String password = "";
            java.sql.Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public static List<Datos> obtenerTodasLasPartidas() {
        List<Datos> lista = new ArrayList<>();

        String sql = "SELECT * FROM partidas";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Datos dat = new Datos(
                    rs.getString("jugador1"),
                    rs.getString("jugador2"),
                    rs.getString("ganador"),
                    rs.getString("personajeGanador"),
                    rs.getDate("fecha"),
                    rs.getString("duracion")
                );

                lista.add(dat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public static List<Datos> buscarPorJugador(String nombre) {
        List<Datos> lista = new ArrayList<>();
        String sql = "SELECT * FROM partidas WHERE jugador1 LIKE ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Datos dat = new Datos(
                    rs.getString("jugador1"),
                    rs.getString("jugador2"),
                    rs.getString("ganador"),
                    rs.getString("personajeGanador"),
                    rs.getDate("fecha"),
                    rs.getString("duracion")
                );

                lista.add(dat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public static List<Datos> obtenerPartidasOrdenadasPorDuracion() {
        List<Datos> lista = new ArrayList<>();
        String sql = "SELECT * FROM partidas ORDER BY TIME_TO_SEC(duracion) ASC";

        try {
            Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Datos dat = new Datos(
                    rs.getString("jugador1"),
                    rs.getString("jugador2"),
                    rs.getString("ganador"),
                    rs.getString("personajeGanador"),
                    rs.getDate("fecha"),
                    rs.getString("duracion")
                );
                lista.add(dat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
