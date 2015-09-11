package com.iteye.weimingtom.tinyxml;

import java.io.PrintStream;

public class TiXmlDeclaration extends TiXmlNode {
	public TiXmlDeclaration() {
		this(TiXmlNode.NodeType.DECLARATION);
	}

	public TiXmlDeclaration(String _version, 
		String _encoding, String _standalone) {
		this();
		version = _version;
		encoding = _encoding;
		standalone = _standalone;
	}

	@Override
	public void destroy() {
		
	}

	public String Version()	{ 
		return this.version; 
	}

	public String Encoding() { 
		return this.encoding; 
	}
	
	public String Standalone() { 
		return standalone; 
	}

	@Override
	public void Print(PrintStream fp, int depth) {
		String out = "<?xml ";

		if (version != null && version.length() > 0) {
			out += "version=\"";
			out += version;
			out += "\" ";
		}
		if (encoding != null && encoding.length() > 0) {
			out += "encoding=\"";
			out += encoding;
			out += "\" ";
		}
		if (standalone != null && standalone.length() > 0) {
			out += "standalone=\"";
			out += standalone;
			out += "\" ";
		}
		out += "?>";

		fp.printf("%s", out);		
	}
	
	private String version;
	private String encoding;
	private String standalone;
	
	protected TiXmlDeclaration(NodeType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TiXmlNode Clone() {
		TiXmlDeclaration clone = new TiXmlDeclaration();

		if (clone == null) {
			return null;
		}

		CopyToClone(clone);
		clone.version = version;
		clone.encoding = encoding;
		clone.standalone = standalone;
		return clone;
	}

	@Override
	public int Parse(String str, int _p) {
		int start = _p + 4;
		int end = str.indexOf("?>", start);

		if (end == -1) {
			end = str.indexOf(">", start);
			end++;
		} else {
			end += 2;
		}
		
		if (end == -1) {
			TiXmlDocument document = GetDocument();
			if (document != null) {
				document.SetError(TIXML_ERROR_PARSING_DECLARATION);
			}
			return -1;  //FIXME:
		} else {
			int p;
			
			p = str.indexOf("version", start);
			if (p > 0 && p < end) {
				TiXmlAttribute attrib = new TiXmlAttribute();
				attrib.Parse(str, p);		
				version = attrib.Value();
			}

			p = str.indexOf("encoding", start);
			if (p > 0 && p < end) {
				TiXmlAttribute attrib = new TiXmlAttribute();
				attrib.Parse(str, p);
				encoding = attrib.Value();
			}

			p = str.indexOf("standalone", start);
			if (p > 0 && p < end) {
				TiXmlAttribute attrib = new TiXmlAttribute();
				attrib.Parse(str, p);		
				standalone = attrib.Value();
			}
		}
		return end;
	}

}
