package pa.gob.dntic.serviciosolicitudes.solicitudes.dominio;

import pa.gob.dntic.serviciosolicitudes.eventos.SolicitudAprobada;
import pa.gob.dntic.serviciosolicitudes.eventos.SolicitudEnviada;

public interface PublicadorDeEventos {
    void publicar(SolicitudEnviada evento);
    void publicar(SolicitudAprobada evento);
}
