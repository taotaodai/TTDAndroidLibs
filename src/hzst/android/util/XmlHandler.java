package hzst.android.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlHandler extends DefaultHandler{
	 //存储正在解析的元素的数据
	 private Map<String,String> map=null;
	 //存储所有解析的元素的数据
	 private List<Map<String,String>> list=null;
	 //正在解析的元素的名字
	 String currentTag=null;
	 //正在解析的元素的元素值
	 String currentValue=null;
	 //开始解析的元素
	 String nodeName=null;

	 public XmlHandler(String nodeName) {
		 this.nodeName = nodeName;
	 }

	/**
	 * 获取解析后的元素集合
	 * @return
	 */
	 public List<Map<String, String>> getList() {
		 return list;
	 }
	 //开始解析文档，即开始解析XML根元素时调用该方法
	 @Override
	 public void startDocument() throws SAXException {
		 System.out.println("--startDocument()--");
		 //初始化Map
		 list=new ArrayList<Map<String,String>>();
	 }
	 //开始解析每个元素时都会调用该方法
	 @Override
	 public void startElement(String uri, String localName, String qName,
	 Attributes attributes) throws SAXException {
	 //判断正在解析的元素是不是开始解析的元素
		 System.out.println("--startElement()--"+qName);
		 if(qName.equals(nodeName)){
			 map=new HashMap<String, String>();
		 }
	 //判断正在解析的元素是否有属性值,如果有则将其全部取出并保存到map对象中，如:<person id="00001"></person>
		 if(attributes!=null&&map!=null){
			 for(int i=0;i<attributes.getLength();i++){
				 map.put(attributes.getQName(i), attributes.getValue(i));
			 }
		 }
		 currentTag=qName;  //正在解析的元素
	 	}
	 //解析到每个元素的内容时会调用此方法
	 @Override
	 public void characters(char[] ch, int start, int length)throws SAXException {
		 System.out.println("--characters()--");
		 if(currentTag!=null&&map!=null){
			 currentValue=new String(ch,start,length);
			 //如果内容不为空和空格，也不是换行符则将该元素名和值和存入map中
			 if(currentValue!=null&&!currentValue.trim().equals("")&&!currentValue.trim().equals("\n")){
				 map.put(currentTag, currentValue);
				 System.out.println("-----"+currentTag+" "+currentValue);
			 }
			 //当前的元素已解析过，将其置空用于下一个元素的解析
			 currentTag=null;
			 currentValue=null;
		 }
	 }
	 //每个元素结束的时候都会调用该方法
	 @Override
	 public void endElement(String uri, String localName, String qName)throws SAXException {
		 System.out.println("--endElement()--"+qName);
		 //判断是否为一个节点结束的元素标签
		 if(qName.equals(nodeName)){
			 list.add(map);
			 map=null;
		 }
	 }
	 //结束解析文档，即解析根元素结束标签时调用该方法
	 @Override
	 public void endDocument() throws SAXException {
		 System.out.println("--endDocument()--");
		 super.endDocument();
	 }
	 /**
	  * 
	  * @param data
	  * @param rootNode
	  * @param itemNode
	  * @return
	  */
	 public String createXml(List<Map<String, String>> data,String rootNode,String itemNode){
		 try{
			 //解析器工厂类
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			 //解析器
			 DocumentBuilder builder = factory.newDocumentBuilder();
			 //操作的Document对象
			 Document document = builder.newDocument();
			 //设置XML的版本
			 document.setXmlVersion("1.0");
			 //创建根节点
			 Element root = document.createElement(rootNode);
			 //将根节点添加到Document对象中
			 document.appendChild(root);
			 
			 for (int i = 0; i < data.size(); i++) {
				 Element item = document.createElement(itemNode);
				 Map<String, String> map = data.get(i);
				 Set<String> set = map.keySet();
				 Iterator<String> it = set.iterator();
				 while(it.hasNext()){
					 String name = it.next();
					 Element element = document.createElement(name);
					 element.setTextContent(map.get(name));
					 item.appendChild(element);
				 }
				 root.appendChild(item);
			 }
			 
			 return toStringFromDoc(document);
			}catch(Exception e){
				L.showLogInfo(L.TAG_EXCEPTION, e.toString());
			}
		return "";

	 }
	 @Deprecated
	 public static String toStringFromDoc(Document document) {  
		 String result = null;  
		 if (document != null) {  
			 StringWriter strWtr = new StringWriter();  
			 StreamResult strResult = new StreamResult(strWtr);  
			 TransformerFactory tfac = TransformerFactory.newInstance();  
			 try {  
				 javax.xml.transform.Transformer t = tfac.newTransformer();  
//				 t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
//				 t.setOutputProperty(OutputKeys.INDENT, "yes");  
//				 t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,  
//				 // text  
//				 t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");  
				 t.transform(new DOMSource(document.getDocumentElement()), strResult);  
			 	} catch (Exception e) {
			 		
			 	}  
			 	result = strResult.getWriter().toString();  

		 		try {  
		 				strWtr.close();  
		 			} catch (IOException e) {  
		 				L.showLogInfo(L.TAG_EXCEPTION, e.toString());  
		 			}  
		 }  
		 	StringBuilder sb = new StringBuilder(result);
		 	sb.replace(0, 38, "<?xml version='1.0' encoding='UTF-8'?>");
		 	return sb.toString();  
	}  
}
