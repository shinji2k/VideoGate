package com.crscic.gate.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.crscic.gate.log.Log;
import com.k.reflect.ReflectUtils;
import com.k.util.StringUtils;

public class XmlHelper
{
	private SAXReader saxReader;
	private Document xmlDocument;

	public void loadXml(String xmlPath)
	{
		saxReader = new SAXReader();
		try
		{
			xmlDocument = saxReader.read(xmlPath);
		}
		catch (DocumentException e)
		{
			Log.error("加载配置文件" + xmlPath + "错误", e);
		}
	}

	public Element getSingleElement(String xpath)
	{
		if (StringUtils.isNullOrEmpty(xpath))
			return null;
		return (Element) xmlDocument.selectSingleNode(xpath);
	}

	public List<Element> getElements(String xpath)
	{
		if (StringUtils.isNullOrEmpty(xpath))
			return null;
		List<?> elements = xmlDocument.selectNodes(xpath);
		List<Element> res = new ArrayList<Element>();
		for (int i = 0; i < elements.size(); i++)
			res.add((Element) elements.get(i));
		return res;
	}

	/**
	 * 获得节点下所有子节点
	 * 
	 * @param ele
	 * @return zhaokai 2017年9月27日 下午11:20:03
	 */
	public static List<Element> getElements(Element ele)
	{
		List<Element> childEleList = new ArrayList<Element>();
		List<?> elements = ele.elements();
		for (int i = 0; i < elements.size(); i++)
			childEleList.add((Element) elements.get(i));

		return childEleList;
	}

	/**
	 * 获得节点下属性值的列表
	 * @param ele
	 * @return
	 * @author zhaokai
	 * @create 2018年1月5日 下午4:39:45
	 */
	public static List<Attribute> getAttributesList(Element ele)
	{
		List<Attribute> attrList = new ArrayList<Attribute>();
		List<?> attrObjList = ele.attributes();
		if (attrObjList.size() > 0)
		{
			for (int i = 0; i < attrObjList.size(); i++)
				attrList.add((Attribute) attrObjList.get(i));
		}

		return attrList;
	}

	/**
	 * 获得节点下属性与值的Map
	 * @param ele
	 * @return
	 * @author zhaokai
	 * @create 2018年1月5日 下午4:40:04
	 */
	public static Map<String, String> getAttributesMap(Element ele)
	{
		Map<String, String> attrMap = new HashMap<String, String>();
		
		List<?> attrObjList = ele.attributes();
		if (attrObjList.size() > 0)
		{
			for (int i = 0; i < attrObjList.size(); i++)
			{
				Attribute attr = (Attribute) attrObjList.get(i);
				attrMap.put(attr.getName(), attr.getStringValue());
			}
		}
		
		return attrMap;
	}
	
	/**
	 * 将xml某节点下所有子节点自动填装。 要求：该节点下只有一级子节点，且填装类的属性与节点名称一致。 注意：暂不支持属性的填装
	 * 
	 * @param filePath
	 * @param t
	 * @return zhaokai 2017年9月6日 下午6:33:25
	 */
	public static <T> T fill(Element element, Class<T> t)
	{
		T ret = null;
		Method[] methods = t.getDeclaredMethods();
		try
		{
			ret = t.newInstance();
			List<?> elements = element.elements();
			List<Element> nodeList = new ArrayList<Element>();
			for (int i = 0; i < elements.size(); i++)
				nodeList.add((Element) elements.get(i));

			for (Element node : nodeList)
			{
				Method setMethod = ReflectUtils.getSetMethod(methods, node.getName());
				setMethod.invoke(ret, node.getTextTrim());
			}
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	public XmlHelper()
	{
		super();
	}

	public XmlHelper(String xmlPath)
	{
		loadXml(xmlPath);
	}
}
