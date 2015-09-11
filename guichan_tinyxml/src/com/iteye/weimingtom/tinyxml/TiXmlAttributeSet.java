package com.iteye.weimingtom.tinyxml;

public class TiXmlAttributeSet {
	public TiXmlAttributeSet() {
		sentinel.setNext(sentinel);
		sentinel.setPrev(sentinel);
	}
	
	public void destroy() {
		assert(sentinel.getNext() == sentinel);
		assert(sentinel.getPrev() == sentinel);		
	}

	public void Add(TiXmlAttribute addMe) {
		assert(Find(addMe.Name()) == null);
		
		addMe.setNext(sentinel);
		addMe.setPrev(sentinel.getPrev());

		sentinel.getPrev().setNext(addMe);
		sentinel.setPrev(addMe);
	}
	
	public void Remove(TiXmlAttribute removeMe) {
		TiXmlAttribute node;

		for (node = sentinel.getNext(); node != sentinel; node = node.getNext()) {
			if (node == removeMe) {
				node.getPrev().setNext(node.getNext());
				node.getNext().setPrev(node.getPrev());
				node.setNext(null);
				node.setPrev(null);
				return;
			}
		}
		assert(false);
	}

	public TiXmlAttribute First() { 
		return (this.sentinel.getNext() == this.sentinel) ? null : this.sentinel.getNext(); 
	}
	
	public TiXmlAttribute Last() { 
		return (this.sentinel.getPrev() == this.sentinel) ? null : this.sentinel.getPrev(); 
	}
	
	public TiXmlAttribute Find(String name) {
		TiXmlAttribute node;

		for (node = sentinel.getNext(); node != sentinel; node = node.getNext()) {
			if (node.Name() != null && name != null && node.Name().equals(name)) { //FIXME:
				return node;
			}
		}
		return null;
	}

	private TiXmlAttribute sentinel = new TiXmlAttribute();
}
