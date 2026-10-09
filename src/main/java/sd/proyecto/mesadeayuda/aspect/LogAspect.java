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
    public void antesDeEjecutar(
            JoinPoint joinPoint) {

        String metodo = joinPoint.getSignature().getName();

        logger.info("");
        logger.info("========================================");
        logger.info("INICIANDO MÉTODO: {}", metodo);
        logger.info("========================================");

        Object[] parametros = joinPoint.getArgs();

        if (parametros.length > 0) {

            logger.info("Parámetros:");

            for (Object parametro : parametros) {
                logger.info(" - {}", parametro);
            }

        } else {

            logger.info("Sin parámetros.");
        }

        logger.info("========================================");
    }

    @AfterReturning(pointcut = "execution(* sd.proyecto.mesadeayuda.service..*(..))")
    public void despuesDeEjecutar(
            JoinPoint joinPoint) {

        String metodo = joinPoint.getSignature().getName();

        logger.info("");
        logger.info("========================================");
        logger.info("MÉTODO COMPLETADO: {}", metodo);
        logger.info("========================================");
    }

    @AfterThrowing(pointcut = "execution(* sd.proyecto.mesadeayuda.service..*(..))", throwing = "error")
    public void cuandoFalla(
            JoinPoint joinPoint,
            Throwable error) {

        String metodo = joinPoint.getSignature().getName();

        logger.error("");
        logger.error("========================================");
        logger.error("ERROR EN MÉTODO: {}", metodo);
        logger.error("========================================");
        logger.error("Mensaje: {}", error.getMessage());
        logger.error("========================================");
    }
}