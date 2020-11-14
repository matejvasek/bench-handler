package cz.xvasek.bench.handler.impl;

import io.vertx.core.MultiMap;

import java.util.*;

public class BasicReadOnlyMultiMap implements MultiMap {
    private final Map<String,String> headers;

    public BasicReadOnlyMultiMap(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String get(CharSequence charSequence) {
        return headers.get(charSequence.toString());
    }

    @Override
    public String get(String s) {
        return headers.get(s);
    }

    @Override
    public List<String> getAll(String s) {
        return Collections.singletonList(headers.get(s));
    }

    @Override
    public List<String> getAll(CharSequence charSequence) {
        return Collections.singletonList(headers.get(charSequence.toString()));
    }

    @Override
    public boolean contains(String s) {
        return headers.containsKey(s);
    }

    @Override
    public boolean contains(CharSequence charSequence) {
        return headers.containsKey(charSequence.toString());
    }

    @Override
    public boolean isEmpty() {
        return headers.isEmpty();
    }

    @Override
    public Set<String> names() {
        return headers.keySet();
    }

    @Override
    public MultiMap add(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap add(CharSequence charSequence, CharSequence charSequence1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap add(String s, Iterable<String> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap add(CharSequence charSequence, Iterable<CharSequence> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap addAll(MultiMap multiMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap addAll(Map<String, String> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap set(String s, String s1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap set(CharSequence charSequence, CharSequence charSequence1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap set(String s, Iterable<String> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap set(CharSequence charSequence, Iterable<CharSequence> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap setAll(MultiMap multiMap) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap setAll(Map<String, String> map) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap remove(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap remove(CharSequence charSequence) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiMap clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return headers.size();
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return headers.entrySet().iterator();
    }
}
