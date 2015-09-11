package com.iteye.weimingtom.tinyxml;

import java.io.PrintStream;

public class TiXmlAttribute extends TiXmlBase {
	public TiXmlAttribute()	{
		this.prev = null;
		this.next = null;
	}

	public TiXmlAttribute(String _name, String _value) {
		this.name = _name;
		this.value = _value;
		this.prev = null;
		this.next = null;
	}

	public String Name() { 
		return this.name; 
	}
	
	public String Value() { 
		return this.value; 
	}

	public void SetName(String _name) { 
		this.name = _name; 
	}
	
	public void SetValue(String _value)	{ 
		this.value = _value; 
	}
	
	public TiXmlAttribute Next() {
		if ((next.value == null || next.value.length() == 0) && 
			(next.name == null || next.name.length() == 0)) {
			return null;
		}
		return next;
	}
	
	public TiXmlAttribute Previous() {
		if ((prev.value == null || prev.value.length() == 0) && 
			(prev.name == null || prev.name.length() == 0)) {
			return null;
		}
		return prev;
	}

	public boolean isEqual(TiXmlAttribute rhs) { 
		return rhs.name != null && name != null && rhs.name.equals(name); 
	}
	
	public boolean isLessThan(TiXmlAttribute rhs) { 
		if (name == null) {
			return true;
		}
		if (rhs.name == null) {
			return false;
		}
		return name.compareTo(rhs.name) < 0; 
	}
	
	public boolean isLargerThan(TiXmlAttribute rhs) { 
		if (name == null) {
			return false;
		}
		if (rhs.name == null) {
			return true;
		}
		return name.compareTo(rhs.name) > 0; 
	}

	//@Override
	//FIXME:
	public int Parse(String str, int p) {
		// Read the name, the '=' and the value.
		String[] tempName = new String[1];
		tempName[0] = name;
		p = ReadName(str, p, tempName);
		name = tempName[0];
		if (p == -1) {
			if (document != null) {
				document.SetError(TIXML_ERROR_READING_ATTRIBUTES);
			}
			return -1; //FIXME:
		}
		p = SkipWhiteSpace(str, p);
		if (p < 0 || p >= str.length() || str.charAt(p) != '=') {
			if (document != null) {
				document.SetError(TIXML_ERROR_READING_ATTRIBUTES);
			}
			return -1; //FIXME:
		}

		p = SkipWhiteSpace(str, p + 1);
		if (p < 0 || p >= str.length()) {
			if (document != null) {
				document.SetError(TIXML_ERROR_READING_ATTRIBUTES);
			}
			return -1; //FIXME:
		}
		
		int end = 0;
		int start = p + 1;
		int past = 0;

		if (str.charAt(p) == '\'') {
			end = str.indexOf('\'', start);
			past = end + 1;
		} else if (str.charAt(p) == '"') {
			end = str.indexOf('"', start);
			past = end + 1;
		} else {
			start--;
			for (end = start; end < str.length(); end++) {
				if (Character.isWhitespace(str.charAt(end)) || 
					str.charAt(end) == '/' || 
					str.charAt(end) == '>') {
					break;
				}
			}
			past = end;
		}
		value = str.substring(start, end);
		return past;
	}
	
	public void SetDocument(TiXmlDocument doc) { 
		this.document = doc; 
	}

	private TiXmlDocument document;
	private String name;
	private String value;

	private TiXmlAttribute prev;
	private TiXmlAttribute next;
	
	@Override
	public void Print(PrintStream fp, int depth) {
		if (value.indexOf('\"') != -1) {
			fp.printf("%s='%s'", name, value);
		} else {
			fp.printf("%s=\"%s\"", name, value);
		}
	}

	public TiXmlAttribute getPrev() {
		return prev;
	}

	public void setPrev(TiXmlAttribute prev) {
		this.prev = prev;
	}

	public TiXmlAttribute getNext() {
		return next;
	}

	public void setNext(TiXmlAttribute next) {
		this.next = next;
	}
}
