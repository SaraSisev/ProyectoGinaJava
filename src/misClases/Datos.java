
package misClases;

import java.util.Date;

public class Datos {
    private String jugador1;
    private String jugador2;
    private String ganador;
    private String personajeGanador;
    private Date fecha;
    private String duracion;

    public Datos(String jugador1, String jugador2, String ganador, String personajeGanador, Date fecha, String duracion) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.ganador = ganador;
        this.personajeGanador = personajeGanador;
        this.fecha = fecha;
        this.duracion = duracion;
    }

    public String getJugador1() {
        return jugador1;
    }

    public void setJugador1(String jugador1) {
        this.jugador1 = jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public void setJugador2(String jugador2) {
        this.jugador2 = jugador2;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public String getPersonajeGanador() {
        return personajeGanador;
    }

    public void setPersonajeGanador(String personajeGanador) {
        this.personajeGanador = personajeGanador;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    
}
