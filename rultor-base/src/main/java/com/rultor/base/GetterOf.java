/**
 * Copyright (c) 2009-2013, rultor.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the rultor.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.rultor.base;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.rultor.spi.Proxy;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;

/**
 * Retrieves a string property from an object.
 *
 * @author Krzysztof Krason (Krzysztof.Krason@gmail.com)
 * @version $Id$
 * @since 1.0
 */
@Immutable
@EqualsAndHashCode(of = { "source", "property" })
@Loggable(Loggable.DEBUG)
public final class GetterOf implements Proxy<String> {

    /**
     * Object from which to get property.
     */
    private final transient Object source;

    /**
     * Name of the property to retrieve.
     */
    private final transient String property;

    /**
     * Public ctor.
     * @param src Source of the property.
     * @param prop Property name.
     */
    public GetterOf(
        @NotNull(message = "object can't be null") final Object src,
        @NotNull(message = "property can't be null") final String prop
    ) {
        this.source = src;
        this.property = prop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String object() {
        try {
            for (PropertyDescriptor descr : Introspector
                .getBeanInfo(this.source.getClass()).getPropertyDescriptors()) {
                if (descr.getName().equals(this.property)
                    && (descr.getReadMethod() != null)
                    && (descr.getPropertyType() == String.class)) {
                    return (String) descr.getReadMethod().invoke(this.source);
                }
            }
            throw new IllegalArgumentException(
                String.format(
                    "Object should have a getter for a String property '%s'",
                    this.property
                )
            );
        } catch (IntrospectionException ex) {
            throw new IllegalArgumentException(ex);
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

}
