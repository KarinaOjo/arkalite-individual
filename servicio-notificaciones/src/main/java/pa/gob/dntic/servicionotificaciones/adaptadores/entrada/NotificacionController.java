package pa.gob.dntic.servicionotificaciones.adaptadores.entrada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pa.gob.dntic.servicionotificaciones.dominio.Notificacion;
import pa.gob.dntic.servicionotificaciones.dominio.ServicioDeNotificaciones;
import pa.gob.dntic.servicionotificaciones.eventos.SolicitudAprobada;
import pa.gob.dntic.servicionotificaciones.eventos.SolicitudEnviada;

import java.util.List;


@RestController
public class NotificacionController {
    private final ServicioDeNotificaciones servicio;
    private final ManejadorDeEventos manejadorDeEventos;

    public NotificacionController(ServicioDeNotificaciones servicio, ManejadorDeEventos manejadorDeEventos) {
        this.servicio = servicio;
        this.manejadorDeEventos = manejadorDeEventos;
    }

    @GetMapping("/notificaciones")
    public List<Notificacion> todas() {
        return servicio.listar();
    }

    @PostMapping("/solicitud-enviada")
    public void recibirSolicitudEnviada(@RequestBody SolicitudEnviada evento) {
        manejadorDeEventos.manejar(evento);
    }

    @PostMapping("/solicitud-aprobada")
    public void recibirSolicitudAprobada(@RequestBody SolicitudAprobada evento) {
        manejadorDeEventos.manejar(evento);
    }
}
