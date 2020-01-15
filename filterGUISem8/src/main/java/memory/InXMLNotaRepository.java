package memory;

import domain.Nota;
import domain.Pair;
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
import java.time.LocalDateTime;

public class InXMLNotaRepository  extends InMemoryRepository<Pair, Nota>{

    private String fileName;

    public InXMLNotaRepository(Validator<Nota> validator, String fileName) {
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

            NodeList nList = doc.getElementsByTagName("nota");


            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    Long idS = Long.parseLong(eElement.getElementsByTagName("idStudent").item(0).getTextContent());
                    Long idT = Long.parseLong(eElement.getElementsByTagName("idTema").item(0).getTextContent());
                    int an = Integer.parseInt(eElement.getElementsByTagName("an").item(0).getTextContent());
                    int luna = Integer.parseInt(eElement.getElementsByTagName("luna").item(0).getTextContent());
                    int zi = Integer.parseInt(eElement.getElementsByTagName("zi").item(0).getTextContent());
                    String feedback =  eElement.getElementsByTagName("feedback").item(0).getTextContent();
                    Float value = Float.parseFloat(eElement.getElementsByTagName("value").item(0).getTextContent());

                    LocalDateTime d1 = LocalDateTime.of(an,luna,zi,0,0);

                    Nota student= new Nota(idS,idT,d1,value,feedback);

                    super.save(student);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFileXML(){

        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("class");
            document.appendChild(root);
            for (Nota s:findAll()) {

                // employee element
                Element employee = document.createElement("nota");

                root.appendChild(employee);

                //ID element
                Element idS = document.createElement("idStudent");
                idS.appendChild(document.createTextNode(String.valueOf(s.getId().getFirst())));
                employee.appendChild(idS);

                Element idT = document.createElement("idTema");
                idT.appendChild(document.createTextNode(String.valueOf(s.getId().getSecond())));
                employee.appendChild(idT);


                Element an = document.createElement("an");
                an.appendChild(document.createTextNode(String.valueOf(s.getData().getYear())));
                employee.appendChild(an);

                Element luna = document.createElement("luna");
                luna.appendChild(document.createTextNode(String.valueOf(s.getData().getMonthValue())));
                employee.appendChild(luna);

                Element zi = document.createElement("zi");
                zi.appendChild(document.createTextNode(String.valueOf(s.getData().getDayOfMonth())));
                employee.appendChild(zi);

                //nota
                Element nota = document.createElement("value");
                nota.appendChild(document.createTextNode(String.valueOf(s.getValue())));
                employee.appendChild(nota);

                // feedback element
                Element firstName = document.createElement("feedback");
                firstName.appendChild(document.createTextNode(s.getFeedback()));
                employee.appendChild(firstName);


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
    public Nota save(Nota elem) {
        Nota st=super.save(elem);
        writeFileXML();
        return st;
    }

    @Override
    public Nota delete(Pair aLong) {
        Nota st= super.delete(aLong);
        writeFileXML();
        return st;
    }

    @Override
    public Nota update(Nota entity) {
        Nota st= super.update(entity);
        writeFileXML();
        return st;
    }


}
