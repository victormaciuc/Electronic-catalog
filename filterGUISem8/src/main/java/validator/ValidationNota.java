package validator;

import domain.Nota;

public class ValidationNota extends ValidationException implements  Validator<Nota> {
    @Override
    public void validate(Nota entity) throws ValidationException {
        String err = "";
        if(entity.getValue() < 1 || entity.getValue() > 10)
            err += "Valoare invalida!";
        if(!err.equals(""))
            throw new ValidationException(err);
    }
}
