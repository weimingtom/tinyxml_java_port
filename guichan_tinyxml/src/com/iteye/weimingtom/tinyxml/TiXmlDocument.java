package com.iteye.weimingtom.tinyxml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class TiXmlDocument extends TiXmlNode {
	public TiXmlDocument() {
		super(TiXmlNode.NodeType.DOCUMENT);
		error = false;
	}

	public TiXmlDocument(String documentName) {
		super(TiXmlNode.NodeType.DOCUMENT);
		value = documentName;
		error = false;
	}

	@Override
	public void destroy() {
		
	}

	public boolean LoadFile() {
		return LoadFile(value);
	}

	public boolean SaveFile() {
		return SaveFile(value);
	}
	
	public boolean LoadFile(String filename) {
		Clear();

		InputStream input = null;
		try {
			input = new FileInputStream(filename);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		if (input != null) {
			String buf = "";
			try {
				byte[] bytes = new byte[input.available()];
				input.read(bytes);
				String str = new String(bytes, "UTF-8");
				buf = str;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Parse(buf, 0);
			buf = null;
			
			if (!Error()) {
				return true;
			}
		} else {
			SetError(TIXML_ERROR_OPENING_FILE);
		}
		return false;
	}
	
	public boolean SaveFile(String filename) {
		PrintStream fp = null;
		try {
			fp = new PrintStream(filename, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (fp != null) {
			Print(fp, 0);
			fp.close();
			return true;
		}
		return false;
	}

	public boolean Error() { 
		return this.error; 
	}
	
	public String ErrorDesc() { 
		return this.errorDesc; 
	}

	@Override
	public void Print(PrintStream fp, int depth) {
		TiXmlNode node;
		for (node = FirstChild(); node != null; node = node.NextSibling()) {
			node.Print(fp, 0);
			fp.printf("\n");
		}
	}
	
	public void Print(PrintStream fp)	{ 
		Print(fp, 0);
	}
	
	public void Print()	{ 
		Print(System.out, 0);
	}

	public void SetError(int err) {		
		assert(err > 0 && err < TIXML_ERROR_STRING_COUNT);
		this.error = true; 
		this.errorId = err;
		this.errorDesc = errorString[errorId]; 
	}

	private boolean error;
	private int errorId;	
	private String errorDesc;
	
	protected TiXmlDocument(NodeType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TiXmlNode Clone() {
		TiXmlDocument clone = new TiXmlDocument();
		if (clone == null) {
			return null;
		}

		CopyToClone(clone);
		clone.error = error;
		clone.errorDesc = errorDesc;

		TiXmlNode node = null;
		for (node = firstChild; node != null; node = node.NextSibling()) {
			clone.LinkEndChild(node.Clone());
		}
		return clone;
	}

	public int Parse(String str) {
		return Parse(str, 0);
	}
	
	@Override
	public int Parse(String str, int start) {
		int p = start;

	 	p = SkipWhiteSpace(str, p);
		if (p < 0 || p >= str.length()) {
			error = true;
			errorDesc = "Document empty.";
		}
		
		while (p >= 0 && p < str.length()) {
			if (TiXmlNode.DEBUG_PARSER) {
				System.out.println("p == " + p);
			}
			if (str.charAt(p) != '<') {
				if (TiXmlNode.DEBUG_PARSER) {
//					System.out.println("p == " + p + ", str.charAt(p) == " + str.charAt(p) + ", text == " + str.substring(0, p));
				}
				error = true;
				errorDesc = "The '<' symbol that starts a tag was not found.";
				break;
			} else {
				int[] tempP = new int[1];
				tempP[0] = p;
				TiXmlNode node = IdentifyAndParse(str, tempP);
				p = tempP[0];
				if (node != null) {
					LinkEndChild( node );
				}
			}
			p = SkipWhiteSpace(str, p);
		}
		return -1; //FIXME:
	}
}
