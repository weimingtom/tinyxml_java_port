package com.iteye.weimingtom.tinyxml;

import java.io.PrintStream;

public class TiXmlComment extends TiXmlNode {
	public TiXmlComment() {
		this(TiXmlNode.NodeType.COMMENT);
	}
	
	//@Override
	public void destroy() {
		
	}
	
	@Override
	public void Print(PrintStream fp, int depth) {
		for (int i = 0; i < depth; i++) {
			fp.printf("    " );
		}
		fp.printf("<!--%s-->", value);		
	}
	
	protected TiXmlComment(NodeType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TiXmlNode Clone() {
		TiXmlComment clone = new TiXmlComment();

		if (clone == null) {
			return null;
		}

		CopyToClone(clone);
		return clone;
	}

	@Override
	public int Parse(String str, int p) {
		assert(p >= 0 && p < str.length() && 
			str.charAt(p) == '!' && 
			str.charAt(p + 1) == '-' && 
			str.charAt(p + 2) == '-');

		int start = p + 3;
		int end = str.indexOf("-->", p);
		if (end == -1) {
			TiXmlDocument document = GetDocument();
			if (document != null) {
				document.SetError(TIXML_ERROR_PARSING_COMMENT);
			}
			return -1;  //FIXME:
		} else {
			boolean whiteSpace = false;

			int q;
			for (q = start; q < end; q++) {
				if (Character.isWhitespace(str.charAt(q))) {
					if (!whiteSpace) {
						value += ' ';
						whiteSpace = true;
					}
				} else {
					value += str.charAt(q);
					whiteSpace = false;
				}
			}
			return end + 3;
		}
	}

}
