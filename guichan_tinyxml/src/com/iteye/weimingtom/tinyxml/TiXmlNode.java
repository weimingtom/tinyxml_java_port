package com.iteye.weimingtom.tinyxml;

import java.io.PrintStream;

public abstract class TiXmlNode extends TiXmlBase {
	public static final boolean DEBUG_PARSER = false;
	
	public static enum NodeType {
		DOCUMENT, 
		ELEMENT, 
		COMMENT, 
		UNKNOWN, 
		TEXT, 
		DECLARATION, 
		TYPECOUNT
	};
	
	@Override
	public void destroy() {
		TiXmlNode node = firstChild;
		TiXmlNode temp = null;

		while (node != null) {
			temp = node;
			node = node.next;
			temp.destroy();
			temp = null;
		}
	}
	
	public String Value() { 
		return value; 
	}

	public void SetValue(String _value ) { 
		value = _value; 
	}

	public void Clear() {
		TiXmlNode node = firstChild;
		TiXmlNode temp = null;

		while (node != null) {
			temp = node;
			node = node.next;
			temp.destroy();
			temp = null;
		}
		
		firstChild = null;
		lastChild = null;
	}

	public TiXmlNode Parent() { 
		return parent; 
	}

	public TiXmlNode FirstChild() { 
		return firstChild; 
	}		
	
	public TiXmlNode FirstChild(String value) {
		TiXmlNode node;
		for (node = firstChild; node != null; node = node.next) {
			if (node.Value() != null && value != null && node.Value().equals(value)) { //FIXME:
				return node;
			}
		}
		return null;
	}
	
	public TiXmlNode LastChild() { 
		return lastChild; 
	}
	
	public TiXmlNode LastChild(String value) {
		TiXmlNode node;
		for (node = lastChild; node != null; node = node.prev) {
			if (node.Value() != null && value != null && node.Value().equals(value)) {  //FIXME: 
				return node;
			}
		}
		return null;
	}

	public TiXmlNode IterateChildren(TiXmlNode previous) {
		if (previous == null) {
			return FirstChild();
		} else {
			return previous.NextSibling();
		}
	}

	public TiXmlNode IterateChildren(String val, TiXmlNode previous) {
		if (previous == null) {
			return FirstChild(val);
		} else {
			assert(previous.parent == this);
			return previous.NextSibling(val);
		}
	}
	
	public TiXmlNode InsertEndChild(TiXmlNode addThis) {
		TiXmlNode node = addThis.Clone();
		if (node == null) {
			return null;
		}

		return LinkEndChild(node);
	}

	public TiXmlNode InsertBeforeChild(TiXmlNode beforeThis, TiXmlNode addThis) {
		if (beforeThis.parent != this) {
			return null;
		}
		
		TiXmlNode node = addThis.Clone();
		if (node == null) {
			return null;
		}
		node.parent = this;
		
		node.next = beforeThis;
		node.prev = beforeThis.prev;
		beforeThis.prev.next = node;
		beforeThis.prev = node;
		return node;
	}

	public TiXmlNode InsertAfterChild(TiXmlNode afterThis, TiXmlNode addThis) {
		if (afterThis.parent != this) {
			return null;
		}
		
		TiXmlNode node = addThis.Clone();
		if (node == null) {
			return null;
		}
		node.parent = this;
		
		node.prev = afterThis;
		node.next = afterThis.next;
		afterThis.next.prev = node;
		afterThis.next = node;
		return node;
	}
	
	public TiXmlNode ReplaceChild(TiXmlNode replaceThis, TiXmlNode withThis) {
		if (replaceThis.parent != this) {
			return null;
		}
		
		TiXmlNode node = withThis.Clone();
		if (node == null) {
			return null;
		}

		node.next = replaceThis.next;
		node.prev = replaceThis.prev;

		if (replaceThis.next != null) {
			replaceThis.next.prev = node;
		} else {
			lastChild = node;
		}
		
		if (replaceThis.prev != null) {
			replaceThis.prev.next = node;
		} else {
			firstChild = node;
		}
		replaceThis.destroy();
		replaceThis = null;
		return node;
	}
	
	public boolean RemoveChild(TiXmlNode removeThis) {
		if (removeThis.parent != this) {	
			assert false; //FIXME:
			return false;
		}
		
		if (removeThis.next != null) {
			removeThis.next.prev = removeThis.prev;
		} else {
			lastChild = removeThis.prev;
		}
		if (removeThis.prev != null) {
			removeThis.prev.next = removeThis.next;
		} else {
			firstChild = removeThis.next;
		}
		removeThis.destroy();
		removeThis = null;
		return true;
	}

	public TiXmlNode PreviousSibling() { 
		return prev; 
	}

	public TiXmlNode PreviousSibling(String value) {
		TiXmlNode node;
		for (node = prev; node != null; node = node.prev) {
			if (node.Value() != null && value != null && node.Value().equals(value)) { // FIXME:
				return node;
			}
		}
		return null;
	}
	
	public TiXmlNode NextSibling() { 
		return next; 
	}

	public TiXmlNode NextSibling(String value) {
		TiXmlNode node;
		for (node = next; node != null; node = node.next) {
			if (node.Value() != null && value != null && node.Value().equals(value)) { //FIXME:
				return node;
			}
		}
		return null;
	}

