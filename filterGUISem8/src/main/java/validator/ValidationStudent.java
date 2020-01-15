package validator;

import domain.Student;



public class ValidationStudent extends  ValidationException implements Validator<Student>{

    public void validate(Student entity) throws ValidationException {
        String err = "";
        if(entity.getNume().equals(""))
            err += "Numele nu poate fi vid ";
        if(entity.getEmail().equals(""))
            err += "Emailul nu poate fi vid ";
        if(entity.getProf().equals(""))
            err += "Profesorul nu poate fi vid ";
        if(err.length() > 0)
            throw new ValidationException(err);
    }

}