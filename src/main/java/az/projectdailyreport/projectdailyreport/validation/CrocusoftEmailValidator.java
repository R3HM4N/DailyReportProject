package az.projectdailyreport.projectdailyreport.validation;
import az.projectdailyreport.projectdailyreport.exception.MailValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CrocusoftEmailValidator implements ConstraintValidator<CrocusoftEmail, String> {
    @Override
    public void initialize(CrocusoftEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && value.contains("@crocusoft")) {
            return true;
        } else {
            throw new MailValidationException("Geçerli bir e-posta adresi değil");
        }
    }
}