	public TiXmlElement NextSiblingElement() {
		TiXmlNode node;

		for (node = NextSibling(); node != null; node = node.NextSibling()) {
			if (node.ToElement() != null) {
				return node.ToElement();
			}
		}
		return null;
	}

	public TiXmlElement NextSiblingElement(String value) {
		TiXmlNode node;

		for (node = NextSibling(value); node != null; node = node.NextSibling(value)) {
			if (node.ToElement() != null) {
				return node.ToElement();
			}
		}
		return null;
	}

	public TiXmlElement FirstChildElement() {
		TiXmlNode node;

		for (node = FirstChild(); node != null; node = node.NextSibling()) {
			if (node.ToElement() != null) {
				return node.ToElement();
			}
		}
		return null;
	}
	
	public TiXmlElement FirstChildElement(String value) {
		TiXmlNode node;

		for (node = FirstChild(value); node != null; node = node.NextSibling(value)) {
			if (node.ToElement() != null) {
				return node.ToElement();
			}
		}
		return null;
	}

	public int Type() { 
		return type.ordinal(); 
	}

	public TiXmlDocument GetDocument() {
		TiXmlNode node;

		for (node = this; node != null; node = node.parent) {
			if (node.ToDocument() != null) {
				return node.ToDocument();
			}
		}
		return null;
	}

	public TiXmlDocument ToDocument() { 
		return ( type == NodeType.DOCUMENT ) ? (TiXmlDocument) this : null; 
	} 
	
	public TiXmlElement ToElement() { 
		return ( type == NodeType.ELEMENT  ) ? (TiXmlElement)  this : null; 
	} 
	
	public TiXmlComment  ToComment() { 
		return ( type == NodeType.COMMENT  ) ? (TiXmlComment)  this : null; 
	}
	
	public TiXmlUnknown  ToUnknown() { 
		return ( type == NodeType.UNKNOWN  ) ? (TiXmlUnknown)  this : null; 
	} 
	
	public TiXmlText ToText() {
		return (type == NodeType.TEXT) ? (TiXmlText) this : null; 	
	} 
	
	public TiXmlDeclaration ToDeclaration() { 
		return (type == NodeType.DECLARATION ) ? (TiXmlDeclaration) this : null; 
	} 

	public abstract TiXmlNode Clone();

	protected TiXmlNode(NodeType _type) {
		this.parent = null;
		this.type = _type;
		this.firstChild = null;
		this.lastChild = null;
		this.prev = null;
		this.next = null;
	}
	public/*protected*/ abstract int Parse(String str, int start);

	protected TiXmlNode LinkEndChild(TiXmlNode node) {
		node.parent = this;
		
		node.prev = lastChild;
		node.next = null;

		if (lastChild != null) {
			lastChild.next = node;
		} else {
			firstChild = node;
		}
		
		lastChild = node;
		return node;
	}

	protected TiXmlNode IdentifyAndParse(String str, int[] where) {
		int p = where[0];
		TiXmlNode returnNode = null;
		assert(str.charAt(p) == '<');
		TiXmlDocument doc = GetDocument();

		p = SkipWhiteSpace(str, p + 1);
		
		if (Character.toLowerCase(str.charAt(p + 0)) == '?' && 
			Character.toLowerCase(str.charAt(p + 1)) == 'x' && 
			Character.toLowerCase(str.charAt(p + 2)) == 'm' && 
			Character.toLowerCase(str.charAt(p + 3)) == 'l' ) {
			if (DEBUG_PARSER) {
				System.out.println("XML parsing Declaration");
			}
			returnNode = new TiXmlDeclaration();
		} else if (Character.isLetter(str.charAt(p)) || 
			str.charAt(p) == '_') {
			if (DEBUG_PARSER) {
				System.out.println("XML parsing Element");
			}
			returnNode = new TiXmlElement("");
		} else if (str.charAt(p + 0) == '!' && 
			str.charAt(p + 1) == '-' && 
			str.charAt(p + 2) == '-' ) {
			if (DEBUG_PARSER) {
				System.out.println("XML parsing Comment");
			}
			returnNode = new TiXmlComment();
		} else {
			if (DEBUG_PARSER) {
				System.out.println("XML parsing Comment");
			}
			returnNode = new TiXmlUnknown();
		}

		if (returnNode != null) {
			returnNode.parent = this;
			p = returnNode.Parse(str, p);
		} else {
			if (doc != null) {
				doc.SetError(TIXML_ERROR_OUT_OF_MEMORY );
			}
			p = 0;
		}
		where[0] = p;
		return returnNode;
	}

	protected void CopyToClone(TiXmlNode target) { 
		target.value = value; 
	}

	protected TiXmlNode parent;		
	protected NodeType type;
	
	protected TiXmlNode firstChild;
	protected TiXmlNode lastChild;

	protected String value = ""; //FIXME: for +=
	
	protected TiXmlNode	prev;
	protected TiXmlNode next;
	
	
	
	
	
	
	
	
	
	
	@Override
	public void Print(PrintStream fp, int depth) {
		// TODO Auto-generated method stub

	}

}
