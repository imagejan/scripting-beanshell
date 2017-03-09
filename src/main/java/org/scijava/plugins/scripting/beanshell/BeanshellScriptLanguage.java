/*
 * #%L
 * JSR-223-compliant BeanShell scripting language plugin.
 * %%
 * Copyright (C) 2011 - 2017 Board of Regents of the University of
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

import bsh.Primitive;

import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;

import org.scijava.plugin.Plugin;
import org.scijava.script.AbstractScriptLanguage;
import org.scijava.script.ScriptLanguage;

/**
 * An adapter of the BeanShell interpreter to the SciJava scripting interface.
 * 
 * @author Johannes Schindelin
 * @see ScriptEngine
 */
@Plugin(type = ScriptLanguage.class, name = "BeanShell")
public class BeanshellScriptLanguage extends AbstractScriptLanguage {

	@Override
	public List<String> getExtensions() {
		return Arrays.asList("bsh", "bs");
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new BeanshellScriptEngine();
	}

	@Override
	public Object decode(final Object object) {
		if (object instanceof Primitive) {
			final Primitive p = (Primitive) object;
			if (p == Primitive.VOID || p == Primitive.NULL) {
				// NB: No return value, or null return value. So decode to null.
				return null;
			}
		}
		return object;
	}

}
