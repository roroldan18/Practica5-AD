import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String path = "./src/MusicLibrary.xml";

        //System.out.println("Dom con try catch");
        //domConTryCatch(path);
        //domConThrow(path);

        //System.out.println("Sax con try catch");
        //saxConTryCatch(path);
        //saxConThrow(path);

        //Metodo para buscar un artista por su nombre con DOM
        buscarArtista(path);

    }

    public static void buscarArtista(String path){
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el nombre del artista que quieres buscar: ");
        String artista = sc.nextLine();
        System.out.println("Buscando artista: " + artista);
        buscarArtistaDom(artista, path);
    }



    public static void domConTryCatch(String path){
        // Instancio DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Atirbuto en true para que el fichero xml sea validado
        factory.setValidating(true);
        // Atributo en true para ignorar los espacios en blanco
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            File file = new File(path);
            Document doc = builder.parse(file);
            Node root = doc.getDocumentElement();
            listarElementosRecursivamente(root);

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public static void buscarArtistaDom(String artista, String path) {
        try {
            boolean artistaEncontrado = false;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));

            NodeList artistas = doc.getElementsByTagName("Artist");

            for (int i = 0; i < artistas.getLength(); i++) {
                Element artistaElement = (Element) artistas.item(i);
                String nombreArtista = artistaElement.getElementsByTagName("Name").item(0).getTextContent().trim();

                if (nombreArtista.equalsIgnoreCase(artista)) {
                    artistaEncontrado = true;
                    NodeList elementosArtista = artistaElement.getChildNodes();
                    System.out.println("Artista encontrado: " + nombreArtista);

                    for (int j = 0; j < elementosArtista.getLength(); j++) {
                        Node elemento = elementosArtista.item(j);

                        if (elemento.getNodeType() == Node.ELEMENT_NODE) {
                            System.out.println("Elemento: " + elemento.getNodeName());
                            System.out.println("Contenido: " + elemento.getTextContent().trim());
                        }
                    }
                }
            }
            if(!artistaEncontrado){
                System.out.println("Artista no encontrado");
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void domConThrow(String path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = null;
        builder = factory.newDocumentBuilder();
        File file = new File(path);
        Document doc = builder.parse(file);
        Node root = doc.getDocumentElement();
        listarElementosRecursivamente(root);
    }



    public static void saxConTryCatch(String path) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            SAXParser saxParser = factory.newSAXParser();

            // Crea un manejador personalizado que extiende DefaultHandler
            DefaultHandler customHandler = new DefaultHandler() {
                private String currentElement;
                private StringBuilder currentText;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    // Imprimir el nombre del elemento
                    System.out.println("Elemento: " + qName);

                    // Imprimir atributos, si los hay
                    for (int i = 0; i < attributes.getLength(); i++) {
                        System.out.println("Atributo: " + attributes.getQName(i) + " = " + attributes.getValue(i));
                    }

                    currentElement = qName;
                    currentText = new StringBuilder();
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (currentElement != null) {
                        currentText.append(ch, start, length);
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (currentElement != null) {
                        // Imprimir el contenido del elemento
                        System.out.println("Contenido: " + currentText.toString());
                    }
                }
            };

            File file = new File(path);
            saxParser.parse(file, customHandler);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    private static void saxConThrow(String path) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        SAXParser saxParser = factory.newSAXParser();

        // Crea un manejador personalizado que extiende DefaultHandler
        DefaultHandler customHandler = new DefaultHandler() {
            private String currentElement;
            private StringBuilder currentText;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                // Imprimir el nombre del elemento
                System.out.println("Elemento: " + qName);

                // Imprimir atributos, si los hay
                for (int i = 0; i < attributes.getLength(); i++) {
                    System.out.println("Atributo: " + attributes.getQName(i) + " = " + attributes.getValue(i));
                }

                currentElement = qName;
                currentText = new StringBuilder();
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (currentElement != null) {
                    currentText.append(ch, start, length);
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                if (currentElement != null) {
                    // Imprimir el contenido del elemento
                    System.out.println("Contenido: " + currentText.toString());
                }
            }
        };

        File file = new File(path);
        saxParser.parse(file, customHandler);
    }

    public static void listarElementosRecursivamente(Node nodo) {
        // Verificamos si el nodo es un elemento (y no un texto u otro tipo de nodo)
        if (nodo.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println("Elemento: " + nodo.getNodeName() +": " + nodo.getTextContent());

            // Si el elemento tiene atributos, los listamos
            if (nodo.hasAttributes()) {
                System.out.println("Atributos:");
                // Obtenemos la lista de atributos del elemento
                for (int i = 0; i < nodo.getAttributes().getLength(); i++) {
                    Node atributo = nodo.getAttributes().item(i);
                    System.out.println(atributo.getNodeName() + ": " + atributo.getNodeValue());
                }
            }
        }

        // Llamamos a esta función de manera recursiva para procesar los hijos del nodo actual
        NodeList hijos = nodo.getChildNodes();
        for (int i = 0; i < hijos.getLength(); i++) {
            listarElementosRecursivamente(hijos.item(i));
        }
    }
}

