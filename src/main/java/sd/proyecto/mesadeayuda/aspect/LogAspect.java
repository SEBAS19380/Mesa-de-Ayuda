package sd.proyecto.mesadeayuda.aspect;
 
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
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
 
        logger.info("Iniciando método de servicio: {}", metodo);
    }
 
    @After("execution(* sd.proyecto.mesadeayuda.service..*(..))")
    public void despuesDeEjecutar(JoinPoint joinPoint) {
 
        String metodo = joinPoint.getSignature().getName();
 
        logger.info("Finalizando método de servicio: {}", metodo);
    }
}
 
