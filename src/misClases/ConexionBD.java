
package misClases;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ConexionBD {
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
   
    
    public static List<Datos> obtenerTodasLasPeliculas() {
        List<Datos> lista = new ArrayList<>();

        String sql = "SELECT * FROM partidas";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Datos peli = new Datos(
                    rs.getString("nombre"),
                    rs.getInt("duracion"),
                    rs.getString("fecha_estreno"),
                    rs.getString("actor_principal")
                );

                lista.add(peli);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Consultar películas por nombre (parcial o exacto)
    public static List<Peliculas> buscarPorNombre(String nombre) {
        List<Peliculas> lista = new ArrayList<>();
        String sql = "SELECT * FROM peliculas WHERE nombre LIKE ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Peliculas peli = new Peliculas(
                    rs.getString("nombre"),
                    rs.getInt("duracion"),
                    rs.getString("fecha_estreno"),
                    rs.getString("actor_principal")
                );

                lista.add(peli);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public static Peliculas buscarPorId(int id) {
        String sql = "SELECT * FROM peliculas WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Peliculas peli = new Peliculas(
                    rs.getString("nombre"),
                    rs.getInt("duracion"),
                    rs.getString("fecha_estreno"),
                    rs.getString("actor_principal")
                );
                return peli;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // No encontrada
    }

    public static boolean eliminarPorId(int id) {
        String sql = "DELETE FROM peliculas WHERE id = ?";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void eliminarConConfirmacion(int id) {
        Peliculas peli = buscarPorId(id);

        if (peli == null) {
            System.out.println("No se encontró ninguna película con ID " + id);
            return;
        }

        System.out.println("Deseas eliminar esta película?");
        System.out.println("Nombre: " + peli.getNombre());
        System.out.println("Duracion: " + peli.getDuracion() + " min");
        System.out.println("Fecha de Estreno: " + peli.getFechaEstreno());
        System.out.println("Actor Principal: " + peli.getActorPrincipal());

        System.out.print("Escribe 'S' para confirmar: ");
        Scanner sc = new Scanner(System.in);
        String opcion = sc.nextLine();

        if (opcion.equalsIgnoreCase("S")) {
            if (eliminarPorId(id)) {
                System.out.println("Pelicula eliminada con exito.");
            } else {
                System.out.println("Error al eliminar la pelicula.");
            }
        } else {
            System.out.println("Operacion cancelada.");
        }
    }
    
    public static boolean actualizarPelicula(Peliculas peli, int id) {
        String sql = "UPDATE peliculas SET nombre = ?, duracion = ?, fecha_estreno = ?, actor_principal = ? WHERE id = ?";

        try (Connection conn = conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, peli.getNombre());
            stmt.setInt(2, peli.getDuracion());
            stmt.setString(3, peli.getFechaEstreno());
            stmt.setString(4, peli.getActorPrincipal());
            stmt.setInt(5, id); // Estoy usando el parámetro id porque la clase no tiene campo id

            int filas = stmt.executeUpdate();
            return filas > 0; // true si se actualizó al menos una fila

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertarPelicula(Peliculas peli) {
        String sql = "INSERT INTO peliculas (nombre, duracion, fecha_estreno, actor_principal) VALUES (?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, peli.getNombre());
            stmt.setInt(2, peli.getDuracion());
            stmt.setString(3, peli.getFechaEstreno());
            stmt.setString(4, peli.getActorPrincipal());

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
