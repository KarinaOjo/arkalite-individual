package pa.gob.dntic.servicionotificaciones.adaptadores.entrada;

import org.springframework.stereotype.Component;
import pa.gob.dntic.servicionotificaciones.eventos.SolicitudAprobada;
import pa.gob.dntic.servicionotificaciones.eventos.SolicitudEnviada;
import pa.gob.dntic.servicionotificaciones.dominio.ServicioDeNotificaciones;


@Component
public class ManejadorDeEventos {
    private final ServicioDeNotificaciones servicio;

    public ManejadorDeEventos(ServicioDeNotificaciones servicio) {
        this.servicio = servicio;
    }

    public void manejar(SolicitudEnviada evento) {
        servicio.alRecibirSolicitudEnviada(evento);
    }

    public void manejar(SolicitudAprobada evento) {
        servicio.alRecibirSolicitudAprobada(evento);
    }
}
