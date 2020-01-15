package validator;

import domain.Tema;


public class ValidationTema extends ValidationException implements Validator<Tema>{

    public void validate(Tema entity) throws ValidationException {


        if(entity.getDesc().equals(""))
            throw new ValidationException("Desc nu poate fi vida!");
        if(entity.getStartWeek()>14 || entity.getStartWeek()<1 || entity.getDeadlineWeek()>14 || entity.getDeadlineWeek()<1)
            throw new ValidationException("StartWeek sau Deadline nu este in timpul semestrului!");
        if(entity.getDeadlineWeek()<entity.getStartWeek())
            throw new ValidationException("StartWeek nu poate fi dupa deadline!");

    }
}