package memory;

import domain.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import validator.Validator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class InXMLStudentRepository  extends InMemoryRepository<Long, Student>{

    private String fileName;

    public InXMLStudentRepository(Validator<Student> validator,String fileName) {
        super(validator);
        this.fileName = fileName;
        loadDataXML();
    }

    private void loadDataXML(){
        try {

            //File fXmlFile = new File("C:\\Users\\Mano\\Desktop\\Mano\\Facultate\\MAP\\Lab4\\src\\student.xml");
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("student");


            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    Long id = Long.parseLong(eElement.getElementsByTagName("id").item(0).getTextContent());
                    String nume =  eElement.getElementsByTagName("nume").item(0).getTextContent();
                    int grupa = Integer.parseInt(eElement.getElementsByTagName("grupa").item(0).getTextContent());
                    String email =  eElement.getElementsByTagName("email").item(0).getTextContent();
                    String cadruDidactic =  eElement.getElementsByTagName("cadru_didactic").item(0).getTextContent();

                    Student student= new Student(id,nume,grupa,email,cadruDidactic);

                    super.save(student);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFileXML(){
        String xmlFilePath = "C:\\Users\\Mano\\Desktop\\Mano\\Facultate\\MAP\\Lab4\\src\\student.xml";

        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("class");
            document.appendChild(root);
            for (Student s:findAll()) {

                // employee element
                Element employee = document.createElement("student");

                root.appendChild(employee);

                //ID element
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(s.getId())));
                employee.appendChild(id);

                // firstname element
                Element firstName = document.createElement("nume");
                firstName.appendChild(document.createTextNode(s.getNume()));
                employee.appendChild(firstName);

                // grupa element
                Element lastname = document.createElement("grupa");
                lastname.appendChild(document.createTextNode(String.valueOf(s.getGrupa())));
                employee.appendChild(lastname);

                // email element
                Element email = document.createElement("email");
                email.appendChild(document.createTextNode(s.getEmail()));
                employee.appendChild(email);

                // prof elements
                Element department = document.createElement("cadru_didactic");
                department.appendChild(document.createTextNode(s.getProf()));
                employee.appendChild(department);
            }

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileName));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);


        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    @Override
    public Student save(Student elem) {
        Student st=super.save(elem);
        writeFileXML();
        return st;
    }

    @Override
    public Student delete(Long aLong) {
        Student st= super.delete(aLong);
        writeFileXML();
        return st;
    }

    @Override
    public Student update(Student entity) {
        Student st= super.update(entity);
        writeFileXML();
        return st;
    }


}

