package com.iteye.weimingtom.tinyxml;

import java.io.PrintStream;

public class TiXmlElement extends TiXmlNode {
	public TiXmlElement(String _value) {
		super(TiXmlNode.NodeType.ELEMENT);
		firstChild = lastChild = null;
		value = _value;
	}
	
	@Override
	public void destroy() {
		while (attributeSet.First() != null) {
			TiXmlAttribute node = attributeSet.First();
			attributeSet.Remove(node);
			node.destroy();
			node = null;
		}
	}

	//FIXME: return pointer
	public String Attribute(String name) {
		TiXmlAttribute node = attributeSet.Find(name);

		if (node != null) {
			return node.Value(); //FIXME:
		}
		return null;
	}
	
	//FIXME: return pointer
	public String Attribute(String name, int[] i) {
		String s = Attribute(name);
		if (s != null) {
			i[0] = Integer.parseInt(s);
		} else {
			i[0] = 0;
		}
		return s;
	}

	public void SetAttribute(String name, String value) {
		TiXmlAttribute node = attributeSet.Find(name);
		if (node != null) {
			node.SetValue(value);
			return;
		}

		TiXmlAttribute attrib = new TiXmlAttribute(name, value);
		if (attrib != null) {
			attributeSet.Add(attrib);
		} else {
			TiXmlDocument document = GetDocument();
			if (document != null) {
				document.SetError(TIXML_ERROR_OUT_OF_MEMORY);
			}
		}
	}

	public void SetAttribute(String name, int val) {
		String buf = String.format("%d", val);

		String v = buf;

		SetAttribute(name, v);		 
	}

	public void RemoveAttribute(String name) {
		TiXmlAttribute node = attributeSet.Find(name);
		if (node != null) {
			attributeSet.Remove(node);
			node.destroy();
			node = null;
		}
	}

	public TiXmlAttribute FirstAttribute()	{ 
		return attributeSet.First(); 
	}
	
	public TiXmlAttribute LastAttribute()	{ 
		return attributeSet.Last(); 
	}
	
	@Override
	public void Print(PrintStream fp, int depth) {
		int i;
		for (i = 0; i < depth; i++) {
			fp.printf("    ");
		}

		fp.printf("<%s", value);

		TiXmlAttribute attrib;
		for (attrib = attributeSet.First(); attrib != null; attrib = attrib.Next()) {	
			fp.printf(" ");
			attrib.Print(fp, 0);
		}
		TiXmlNode node;
		if (firstChild != null) { 		
			fp.printf(">");

			for ( node = firstChild; node != null; node = node.NextSibling()) {
		 		if (node.ToText() == null) {
					fp.printf("\n");
		 		}
				node.Print(fp, depth + 1);
			}
	 		fp.printf("\n");
			for (i = 0; i < depth; i++) {
				fp.printf("    ");
			}
			fp.printf("</%s>", value);
		} else {
			fp.printf(" />" );
		}
	}
	
	protected int ReadValue(String str, int p) {
		TiXmlDocument document = GetDocument();

		p = SkipWhiteSpace(str, p);
		while (p >= 0 && p < str.length()) {
			int start = p;
			while ( p >= 0 && p < str.length() && str.charAt(p) != '<' ) {
				p++;
			}
			if (p < 0 || p >= str.length()) {
				if (document != null) {
					document.SetError(TIXML_ERROR_READING_ELEMENT_VALUE);
				}
				return -1;  //FIXME:
			}
			if (p != start) {
				TiXmlText text = new TiXmlText();

				if (text == null) {
					if (document != null) {
						document.SetError(TIXML_ERROR_OUT_OF_MEMORY);
					}
					return -1; //FIXME:
				}
				text.Parse(str, start);
				if (!text.Blank()) {
					LinkEndChild(text);
				} else {
					text.destroy();
					text = null;
				}
			} else {
				if (str.charAt(p + 1) == '/') {
					return p;	// end tag
				} else {
					int[] tempP = new int[1];
					tempP[0] = p;
					TiXmlNode node = IdentifyAndParse(str, tempP);
					p = tempP[0];
					if (node != null) {
						LinkEndChild(node);
					} else {
						return -1; //FIXME:
					}
				}
			}
		}
		return -1; //FIXME:
	}

	private TiXmlAttributeSet attributeSet = new TiXmlAttributeSet();
	
	protected TiXmlElement(NodeType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TiXmlNode Clone() {
		TiXmlElement clone = new TiXmlElement(Value());

		if (clone == null) {
			return null;
		}
		
		CopyToClone(clone);

		// Clone the attributes, then clone the children.
		TiXmlAttribute attribute = null;
		for (attribute = attributeSet.First(); 
			attribute != null; 
			attribute = attribute.Next()) {
			clone.SetAttribute(attribute.Name(), attribute.Value());
		}
		
		TiXmlNode node = null;
		for (node = firstChild; node != null; node = node.NextSibling()) {
			clone.LinkEndChild(node.Clone());
		}
		return clone;
	}

	@Override
	public int Parse(String str, int p) {
		TiXmlDocument document = GetDocument();
		p = SkipWhiteSpace(str, p);
		if (p < 0 || p >= str.length()) {
			if (document != null) {
				document.SetError(TIXML_ERROR_PARSING_ELEMENT);
			}
			if (TiXmlNode.DEBUG_PARSER) {
				System.out.println("p == " + p + ", str.charAt(p) == " + str.charAt(p) + ", text == " + str.substring(0, p));
			}
			return -1;  //FIXME:
		}
		String[] tempValue = new String[1];
		tempValue[0] = value;
		p = ReadName(str, p, tempValue);
		value = tempValue[0];
		
		if (p < 0 || p >= str.length()) {
			if (document != null)	{
				document.SetError( TIXML_ERROR_FAILED_TO_READ_ELEMENT_NAME);
			}
			return -1;  //FIXME:
		}

		String endTag = "</";
		endTag += value;
		endTag += ">";

		while (p >= 0 && p < str.length()) {
			p = SkipWhiteSpace(str, p);
			if (p < 0 || p >= str.length()) {
				if (document != null) {
					document.SetError(TIXML_ERROR_READING_ATTRIBUTES);
				}
				return -1;  //FIXME:
			}
			if (str.charAt(p) == '/') {
				if (str.charAt(p + 1) != '>' ) {
					if (document != null) {
						document.SetError(TIXML_ERROR_PARSING_EMPTY);		
					}
					return -1;  //FIXME:
				}
				return p + 2;
			} else if (str.charAt(p) == '>') {
				p = ReadValue(str, p + 1);		
				if (p < 0 || p >= str.length()) {
					return -1;   //FIXME:
				}
				String buf = str.substring(p, p + endTag.length());
				if (endTag != null && buf != null && endTag.equals(buf)) {
					return p + endTag.length();
				} else {
					if (document != null) {
						document.SetError(TIXML_ERROR_READING_END_TAG);
					}
					return -1;   //FIXME:
				}
			} else {
				TiXmlAttribute attrib = new TiXmlAttribute();
				attrib.SetDocument(document);
				p = attrib.Parse(str, p);
				if (p >= 0 && p < str.length()) {
					SetAttribute(attrib.Name(), attrib.Value());
				}
			}
		}
		return -1; //FIXME:
	}
}
