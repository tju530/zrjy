package org.zr;

import java.util.*;

public class ChatRoomMap<K,V> {
	public Map<K, V> map = Collections.synchronizedMap(new HashMap<K,V>());
	
	public synchronized void deleteByValue(V value)	{
		for( K key : map.keySet())	{
			if(map.get(key) == value)	{
				map.remove(key);
				break;
			}
		}
	}
	
	public synchronized Set<V> getValue()	{
		Set<V> results = new HashSet<>();
		results.addAll(map.values());
		return results;
	}
	
	public synchronized K getKeyByValue(V value)	{
		for(K key: map.keySet())	{
			if(map.get(key).equals(value) || (map.get(key) == value))	{
				return key;
			}
		}
		return null;
	}
	
	public synchronized V put(K key, V value)	{
		for(V v: map.values())	{
			if(v.equals(value) || v ==value)	{
				throw new RuntimeException("outputStream error");
			}
		}
		return map.put(key, value);
	}

}
