package com.miicaa.detail;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.text.Editable;

public class MyContentHandler extends DefaultHandler{
    String code ;
    int position;
    int p = 0;
    Editable editable;
    int start;
    public MyContentHandler(int position,Editable editable,int start){
        this.position = position;
        this.editable = editable;
        this.start = start;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("span".equalsIgnoreCase(localName)){
            for (int i = 0 ; i < attributes.getLength();i++){
                String codeW = attributes.getLocalName(i);
                if ("code".equalsIgnoreCase(codeW)){
//                   if ( position == p){
                       code = attributes.getValue(i);

//                   }
                }
            }
        }
        p++;
    }
}