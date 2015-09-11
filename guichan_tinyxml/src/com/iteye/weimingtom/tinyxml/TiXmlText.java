package com.iteye.weimingtom.tinyxml;

import java.io.PrintStream;

public class TiXmlText extends TiXmlNode {
	public TiXmlText() {
		this(TiXmlNode.NodeType.TEXT);
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public void Print(PrintStream fp, int depth) {
		fp.printf("%s", value);
	}

	public boolean Blank() {
		for (int i = 0; i < value.length(); i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	protected TiXmlText(NodeType type) {
		super(type);
	}

	@Override
	public TiXmlNode Clone() {
		TiXmlText clone = null;
		clone = new TiXmlText();
		
		if (clone == null) {
			return null;
		}

		CopyToClone(clone);
		return clone;
	}

	@Override
	public int Parse(String str, int p) {
		value = "";
		boolean whitespace = false;

		p = SkipWhiteSpace(str, p);
		while (p >= 0 && p < str.length() && str.charAt(p) != '<') {
			if (str.charAt(p) == '\r' || 
				str.charAt(p) == '\n' ) {
				whitespace = true;
			} else if (Character.isWhitespace(str.charAt(p))) {
				whitespace = true;
			} else {
				if (whitespace) {
					value += ' ';
					whitespace = false;
				}
				value += str.charAt(p);
			}
			p++;
		}
		if ( whitespace ) {
			value += ' ';
		}
		return p;
	}

}
