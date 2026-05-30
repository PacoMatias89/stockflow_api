package com.franciscomolina.stockflow_api.config;

import com.franciscomolina.stockflow_api.common.SecurityContextAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/**
 * Configura la auditoría automática de JPA para centralizar la trazabilidad
 * técnica y funcional de las entidades persistidas.
 *
 * <p>Esta configuración evita repetir lógica de auditoría en servicios o
 * controladores y permite que Hibernate complete automáticamente quién creó
 * o modificó cada registro.</p>
 */


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    /**
     * Expone el proveedor de auditoría usado por Spring Data JPA para resolver
     * el usuario responsable de cada operación de persistencia.
     *
     * @return proveedor de auditoría basado en el contexto de seguridad actual
     */

    @Bean
    public AuditorAware<String> auditorProvider(){
        return new SecurityContextAuditorAware();
    }

}
