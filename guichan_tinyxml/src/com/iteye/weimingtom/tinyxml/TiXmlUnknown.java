package com.iteye.weimingtom.tinyxml;

import java.io.PrintStream;

public class TiXmlUnknown extends TiXmlNode {
	public TiXmlUnknown() {
		this(TiXmlNode.NodeType.UNKNOWN);
	}
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void Print(PrintStream fp, int depth) {
		for (int i = 0; i < depth; i++) {
			fp.printf("    ");
		}
		fp.printf("<%s>", value);		
	}
	
	protected TiXmlUnknown(NodeType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TiXmlNode Clone() {
		TiXmlUnknown clone = new TiXmlUnknown();

		if (clone == null) {
			return null;
		}

		CopyToClone(clone);
		return clone;
	}

	@Override
	public int Parse(String str, int p) {
		int end = str.indexOf('>', p);
		if (end == -1) {
			TiXmlDocument document = GetDocument();
			if (document != null) {
				document.SetError(TIXML_ERROR_PARSING_UNKNOWN );
			}
			return -1; //FIXME:
		} else {
			value = str.substring(p, end); //FIXME:
			return end + 1;
		}
	}

}
