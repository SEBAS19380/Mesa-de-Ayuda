package sd.proyecto.mesadeayuda.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* sd.proyecto.mesadeayuda.service..*(..))")
    public void antesDeEjecutar(JoinPoint joinPoint) {

        String metodo = joinPoint.getSignature().getName();

        if ("agregar".equals(metodo)) {

            Object[] parametros = joinPoint.getArgs();

            logger.info("");
            logger.info("========================================");
            logger.info("           AGREGANDO TICKET");
            logger.info("========================================");
            logger.info("Método: agregar");
            logger.info("Parámetros:");

            for (Object parametro : parametros) {
                logger.info(" - {}", parametro);
            }

            logger.info("========================================");

        } else if ("listar".equals(metodo)) {

            logger.info("");
            logger.info("========================================");
            logger.info("           LISTANDO TICKETS");
            logger.info("========================================");

        }
    }

    @AfterReturning(
        pointcut = "execution(* sd.proyecto.mesadeayuda.service..*(..))"
    )
    public void despuesDeEjecutar(JoinPoint joinPoint) {

        String metodo = joinPoint.getSignature().getName();

        logger.info("");

        if ("agregar".equals(metodo)) {

            logger.info("========================================");
            logger.info("           TICKET AGREGADO");
            logger.info("========================================");

        } else if ("listar".equals(metodo)) {

            logger.info("========================================");
            logger.info("           TICKETS LISTADOS");
            logger.info("========================================");

        }
    }

    @AfterThrowing(
        pointcut = "execution(* sd.proyecto.mesadeayuda.service..*(..))",
        throwing = "error"
    )
    public void cuandoFalla(JoinPoint joinPoint, Throwable error) {

        String metodo = joinPoint.getSignature().getName();

        logger.error("");
        logger.error("========================================");
        logger.error("           OPERACIÓN FALLIDA");
        logger.error("========================================");
        logger.error("Método: {}", metodo);
        logger.error("No se pudo completar la operación.");
        logger.error("========================================");
    }
}