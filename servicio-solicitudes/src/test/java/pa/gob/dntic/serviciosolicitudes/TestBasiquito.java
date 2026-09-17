package pa.gob.dntic.serviciosolicitudes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Test
void alEnviarSolicitud_debeGenerarseNotificacion() {
    // Arrange
    Solicitud solicitud = servicioSolicitudes.registrar("INC-002", "Incidencia");

    // Act
    Solicitud enviada = servicioSolicitudes.enviar(solicitud.id());

    // Assert
    assertEquals(Estado.ENVIADA, enviada.estado());
    assertEquals(1, servicioNotificaciones.listar().size());
    assertEquals(
            "Solicitud INC-002 (Incidencia) enviada",
            servicioNotificaciones.listar().get(0).texto()
    );
}

@Test
void Sumar2numeros ()
{
    assertEquals(3, 4+1);
}