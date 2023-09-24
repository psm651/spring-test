package com.study.spring.xmlTest;

import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xpath.XPathAPI;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.util.Date;
import java.util.Set;

@Slf4j
public class XmlTest {
    public static String convertDocumentToString(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter stringWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        return stringWriter.toString();
    }

    @Test
    void sign() throws Exception {
        org.apache.xml.security.Init.init();

        String filePath = "unsigned.xml";

        // 클래스로더를 통해 리소스 가져오기
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(filePath);
        // Document  �����
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);
        is.close();

        // XMLSignature 객체를 만들고
        String BaseURI = "";
        XMLSignature sig = new XMLSignature(doc, BaseURI,
                XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256);
//
//        // 그 객체안의 W3C Element를 DOM안에 삽입
        {
            Element ctx = doc.createElementNS(null, "namespaceContext");
            ctx.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:tax", "urn:kr:or:kec:standard:Tax:ReusableAggregateBusinessInformationEntitySchemaModule:1:0");
            Node pivot = XPathAPI.selectSingleNode(doc, "//tax:TaxInvoiceDocument", ctx);
            pivot.getParentNode().insertBefore(sig.getElement(), pivot);
        }
//
//        //create the transforms object for the Document/Reference
        {
            Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_C14N_OMIT_COMMENTS);
//
            Element xpathElement = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:XPath");
            xpathElement.appendChild(doc.createTextNode("not(self::*[name() = 'TaxInvoice'] | ancestor-or-self::*[name() = 'ExchangedDocument'] | ancestor-or-self::ds:Signature)"));
            transforms.addTransform(Transforms.TRANSFORM_XPATH, xpathElement);
//
            sig.addDocument("", transforms, DigestMethod.SHA256);
        }
//
//        // XMLSignature에 공개키 추가하고 서명
//        sig.addKeyInfo(cert);
//        sig.sign(privateKey);
        //digestvalues구하는 함수??
        sig.getSignedInfo().generateDigestValues();
//
//// OutputStream으로 document를 String으로 변환 출력
//        XMLUtils.outputDOMc14nWithComments(doc, os);
        log.info("result = \n{}", convertDocumentToString(sig.getDocument()));
//        log.info("result = \n{}", convertDocumentToString(doc));
    }

    @Test
    void tets2() {
        // Document  �����
        org.apache.xml.security.Init.init();
        try {
            // XML 문서를 파싱
            String filePath = "unsigned.xml";
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputSource = classLoader.getResourceAsStream(filePath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true); // 네임스페이스 설정
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(inputSource);

            // XPath 객체 생성
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            // XPath 표현식 작성 및 컴파일
//            String xpathExpression = "//your-element[@attribute='value']"; // 원하는 XPath 표현식을 작성
            String xpathExpression = "not(self::*[name() = 'TaxInvoice'] | ancestor-or-self::*[name() = 'ExchangedDocument'] | ancestor-or-self::ds:Signature)";
            XPathExpression expr = xPath.compile(xpathExpression);

            // XPath를 사용하여 노드를 선택
//            NodeList nodeList = (NodeList) expr.evaluate(inputSource, XPathConstants.NODESET);
            NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            StringBuffer sb = new StringBuffer();
            // 추출한 노드를 순회하면서 작업 수행
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                // 추출한 노드에 대한 작업 수행
                String elementValue = node.getTextContent();
                sb.append(elementValue);
//                System.out.println("Found Element: " + elementValue);
            }
            log.info("result = \n{}", sb.toString());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test3() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        // Document  �����
        org.apache.xml.security.Init.init();
        // XML 문서를 파싱
        String filePath = "unsigned.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputSource = classLoader.getResourceAsStream(filePath);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true); // 네임스페이스 설정
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputSource);
//        String xml = "<root><row><col1 id='c1'>값1</col1><col2 id='c2' val='val2'>값2</col2></row>" +
//                "<row><col1 id='c3'>값3</col1><col2 id='c4'>값4</col2></row></root>";
//
        // XML Document 객체 생성
//        InputSource is = new InputSource(new StringReader(xml));
//        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

        // 인터넷 상의 XML 문서는 요렇게 생성하면 편리함.
        //Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        //                               .parse("http://www.example.com/test.xml");


        // xpath 생성
        XPath xpath = XPathFactory.newInstance().newXPath();

        String xpathExpression = "not(self::*[name() = 'TaxInvoice'] | ancestor-or-self::*[name() = 'ExchangedDocument'] | ancestor-or-self::ds:Signature)";

        // NodeList 가져오기 : row 아래에 있는 모든 col1 을 선택
        NodeList cols = (NodeList) xpath.evaluate(xpathExpression, document, XPathConstants.NODESET);
        for (int idx = 0; idx < cols.getLength(); idx++) {
            System.out.println(cols.item(idx).getTextContent());
        }
        // 값1   값3  이 출력됨


//        // id 가 c2 인 Node의 val attribute 값 가져오기
//        Node col2 = (Node) xpath.evaluate("//*[@id='c2']", document, XPathConstants.NODE);
//        System.out.println(col2.getAttributes().getNamedItem("val").getTextContent());
//        // val2 출력
//
//
//        // id 가 c3 인 Node 의 value 값 가져오기
//        System.out.println(xpath.evaluate("//*[@id='c3']", document, XPathConstants.STRING));
    }

    @Test
    void realTest() throws Exception {
        String xpathString = "not(self::*[name()='TaxInvoice'] | ancestor-or-self::*[name()='ExchangedDocument'])";
        org.apache.xml.security.Init.init();
        // XML 문서를 파싱
        String filePath = "unsigned.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputSource = classLoader.getResourceAsStream(filePath);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true); // 네임스페이스 설정
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inputSource);

        Transforms transforms = new Transforms(doc);
        transforms.addTransform(Transforms.TRANSFORM_C14N_OMIT_COMMENTS);

        Element xpathElement = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "ds:XPath");
        xpathElement.appendChild(doc.createTextNode("not(self::*[name() = 'TaxInvoice'] | ancestor-or-self::*[name() = 'ExchangedDocument'] | ancestor-or-self::ds:Signature)"));
        transforms.addTransform(Transforms.TRANSFORM_XPATH, xpathElement);

        transforms.performTransforms((XMLSignatureInput) doc.getDocumentElement());


//        XMLSignature sig = new XMLSignature(doc, "", XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256);
//        sig.addDocument("", transforms, DigestMethod.SHA256);
//        Node extractedNode = transforms.performTransform(doc.getDocumentElement());



    }
}
