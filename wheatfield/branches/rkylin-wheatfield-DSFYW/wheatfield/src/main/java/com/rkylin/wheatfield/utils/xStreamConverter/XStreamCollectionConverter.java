package com.rkylin.wheatfield.utils.xStreamConverter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class XStreamCollectionConverter extends AbstractCollectionConverter{

	public XStreamCollectionConverter(Mapper mapper) {
		super(mapper);
	}

	@Override
	public void marshal(Object original, HierarchicalStreamWriter writer, MarshallingContext context){
		writer.addAttribute("list", "true");
		if(Collection.class.isAssignableFrom(original.getClass())){
			if(Map.class.isAssignableFrom(original.getClass())){
				Map map = (Map)original;
				for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();){
					Map.Entry entry = (Map.Entry)iterator.next();
				 	ExtendedHierarchicalStreamWriterHelper.startNode(writer, mapper().serializedClass(Map.Entry.class), Map.Entry.class);

				 	writeItem(entry.getKey(), context, writer);
				 	writeItem(entry.getValue(), context, writer);

				 	writer.endNode();
				 }
				
			}else{
				Collection collection = (Collection)original;
				for (Iterator iterator = collection.iterator(); iterator.hasNext();){
					Object item = iterator.next();
					writeItem(item, context, writer);
				}
			}
		}

		if(original.getClass().isArray()){
		    int length = Array.getLength(original);
		    for (int i = 0; i < length; i++){
		      Object item = Array.get(original, i);
		      writeItem(item, context, writer);
		    }
		}
	}

	@Override
	public boolean canConvert(Class type) {

		boolean checkFlag = false;

		if(type != null && (type.isArray() || Collection.class.isAssignableFrom(type))){
			checkFlag = true;
		}
		return checkFlag;
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		return null;
	}
}
