package com.iteye.weimingtom.tinyxml;

import java.io.PrintStream;

public abstract class TiXmlBase {
	public TiXmlBase() {
		
	}
	
	public void destroy() {
		
	}
	
	public abstract void Print(PrintStream fp, int depth);

	protected static int SkipWhiteSpace(String str, int p) {
		while (p >= 0 && 
			p < str.length() && 
		    (Character.isWhitespace(str.charAt(p)) || 
		     str.charAt(p) == '\n' || 
		     str.charAt(p) == '\r')) {
			p++;
		}
		return p;
	}

	//FIXME:
	protected static int ReadName(String str, int p, String[] name) {
		name[0] = "";
		int start = p;

		if (p >= 0 && p < str.length() && 
			(Character.isLetter(str.charAt(p)) || 
			str.charAt(p) == '_')) {
			p++;
			while (p >= 0 && p < str.length() && 
				(Character.isDigit(str.charAt(p)) || 
				Character.isLetter(str.charAt(p)) || 
				str.charAt(p) == '_' || 
				str.charAt(p) == '-' || 
				str.charAt(p) == ':')) {
				p++;
			}
			name[0] += str.substring(start, p);
			return p;
		}
		return -1; //FIXME:
	}

	protected final static int TIXML_NO_ERROR = 0;
	protected final static int TIXML_ERROR_OPENING_FILE = 1;
	protected final static int TIXML_ERROR_OUT_OF_MEMORY = 2;
	protected final static int TIXML_ERROR_PARSING_ELEMENT = 3;
	protected final static int TIXML_ERROR_FAILED_TO_READ_ELEMENT_NAME = 4;
	protected final static int TIXML_ERROR_READING_ELEMENT_VALUE = 5;
	protected final static int TIXML_ERROR_READING_ATTRIBUTES = 6;
	protected final static int TIXML_ERROR_PARSING_EMPTY = 7;
	protected final static int TIXML_ERROR_READING_END_TAG = 8;
	protected final static int TIXML_ERROR_PARSING_UNKNOWN = 9;
	protected final static int TIXML_ERROR_PARSING_COMMENT = 10;
	protected final static int TIXML_ERROR_PARSING_DECLARATION = 11;
	protected final static int TIXML_ERROR_STRING_COUNT = 12;
	
	protected final static String[] errorString = {
		"No error",
		"Failed to open file",
		"Memory allocation failed.",
		"Error parsing Element.",
		"Failed to read Element name",
		"Error reading Element value.",
		"Error reading Attributes.",
		"Error: empty tag.",
		"Error reading end tag.",
		"Error parsing Unknown.",
		"Error parsing Comment.",
		"Error parsing Declaration.",
	};
}
