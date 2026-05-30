package com.franciscomolina.stockflow_api.common;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Resuelve el usuario autenticado que debe quedar registrado en los campos
 * de auditoría de las entidades.
 *
 * <p>El fallback {@code system} permite que la aplicación pueda persistir datos
 * fuera de una petición autenticada, por ejemplo durante procesos internos,
 * arranques controlados, jobs batch o inicializaciones técnicas.</p>
 */

public class SecurityContextAuditorAware  implements AuditorAware<String> {

    private static final String SYSTEM_AUDITOR = "system";
    /**
     * Obtiene el identificador del usuario autenticado desde Spring Security.
     *
     * <p>Si no existe autenticación válida, se devuelve {@code system} para
     * mantener siempre una trazabilidad consistente y evitar valores nulos en
     * columnas obligatorias de auditoría.</p>
     *
     * @return auditor actual que será usado por Spring Data JPA
     */

    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return Optional.of(SYSTEM_AUDITOR);
        }

        String username = authentication.getName();

        if (username == null || username.isBlank()) {
            return Optional.of(SYSTEM_AUDITOR);
        }

        return Optional.of(username);
    }
}
