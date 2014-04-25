/*
 * #%L
 * JSR-223-compliant BeanShell scripting language plugin.
 * %%
 * Copyright (C) 2008 - 2014 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.plugins.scripting.beanshell;

import bsh.Interpreter;
import bsh.NameSpace;
import bsh.UtilEvalError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

/**
 * Local variables for Beanshell interpreters.
 * 
 * @author Johannes Schindelin
 */
public class BeanshellBindings implements Bindings {

	protected final Interpreter interpreter;
	protected final NameSpace nameSpace;

	public BeanshellBindings(final Interpreter interpreter) {
		this.interpreter = interpreter;
		nameSpace = interpreter.getNameSpace();
	}

	@Override
	public int size() {
		return nameSpace.getVariableNames().length;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		for (final Object value2 : values()) {
			if (value.equals(value2)) return true;
		}
		return false;
	}

	@Override
	public Object get(Object key) {
		try {
			return nameSpace.get((String)key, interpreter);
		} catch (UtilEvalError e) {
			return null;
		}
	}

	@Override
	public Object put(String key, Object value) {
		final Object result = get(key);
		try {
			nameSpace.setVariable(key, value, false);
		} catch (UtilEvalError e) {
			// ignore
		}
		return result;
	}

	@Override
	public Object remove(Object key) {
		final Object result = get(key);
		nameSpace.unsetVariable((String)key);
		return result;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> toMerge) {
		for (final Entry<? extends String, ? extends Object> entry : toMerge.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		nameSpace.clear();
	}

	@Override
	public Set<String> keySet() {
		final Set<String> result = new HashSet<String>();
		for (final String name : nameSpace.getVariableNames()) {
			result.add(name);
		}
		return result;
	}

	@Override
	public Collection<Object> values() {
		final List<Object> result = new ArrayList<Object>();
		for (final String name : nameSpace.getVariableNames()) try {
			result.add(nameSpace.get(name, interpreter));
		} catch (UtilEvalError exc) {
			// ignore for now
		}
		return result;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		final Set<Entry<String, Object>> result = new HashSet<Entry<String, Object>>();
		for (final String name : nameSpace.getVariableNames()) {
			result.add(new Entry<String, Object>() {

				@Override
				public String getKey() {
					return name;
				}

				@Override
				public Object getValue() {
					return get(name);
				}

				@Override
				public Object setValue(Object value) {
					throw new UnsupportedOperationException();
				}
			});
		}
		return result;
	}

}
