package memory;

import domain.Tema;
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

public class InXMLTemaRepository extends InMemoryRepository<Long, Tema> {

    private String fileName;

    public InXMLTemaRepository(Validator<Tema> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadDataXML();
    }

    private void loadDataXML(){
        try {

            //File fXmlFile = new File("C:\\Users\\Mano\\Desktop\\Mano\\Facultate\\MAP\\Lab4\\src\\tema.xml");
            File fXmlFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("tema");


            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    Long id = Long.parseLong(eElement.getElementsByTagName("id").item(0).getTextContent());
                    String desc =  eElement.getElementsByTagName("desc").item(0).getTextContent();
                    int currentWeek = Integer.parseInt(eElement.getElementsByTagName("startWeek").item(0).getTextContent());
                    int deadlineWeek = Integer.parseInt(eElement.getElementsByTagName("deadlineWeek").item(0).getTextContent());


                    Tema student= new Tema(id,desc,currentWeek,deadlineWeek);

                    super.save(student);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFileXML(){
        //String xmlFilePath = "C:\\Users\\Mano\\Desktop\\Mano\\Facultate\\MAP\\Lab4\\src\\tema.xml";

        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement("class");
            document.appendChild(root);
            for (Tema s:findAll()) {

                // employee element
                Element employee = document.createElement("tema");

                root.appendChild(employee);

                //ID element
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(s.getId())));
                employee.appendChild(id);

                // firstname element
                Element desc = document.createElement("desc");
                desc.appendChild(document.createTextNode(s.getDesc()));
                employee.appendChild(desc);

                // grupa element
                Element startWeek = document.createElement("startWeek");
                startWeek.appendChild(document.createTextNode(String.valueOf(s.getStartWeek())));
                employee.appendChild(startWeek);

                // email element
                Element deadlineWeek = document.createElement("deadlineWeek");
                deadlineWeek.appendChild(document.createTextNode(String.valueOf(s.getDeadlineWeek())));
                employee.appendChild(deadlineWeek);
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
    public Tema save(Tema elem) {
        Tema st=super.save(elem);
        writeFileXML();
        return st;
    }

    @Override
    public Tema delete(Long aLong) {
        Tema st= super.delete(aLong);
        writeFileXML();
        return st;
    }

    @Override
    public Tema update(Tema entity) {
        Tema st= super.update(entity);
        writeFileXML();
        return st;
    }

}