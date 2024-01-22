package az.projectdailyreport.projectdailyreport.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CrocusoftEmailValidator implements ConstraintValidator<CrocusoftEmail, String> {
    @Override
    public void initialize(CrocusoftEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // E-posta adresi içinde "crocusoft@" ifadesinin kontrolü
        return value != null && value.contains("crocusoft@");
    }
}

